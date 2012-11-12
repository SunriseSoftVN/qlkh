/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.TaskDetailDKPlace;
import com.qlkh.client.client.module.content.presenter.share.AbstractTaskDetailPresenter;
import com.qlkh.client.client.module.content.view.TaskDetailDKView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailDKAction;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailDKResult;
import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.core.client.constant.StationLockTypeEnum;
import com.qlkh.core.client.model.StationLock;
import com.qlkh.core.client.model.TaskDetailDK;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

import static com.qlkh.core.client.constant.TaskTypeEnum.DK;

/**
 * The Class TaskAnnualDetailPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 1/4/12, 9:14 PM
 */
@Presenter(view = TaskDetailDKView.class, place = TaskDetailDKPlace.class)
public class TaskDetailDKPresenter extends AbstractTaskDetailPresenter<TaskDetailDKView> {

    @Override
    protected ListStore<BeanModel> createSubTaskListStore() {
        RpcProxy<LoadTaskDetailDKResult> rpcProxy = new RpcProxy<LoadTaskDetailDKResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadTaskDetailDKResult> callback) {
                long currentTaskId = -1;
                if (currentTask != null) {
                    currentTaskId = currentTask.getId();
                }
                dispatch.execute(new LoadTaskDetailDKAction((BasePagingLoadConfig) loadConfig,
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
        dispatch.execute(new GetServerTimeAction(), new AbstractAsyncCallback<GetServerTimeResult>() {
            @Override
            public void onSuccess(GetServerTimeResult result) {
                //Check lock status.
                for (StationLock stationLock : currentStation.getStationLocks()) {
                    if (StationLockTypeEnum.DK.getCode() == stationLock.getCode()) {
                        DiaLogUtils.notify(view.getConstant().lockMessage());
                        view.setLock(true);
                        break;
                    }
                }

                view.setCurrentYear(result.getYear());
                view.createSubTaskGrid(createSubTaskListStore());
                view.getSubTaskPagingToolBar().bind((PagingLoader<?>) view.getSubTaskDetailGird().getStore().getLoader());
            }
        });
    }

    @Override
    protected Integer[] getTaskTypeCode() {
        return new Integer[]{DK.getCode()};
    }
}
