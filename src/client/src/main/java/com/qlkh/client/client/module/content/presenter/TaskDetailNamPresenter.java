/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.TaskDetailNamPlace;
import com.qlkh.client.client.module.content.presenter.share.AbstractTaskDetailPresenter;
import com.qlkh.client.client.module.content.view.TaskDetailNamView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailNamAction;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailNamResult;
import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.core.client.model.StationLock;
import com.qlkh.core.client.model.TaskDetailNam;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.StringUtils;

import static com.qlkh.core.client.constant.StationLockTypeEnum.*;
import static com.qlkh.core.client.constant.TaskTypeEnum.NAM;

/**
 * The Class TaskDetailNamPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 6/11/12, 9:06 PM
 */
@Presenter(view = TaskDetailNamView.class, place = TaskDetailNamPlace.class)
public class TaskDetailNamPresenter extends AbstractTaskDetailPresenter<TaskDetailNamView> {

    private boolean q1Lock;
    private boolean q2Lock;
    private boolean q3Lock;
    private boolean q4Lock;

    @Override
    protected ListStore<BeanModel> createSubTaskListStore() {
        RpcProxy<LoadTaskDetailNamResult> rpcProxy = new RpcProxy<LoadTaskDetailNamResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadTaskDetailNamResult> callback) {
                long currentTaskId = -1;
                if (currentTask != null) {
                    currentTaskId = currentTask.getId();
                }
                dispatch.execute(new LoadTaskDetailNamAction((BasePagingLoadConfig) loadConfig,
                        currentTaskId, currentStation.getId()), callback);
            }
        };

        PagingLoader<PagingLoadResult<TaskDetailNam>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<TaskDetailNam>>(rpcProxy, new LoadGridDataReader()) {
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
    protected void createSubTaskGrid() {
        // Check this station was locked or not.
        String message = StringUtils.EMPTY;
        for (StationLock stationLock : currentStation.getStationLocks()) {
            if (NAM_Q1.getCode() == stationLock.getCode()) {
                q1Lock = true;
                message += "Q1 ";
            } else if (NAM_Q2.getCode() == stationLock.getCode()) {
                q2Lock = true;
                message += ",Q2 ";
            } else if (NAM_Q3.getCode() == stationLock.getCode()) {
                q3Lock = true;
                message += ",Q3 ";
            } else if (NAM_Q4.getCode() == stationLock.getCode()) {
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
        dispatch.execute(new GetServerTimeAction(), new AbstractAsyncCallback<GetServerTimeResult>() {
            @Override
            public void onSuccess(GetServerTimeResult result) {
                view.setCurrentYear(result.getYear());
                view.createSubTaskGrid(createSubTaskListStore());
                view.getSubTaskPagingToolBar().
                        bind((PagingLoader<?>) view.getSubTaskDetailGird().getStore().getLoader());
            }
        });
    }

    @Override
    protected Integer[] getTaskTypeCode() {
        return new Integer[]{NAM.getCode()};
    }
}
