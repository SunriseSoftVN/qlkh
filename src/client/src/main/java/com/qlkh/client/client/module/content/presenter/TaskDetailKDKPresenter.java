/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.TaskDetailKDKPlace;
import com.qlkh.client.client.module.content.view.TaskDetailDKView;
import com.qlkh.client.client.module.content.view.TaskDetailKDKView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.action.station.LoadStationAction;
import com.qlkh.core.client.action.station.LoadStationResult;
import com.qlkh.core.client.action.subtask.LoadSubTaskDetailAction;
import com.qlkh.core.client.action.subtask.LoadSubTaskDetailResult;
import com.qlkh.core.client.criterion.ClientRestrictions;
import com.qlkh.core.client.model.Station;
import com.qlkh.core.client.model.StationLock;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.TaskDetailKDK;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.LoginUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.qlkh.core.client.constant.StationLockTypeEnum.*;
import static com.qlkh.core.client.constant.TaskTypeEnum.KDK;

/**
 * The Class TaskDetailPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 1/1/12, 3:40 PM
 */
@Presenter(view = TaskDetailKDKView.class, place = TaskDetailKDKPlace.class)
public class TaskDetailKDKPresenter extends AbstractPresenter<TaskDetailKDKView> {

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;

    private Station currentStation;
    private Task currentTask;

    private boolean q1Lock;
    private boolean q2Lock;
    private boolean q3Lock;
    private boolean q4Lock;

    @Override
    public void onActivate() {
        view.show();
        if (currentStation != null) {
            //Reload view
            resetView();
        }
        if (view.getTaskGrid() != null) {
            view.getTaskGrid().focus();
        }
    }

    @Override
    protected void doBind() {
        dispatch.execute(new LoadStationAction(LoginUtils.getUserName()), new AbstractAsyncCallback<LoadStationResult>() {
            @Override
            public void onSuccess(LoadStationResult result) {
                currentStation = result.getStation();

                view.createTaskGrid(GridUtils.createListStore(Task.class,
                        ClientRestrictions.eq("taskTypeCode", KDK.getCode())));
                view.getTaskPagingToolBar().bind((PagingLoader<?>) view.getTaskGrid().getStore().getLoader());
                resetView();
                view.getTaskGrid().focus();
                view.getTaskGrid().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
                    @Override
                    public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
                        int index = se.getSelection().size() - 1;
                        if (index >= 0) {
                            currentTask = se.getSelection().get(index).getBean();
                            view.getSubTaskPagingToolBar().refresh();
                        }
                    }
                });

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
                    //Disable delete button when company locked something.
                    DiaLogUtils.showMessage(StringUtils.substitute(view.getConstant().lockMessage(), message));
                }

                view.createSubTaskGrid(createSubTaskListStore(), q1Lock, q2Lock, q3Lock, q4Lock);
                view.getSubTaskPagingToolBar().bind((PagingLoader<?>) view.getSubTaskDetailGird().getStore().getLoader());
            }
        });

        view.getBtnRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                resetFilter();
                resetView();
            }
        });
        view.getBtnSubTaskSave().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                List<TaskDetailKDK> taskDetailKDKs = new ArrayList<TaskDetailKDK>();
                for (Record record : view.getSubTaskDetailGird().getStore().getModifiedRecords()) {
                    taskDetailKDKs.add(((BeanModel) record.getModel()).<TaskDetailKDK>getBean());
                }
                dispatch.execute(new SaveAction(taskDetailKDKs), new AbstractAsyncCallback<SaveResult>() {
                    @Override
                    public void onSuccess(SaveResult result) {
                        DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                        view.getSubTaskPagingToolBar().refresh();
                    }
                });
            }
        });
        view.getTxtSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                String st = view.getTxtSearch().getValue();
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    if (StringUtils.isNotBlank(st)) {
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getTaskGrid().
                                getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put(TaskDetailDKView.TASK_NAME_COLUMN, st);
                        filters.put(TaskDetailDKView.TASK_CODE_COLUMN, st);
                        loadConfig.set("filters", filters);
                    } else {
                        resetFilter();
                    }
                    resetView();
                } else if (event.getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    resetFilter();
                    com.google.gwt.user.client.Timer timer = new com.google.gwt.user.client.Timer() {
                        @Override
                        public void run() {
                            resetView();
                        }
                    };
                    timer.schedule(100);
                }
            }
        });

        view.getBtnSubTaskRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (currentTask != null) {
                    view.getSubTaskPagingToolBar().refresh();
                }
            }
        });
    }

    private ListStore<BeanModel> createSubTaskListStore() {
        RpcProxy<LoadSubTaskDetailResult> rpcProxy = new RpcProxy<LoadSubTaskDetailResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadSubTaskDetailResult> callback) {
                long currentTaskId = -1;
                if (currentTask != null) {
                    currentTaskId = currentTask.getId();
                }
                dispatch.execute(new LoadSubTaskDetailAction((BasePagingLoadConfig) loadConfig,
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


    private void emptySubGird() {
        if (view.getSubTaskDetailGird() != null) {
            view.getSubTaskDetailGird().getStore().removeAll();
        }
    }

    private void resetFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getTaskGrid().
                getStore().getLoadConfig();
        loadConfig.set("hasFilter", false);
        loadConfig.set("filters", null);
        view.getTxtSearch().clear();
    }

    private void resetView() {
        view.getTaskPagingToolBar().refresh();
        emptySubGird();
    }

}
