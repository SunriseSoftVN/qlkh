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
import com.qlkh.client.client.module.content.place.TaskDetailDKPlace;
import com.qlkh.client.client.module.content.view.TaskDetailDKView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.action.station.LoadStationAction;
import com.qlkh.core.client.action.station.LoadStationResult;
import com.qlkh.core.client.action.subtask.LoadSubTaskAnnualAction;
import com.qlkh.core.client.action.subtask.LoadSubTaskAnnualResult;
import com.qlkh.core.client.constant.StationLockTypeEnum;
import com.qlkh.core.client.criterion.ClientRestrictions;
import com.qlkh.core.client.model.Station;
import com.qlkh.core.client.model.StationLock;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.TaskDetailDK;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.LoginUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.qlkh.core.client.constant.TaskTypeEnum.DK;

/**
 * The Class TaskAnnualDetailPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 1/4/12, 9:14 PM
 */
@Presenter(view = TaskDetailDKView.class, place = TaskDetailDKPlace.class)
public class TaskDetailDKPresenter extends AbstractPresenter<TaskDetailDKView> {

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;

    private Station currentStation;
    private Task currentTask;


    @Override
    public void onActivate() {
        view.show();
        if (currentStation != null) {
            //Reload view
            resetView();
        }
        if (view.getTaskGird() != null) {
            view.getTaskGird().focus();
        }
    }

    @Override
    protected void doBind() {
        dispatch.execute(new LoadStationAction(LoginUtils.getUserName()), new AbstractAsyncCallback<LoadStationResult>() {
            @Override
            public void onSuccess(LoadStationResult result) {
                currentStation = result.getStation();
                view.createTaskGrid(GridUtils.createListStore(Task.class, ClientRestrictions.eq("taskTypeCode", DK.getCode())));
                view.getTaskPagingToolBar().bind((PagingLoader<?>) view.getTaskGird().getStore().getLoader());
                resetView();
                view.getTaskGird().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
                    @Override
                    public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
                        int index = se.getSelection().size() - 1;
                        if (index >= 0) {
                            Task task = se.getSelection().get(index).getBean();
                            if (task.getId() != null) {
                                currentTask = task;
                                view.getSubTaskPagingToolBar().refresh();
                            } else {
                                emptySubGird();
                            }
                        }
                    }
                });
                view.getTaskGird().focus();
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
        view.getTxtSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                String st = view.getTxtSearch().getValue();
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    if (StringUtils.isNotBlank(st)) {
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getTaskGird().
                                getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put(TaskDetailDKView.TASK_DETAIL_NAME_COLUMN,
                                view.getTxtSearch().getValue());
                        filters.put(TaskDetailDKView.TASK_DETAIL_CODE_COLUMN,
                                view.getTxtSearch().getValue());
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


    private void emptySubGird() {
        if (view.getSubTaskDetailGird() != null) {
            view.getSubTaskDetailGird().getStore().removeAll();
        }
    }

    private void resetFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getTaskGird().
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
