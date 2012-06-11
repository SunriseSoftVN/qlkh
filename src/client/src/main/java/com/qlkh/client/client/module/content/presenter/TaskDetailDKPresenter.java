/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.TaskDetailDKPlace;
import com.qlkh.client.client.module.content.presenter.share.AbstractTaskDetailPresenter;
import com.qlkh.client.client.module.content.view.TaskDetailDKView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.action.subtask.LoadSubTaskAnnualAction;
import com.qlkh.core.client.action.subtask.LoadSubTaskAnnualResult;
import com.qlkh.core.client.constant.StationLockTypeEnum;
import com.qlkh.core.client.model.StationLock;
import com.qlkh.core.client.model.TaskDetailDK;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TaskAnnualDetailPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 1/4/12, 9:14 PM
 */
@Presenter(view = TaskDetailDKView.class, place = TaskDetailDKPlace.class)
public class TaskDetailDKPresenter extends AbstractTaskDetailPresenter<TaskDetailDKView> {

    @Override
    protected void doBind() {
        super.doBind();
        view.getBtnSubTaskRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (currentTask != null) {
                    view.getSubTaskPagingToolBar().refresh();
                }
            }
        });

        view.getBtnSubTaskSave().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                List<TaskDetailDK> taskDetailDKs = new ArrayList<TaskDetailDK>();
                for (Record record : view.getSubTaskDetailGird().getStore().getModifiedRecords()) {
                    taskDetailDKs.add(((BeanModel) record.getModel()).<TaskDetailDK>getBean());
                }
                dispatch.execute(new SaveAction(taskDetailDKs), new AbstractAsyncCallback<SaveResult>() {
                    @Override
                    public void onSuccess(SaveResult result) {
                        DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                        view.getSubTaskPagingToolBar().refresh();
                    }
                });
            }
        });
    }

    @Override
    protected ListStore<BeanModel> createSubTaskListStore() {
        RpcProxy<LoadSubTaskAnnualResult> rpcProxy = new RpcProxy<LoadSubTaskAnnualResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadSubTaskAnnualResult> callback) {
                long currentTaskId = -1;
                if (currentTask != null) {
                    currentTaskId = currentTask.getId();
                }
                dispatch.execute(new LoadSubTaskAnnualAction((BasePagingLoadConfig) loadConfig,
                        currentTaskId, currentStation.getId()), callback);
            }
        };

        PagingLoader<PagingLoadResult<TaskDetailDK>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<TaskDetailDK>>(rpcProxy, new LoadGridDataReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<BeanModel>(pagingLoader);
    }

    @Override
    protected void checkLockAndCreateSubTaskGrid() {
        view.createSubTaskGrid(createSubTaskListStore());
        view.getSubTaskPagingToolBar().bind((PagingLoader<?>) view.getSubTaskDetailGird().getStore().getLoader());

        //Check lock status.
        for (StationLock stationLock : currentStation.getStationLocks()) {
            if (StationLockTypeEnum.DK.getCode() == stationLock.getCode()) {
                DiaLogUtils.showMessage(view.getConstant().lockMessage());
                view.getContentPanel().setEnabled(false);
                break;
            }
        }
    }
}
