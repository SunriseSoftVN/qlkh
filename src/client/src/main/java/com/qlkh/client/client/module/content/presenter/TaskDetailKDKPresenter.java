/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.module.content.place.TaskDetailKDKPlace;
import com.qlkh.client.client.module.content.presenter.share.AbstractTaskDetailPresenter;
import com.qlkh.client.client.module.content.view.TaskDetailKDKView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailKDKAction;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailKDKResult;
import com.qlkh.core.client.model.StationLock;
import com.qlkh.core.client.model.TaskDetailKDK;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.StringUtils;

import static com.qlkh.core.client.constant.StationLockTypeEnum.*;
import static com.qlkh.core.client.constant.TaskTypeEnum.KDK;

/**
 * The Class TaskDetailPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 1/1/12, 3:40 PM
 */
@Presenter(view = TaskDetailKDKView.class, place = TaskDetailKDKPlace.class)
public class TaskDetailKDKPresenter extends AbstractTaskDetailPresenter<TaskDetailKDKView> {

    private boolean q1Lock;
    private boolean q2Lock;
    private boolean q3Lock;
    private boolean q4Lock;

    @Override
    public ListStore<BeanModel> createSubTaskListStore() {
        RpcProxy<LoadTaskDetailKDKResult> rpcProxy = new RpcProxy<LoadTaskDetailKDKResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadTaskDetailKDKResult> callback) {
                long currentTaskId = -1;
                if (currentTask != null) {
                    currentTaskId = currentTask.getId();
                }
                dispatch.execute(new LoadTaskDetailKDKAction((BasePagingLoadConfig) loadConfig,
                        currentTaskId, currentStation.getId()), callback);
            }
        };

        PagingLoader<PagingLoadResult<TaskDetailKDK>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<TaskDetailKDK>>(rpcProxy, new LoadGridDataReader()) {
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
        // Check this station was locked or not.
        String message = StringUtils.EMPTY;
        for (StationLock stationLock : currentStation.getStationLocks()) {
            if (KDK_Q1.getCode() == stationLock.getCode()) {
                q1Lock = true;
                message += "Q1 ";
            } else if (KDK_Q2.getCode() == stationLock.getCode()) {
                q2Lock = true;
                message += ",Q2 ";
            } else if (KDK_Q3.getCode() == stationLock.getCode()) {
                q3Lock = true;
                message += ",Q3 ";
            } else if (KDK_Q4.getCode() == stationLock.getCode()) {
                q4Lock = true;
                message += ",Q4";
            }
        }
        if (q1Lock || q2Lock || q3Lock || q4Lock) {
            DiaLogUtils.notify(StringUtils.substitute(view.getConstant().lockMessage(), message));
        }

        view.setQ1Lock(q1Lock);
        view.setQ2Lock(q2Lock);
        view.setQ3Lock(q3Lock);
        view.setQ4Lock(q4Lock);
        view.createSubTaskGrid(createSubTaskListStore());
        view.getSubTaskPagingToolBar().bind((PagingLoader<?>) view.getSubTaskDetailGird().getStore().getLoader());
    }

    @Override
    protected Integer[] getTaskTypeCode() {
        return new Integer[]{KDK.getCode()};
    }
}
