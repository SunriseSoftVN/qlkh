/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.TaskDetailKDKPlace;
import com.qlkh.client.client.module.content.view.TaskDetailDKView;
import com.qlkh.client.client.module.content.view.TaskDetailKDKView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.action.taskdetail.DeleteTaskDetailAction;
import com.qlkh.core.client.action.taskdetail.DeleteTaskDetailResult;
import com.qlkh.core.client.model.*;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//    private TaskDetail currentTaskDetail;

    private ListStore<BeanModel> taskDtoListStore;
    private Window taskEditWindow;

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
        if (view.getTaskDetailGird() != null) {
            view.getTaskDetailGird().focus();
        }
    }

    @Override
    protected void doBind() {
//        dispatch.execute(new LoadStationAction(LoginUtils.getUserName()), new AbstractAsyncCallback<LoadStationResult>() {
//            @Override
//            public void onSuccess(LoadStationResult result) {
//                currentStation = result.getStation();
//                taskDtoListStore = GridUtils.createListStore(Task.class,
//                        new LoadUnusedTaskGridAction(currentStation.getId(), TaskTypeEnum.KDK));
////                view.createTaskGrid(GridUtils.createListStore(TaskDetail.class, ClientRestrictions.eq("station.id",
////                        currentStation.getId()), ClientRestrictions.eq("annual", false)));
//                view.getTaskPagingToolBar().bind((PagingLoader<?>) view.getTaskGird().getStore().getLoader());
//                resetView();
//                view.getTaskGird().focus();
//                view.getTaskGird().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
//                    @Override
//                    public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
//                        int index = se.getSelection().size() - 1;
//                        if (index >= 0) {
////                            currentTaskDetail = se.getSelection().get(index).getBean();
//                            view.getSubTaskPagingToolBar().refresh();
//                        }
//                    }
//                });
//
//                // Check this station was locked or not.
//                String message = StringUtils.EMPTY;
//                for (StationLock stationLock : currentStation.getStationLocks()) {
//                    if (StationLockTypeEnum.KDK_Q1.getCode() == stationLock.getCode()) {
//                        q1Lock = true;
//                        message += "Q1 ";
//                    } else if (StationLockTypeEnum.KDK_Q2.getCode() == stationLock.getCode()) {
//                        q2Lock = true;
//                        message += ",Q2 ";
//                    } else if (StationLockTypeEnum.KDK_Q3.getCode() == stationLock.getCode()) {
//                        q3Lock = true;
//                        message += ",Q3 ";
//                    } else if (StationLockTypeEnum.KDK_Q4.getCode() == stationLock.getCode()) {
//                        q4Lock = true;
//                        message += ",Q4";
//                    }
//                }
//                if (q1Lock || q2Lock || q3Lock || q4Lock) {
//                    //Disable delete button when company locked something.
//                    view.getBtnDelete().setEnabled(false);
//                    DiaLogUtils.showMessage(StringUtils.substitute(view.getConstant().lockMessage(), message));
//                }
//
//                view.createSubTaskGrid(createSubTaskListStore(), q1Lock, q2Lock, q3Lock, q4Lock);
//                view.getSubTaskPagingToolBar().bind((PagingLoader<?>) view.getSubTaskDetailGird().getStore().getLoader());
//            }
//        });

//        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (currentStation != null) {
                    taskEditWindow = view.createTaskEditWindow(taskDtoListStore);
                    resetTaskGirdFilter();
                    view.getTaskEditPagingToolBar().bind((PagingLoader<?>) view.getTaskGrid().getStore().getLoader());
                    view.getTaskEditPagingToolBar().refresh();
//                    currentTaskDetail = new TaskDetail();
                    taskEditWindow.show();
                } else {
                    DiaLogUtils.notify(view.getConstant().loadStationError());
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
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getTaskDetailGird().
                                getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put(TaskDetailDKView.TASK_DETAIL_NAME_COLUMN, st);
                        filters.put(TaskDetailDKView.TASK_DETAIL_CODE_COLUMN, st);
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
//        view.getBtnTaskEditOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
//            @Override
//            public void componentSelected(ButtonEvent ce) {
//                if (currentTaskDetail != null && view.getTaskGrid().getSelectionModel().getSelectedItem() != null) {
//                    Task task = view.getTaskGrid().getSelectionModel().getSelectedItem().getBean();
//                    currentTaskDetail.setTask(task);
//                    currentTaskDetail.setAnnual(false);
//                    currentTaskDetail.setStation(currentStation);
//                    currentTaskDetail.setCreateBy(1l);
//                    currentTaskDetail.setUpdateBy(1l);
//                    LoadingUtils.showLoading();
//                    dispatch.execute(new GetServerTimeAction(), new AbstractAsyncCallback<GetServerTimeResult>() {
//                        @Override
//                        public void onSuccess(GetServerTimeResult result) {
//                            currentTaskDetail.setYear(result.getYear());
//                            dispatch.execute(new SaveAction(currentTaskDetail), new AbstractAsyncCallback<SaveResult>() {
//                                @Override
//                                public void onSuccess(SaveResult result) {
//                                    DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
//                                    taskEditWindow.hide();
//                                    updateGrid(result.<TaskDetail>getEntity());
//                                }
//                            });
//                        }
//                    });
//                }
//            }
//        });
        view.getBtnTaskEditCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                taskEditWindow.hide();
            }
        });
//        view.getBtnSubTaskRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
//            @Override
//            public void componentSelected(ButtonEvent ce) {
//                if (currentTaskDetail != null) {
//                    view.getSubTaskPagingToolBar().refresh();
//                }
//            }
//        });
        view.getTxtTaskSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyUp(ComponentEvent event) {
                String st = view.getTxtTaskSearch().getValue();
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    if (StringUtils.isNotBlank(st)) {
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getTaskGrid().
                                getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put(TaskDetailKDKView.TASK_NAME_COLUMN, st);
                        filters.put(TaskDetailKDKView.TASK_CODE_COLUMN, st);
                        loadConfig.set("filters", filters);
                    } else {
                        resetTaskGirdFilter();
                    }
                    view.getTaskEditPagingToolBar().refresh();
                }
            }
        });
    }

//    private void updateGrid(TaskDetail taskDetail) {
//        boolean isNotFound = true;
//        BeanModelFactory factory = BeanModelLookup.get().getFactory(TaskDetail.class);
//        BeanModel updateModel = factory.createModel(taskDetail);
//        for (BeanModel model : view.getTaskGird().getStore().getModels()) {
//            if (taskDetail.getId().equals(model.<TaskDetail>getBean().getId())) {
//                int index = view.getTaskGird().getStore().indexOf(model);
//                view.getTaskGird().getStore().remove(model);
//                view.getTaskGird().getStore().insert(updateModel, index);
//                isNotFound = false;
//            }
//        }
//        if (isNotFound) {
//            view.getTaskGird().getStore().add(updateModel);
//            view.getTaskGird().getView().ensureVisible(view.getTaskGird()
//                    .getStore().getCount() - 1, 1, false);
//        }
//        view.getTaskGird().getSelectionModel().select(updateModel, false);
//    }

//    private ListStore<BeanModel> createSubTaskListStore() {
//        RpcProxy<LoadSubTaskDetailResult> rpcProxy = new RpcProxy<LoadSubTaskDetailResult>() {
//            @Override
//            protected void load(Object loadConfig, AsyncCallback<LoadSubTaskDetailResult> callback) {
//                long currentTaskDetailId = -1;
//                if (currentTaskDetail != null) {
//                    currentTaskDetailId = currentTaskDetail.getId();
//                }
//                dispatch.execute(new LoadSubTaskDetailAction((BasePagingLoadConfig) loadConfig, currentTaskDetailId), callback);
//            }
//        };
//
//        PagingLoader<PagingLoadResult<TaskDetailKDK>> pagingLoader =
//                new BasePagingLoader<PagingLoadResult<TaskDetailKDK>>(rpcProxy, new LoadGridDataReader()) {
//                    @Override
//                    protected void onLoadFailure(Object loadConfig, Throwable t) {
//                        super.onLoadFailure(loadConfig, t);
//                        //Log load exception.
//                        DiaLogUtils.logAndShowRpcErrorMessage(t);
//                    }
//                };
//
//        return new ListStore<BeanModel>(pagingLoader);
//    }
//
//    private class DeleteButtonEventListener extends SelectionListener<ButtonEvent> {
//        @Override
//        public void componentSelected(ButtonEvent ce) {
//            final List<BeanModel> models = view.getTaskGird().getSelectionModel().getSelectedItems();
//            if (CollectionsUtils.isNotEmpty(models)) {
//                if (models.size() == 1) {
//                    final TaskDetail taskDetail = models.get(0).getBean();
//                    showDeleteTagConform(taskDetail.getId(), taskDetail.getTask().getName());
//                } else {
//                    List<Long> taskDetailIds = new ArrayList<Long>(models.size());
//                    for (BeanModel model : models) {
//                        taskDetailIds.add(model.<TaskDetail>getBean().getId());
//                    }
//                    showDeleteTagConform(taskDetailIds, null);
//                }
//            }
//        }
//    }

    private void showDeleteTagConform(long tagDetailId, String tagName) {
        List<Long> tagIds = new ArrayList<Long>(1);
        tagIds.add(tagDetailId);
        showDeleteTagConform(tagIds, tagName);
    }

    private void showDeleteTagConform(final List<Long> taskDetailIds, String tagName) {
        assert taskDetailIds != null;
        String deleteMessage;
        final AsyncCallback<DeleteTaskDetailResult> callback = new AbstractAsyncCallback<DeleteTaskDetailResult>() {
            @Override
            public void onSuccess(DeleteTaskDetailResult result) {
                //Reload grid.
                resetView();
                DiaLogUtils.notify(view.getConstant().deleteTaskMessageSuccess());
            }
        };
        final boolean hasManyTag = taskDetailIds.size() > 1;
        if (hasManyTag) {
            deleteMessage = view.getConstant().deleteAllTaskMessage();
        } else {
            deleteMessage = StringUtils.substitute(view.getConstant().deleteTaskMessage(), tagName);
        }

        DiaLogUtils.conform(deleteMessage, new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked().getText().equals("Yes")) {
                    if (hasManyTag) {
                        dispatch.execute(new DeleteTaskDetailAction(taskDetailIds), callback);
                    } else {
                        dispatch.execute(new DeleteTaskDetailAction(taskDetailIds.get(0)), callback);
                    }
                }
            }
        });
    }

    private void emptySubGird() {
        if (view.getSubTaskDetailGird() != null) {
            view.getSubTaskDetailGird().getStore().removeAll();
        }
    }

    private void resetFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getTaskDetailGird().
                getStore().getLoadConfig();
        loadConfig.set("hasFilter", false);
        loadConfig.set("filters", null);
        view.getTxtSearch().clear();
    }

    private void resetTaskGirdFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getTaskGrid().
                getStore().getLoadConfig();
        if (loadConfig != null) {
            loadConfig.set("hasFilter", false);
            loadConfig.set("filters", null);
            view.getTxtTaskSearch().clear();
        }
    }

    private void resetView() {
        view.getTaskPagingToolBar().refresh();
        emptySubGird();
    }

    @Override
    public String mayStop() {
        if (taskEditWindow != null && taskEditWindow.isVisible()) {
            return view.getConstant().conformExitMessage();
        }
        return null;
    }

    @Override
    public void onCancel() {
        if (taskEditWindow != null && taskEditWindow.isVisible()) {
            taskEditWindow.hide();
        }
    }

}
