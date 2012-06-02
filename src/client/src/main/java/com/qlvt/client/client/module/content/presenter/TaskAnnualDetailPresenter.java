/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.qlvt.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.client.client.core.dispatch.StandardDispatchAsync;
import com.qlvt.client.client.core.reader.LoadGridDataReader;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.content.place.TaskAnnualDetailPlace;
import com.qlvt.client.client.module.content.view.TaskAnnualDetailView;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.client.client.utils.GridUtils;
import com.qlvt.client.client.utils.LoadingUtils;
import com.qlvt.core.client.action.core.SaveAction;
import com.qlvt.core.client.action.core.SaveResult;
import com.qlvt.core.client.action.station.LoadStationAction;
import com.qlvt.core.client.action.station.LoadStationResult;
import com.qlvt.core.client.action.subtaskannualdetail.LoadSubTaskAnnualAction;
import com.qlvt.core.client.action.subtaskannualdetail.LoadSubTaskAnnualResult;
import com.qlvt.core.client.action.taskdetail.DeleteTaskDetailAction;
import com.qlvt.core.client.action.taskdetail.DeleteTaskDetailResult;
import com.qlvt.core.client.action.time.GetServerTimeAction;
import com.qlvt.core.client.action.time.GetServerTimeResult;
import com.qlvt.core.client.constant.TaskTypeEnum;
import com.qlvt.core.client.criterion.ClientRestrictions;
import com.qlvt.core.client.model.Station;
import com.qlvt.core.client.model.SubTaskAnnualDetail;
import com.qlvt.core.client.model.Task;
import com.qlvt.core.client.model.TaskDetail;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.LoginUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class TaskAnnualDetailPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 1/4/12, 9:14 PM
 */
@Presenter(view = TaskAnnualDetailView.class, place = TaskAnnualDetailPlace.class)
public class TaskAnnualDetailPresenter extends AbstractPresenter<TaskAnnualDetailView> {

    private DispatchAsync dispatch = new StandardDispatchAsync(new DefaultExceptionHandler());

    private Station currentStation;
    private TaskDetail currentTaskDetail;

    private ListStore<BeanModel> taskDtoListStore;
    private Window taskEditWindow;

    @Override
    public void onActivate() {
        view.show();
        if (currentStation != null) {
            if (taskDtoListStore != null) {
                //Reload task dto list.
                taskDtoListStore = GridUtils.getListStoreForCb(Task.class, dispatch,
                        ClientRestrictions.eq("taskTypeCode", TaskTypeEnum.DK.getTaskTypeCode()));
            }
            resetView();
        }
        if (view.getTaskDetailGird() != null) {
            view.getTaskDetailGird().focus();
        }
    }

    @Override
    protected void doBind() {
        LoadingUtils.showLoading();
        taskDtoListStore = GridUtils.getListStoreForCb(Task.class, dispatch,
                ClientRestrictions.eq("taskTypeCode", TaskTypeEnum.DK.getTaskTypeCode()));

        dispatch.execute(new LoadStationAction(LoginUtils.getUserName()), new AbstractAsyncCallback<LoadStationResult>() {
            @Override
            public void onSuccess(LoadStationResult result) {
                super.onSuccess(result);
                currentStation = result.getStation();
                view.createTaskGrid(GridUtils.createListStore(TaskDetail.class, dispatch, ClientRestrictions.eq("station.id",
                        currentStation.getId()), ClientRestrictions.eq("annual", true)));
                view.getTaskPagingToolBar().bind((PagingLoader<?>) view.getTaskDetailGird().getStore().getLoader());
                resetView();
                view.getTaskDetailGird().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
                    @Override
                    public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
                        int index = se.getSelection().size() - 1;
                        if (index >= 0) {
                            TaskDetail taskDetail = se.getSelection().get(index).getBean();
                            if (taskDetail.getId() != null) {
                                currentTaskDetail = taskDetail;
                                view.getSubTaskPagingToolBar().refresh();
                            } else {
                                emptySubGird();
                            }
                        }
                    }
                });
                view.getTaskDetailGird().focus();

                view.createSubTaskGrid(createSubTaskListStore());
                view.getSubTaskPagingToolBar().bind((PagingLoader<?>) view.getSubTaskDetailGird().getStore().getLoader());
            }
        });

        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (currentStation != null) {
                    taskEditWindow = view.createTaskEditWindow(taskDtoListStore);
                    currentTaskDetail = new TaskDetail();
                    taskEditWindow.show();
                } else {
                    DiaLogUtils.notify(view.getConstant().loadStationError());
                }
            }
        });
        view.getBtnEdit().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (view.getTaskDetailGird().getSelectionModel().getSelectedItem() != null) {
                    currentTaskDetail = view.getTaskDetailGird().getSelectionModel().getSelectedItem().getBean();
                    taskEditWindow = view.createTaskEditWindow(taskDtoListStore);
                    BeanModel task = null;
                    for (BeanModel model : view.getTaskGrid().getStore().getModels()) {
                        if (model.<Task>getBean().getId().equals(currentTaskDetail.getTask().getId())) {
                            task = model;
                        }
                    }
                    view.getTxtTaskSearch().setValue(currentTaskDetail.getTask().getCode());
                    view.getTaskGrid().getStore().applyFilters("");
                    view.getTaskGrid().getSelectionModel().select(task, false);
                    taskEditWindow.show();
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
                List<SubTaskAnnualDetail> subTaskAnnualDetails = new ArrayList<SubTaskAnnualDetail>();
                for (Record record : view.getSubTaskDetailGird().getStore().getModifiedRecords()) {
                    subTaskAnnualDetails.add(((BeanModel) record.getModel()).<SubTaskAnnualDetail>getBean());
                }
                dispatch.execute(new SaveAction(subTaskAnnualDetails), new AbstractAsyncCallback<SaveResult>() {
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
                        filters.put(TaskAnnualDetailView.TASK_DETAIL_NAME_COLUMN,
                                view.getTxtSearch().getValue());
                        filters.put(TaskAnnualDetailView.TASK_DETAIL_CODE_COLUMN,
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
        view.getBtnTaskEditOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (currentTaskDetail != null && view.getTaskGrid().getSelectionModel().getSelectedItem() != null) {
                    Task task = view.getTaskGrid().getSelectionModel().getSelectedItem().getBean();
                    currentTaskDetail.setTask(task);
                    currentTaskDetail.setAnnual(true);
                    currentTaskDetail.setStation(currentStation);
                    currentTaskDetail.setCreateBy(1l);
                    currentTaskDetail.setUpdateBy(1l);
                    LoadingUtils.showLoading();
                    dispatch.execute(new GetServerTimeAction(), new AbstractAsyncCallback<GetServerTimeResult>() {
                        @Override
                        public void onSuccess(GetServerTimeResult result) {
                            currentTaskDetail.setYear(result.getYear());
                            dispatch.execute(new SaveAction(currentTaskDetail), new AbstractAsyncCallback<SaveResult>() {
                                @Override
                                public void onSuccess(SaveResult result) {
                                    super.onSuccess(result);
                                    DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                                    taskEditWindow.hide();
                                    updateGrid(result.<TaskDetail>getEntity());
                                }
                            });
                        }
                    });
                }
            }
        });
        view.getBtnTaskEditCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                taskEditWindow.hide();
            }
        });
        view.getBtnSubTaskRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (currentTaskDetail != null) {
                    view.getSubTaskPagingToolBar().refresh();
                }
            }
        });
        view.getTxtTaskSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyUp(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER && view.getTxtTaskSearch().isRendered()) {
                    view.getTaskGrid().getSelectionModel().select(0, false);
                    view.getTaskGrid().focus();
                } else {
                    view.getTaskGrid().getStore().applyFilters("");
                }
            }
        });
    }

    private void updateGrid(TaskDetail taskDetail) {
        boolean isNotFound = true;
        BeanModelFactory factory = BeanModelLookup.get().getFactory(TaskDetail.class);
        BeanModel updateModel = factory.createModel(taskDetail);
        for (BeanModel model : view.getTaskDetailGird().getStore().getModels()) {
            if (taskDetail.getId().equals(model.<TaskDetail>getBean().getId())) {
                int index = view.getTaskDetailGird().getStore().indexOf(model);
                view.getTaskDetailGird().getStore().remove(model);
                view.getTaskDetailGird().getStore().insert(updateModel, index);
                isNotFound = false;
            }
        }
        if (isNotFound) {
            view.getTaskDetailGird().getStore().add(updateModel);
            view.getTaskDetailGird().getView().ensureVisible(view.getTaskDetailGird()
                    .getStore().getCount() - 1, 1, false);
        }
        view.getTaskDetailGird().getSelectionModel().select(updateModel, false);
    }

    private ListStore<BeanModel> createSubTaskListStore() {
        RpcProxy<LoadSubTaskAnnualResult> rpcProxy = new RpcProxy<LoadSubTaskAnnualResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadSubTaskAnnualResult> callback) {
                long currentTaskDetailId = -1;
                if (currentTaskDetail != null) {
                    currentTaskDetailId = currentTaskDetail.getId();
                }
                dispatch.execute(new LoadSubTaskAnnualAction((BasePagingLoadConfig) loadConfig, currentTaskDetailId), callback);
            }
        };

        PagingLoader<PagingLoadResult<SubTaskAnnualDetail>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<SubTaskAnnualDetail>>(rpcProxy, new LoadGridDataReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<BeanModel>(pagingLoader);
    }

    private class DeleteButtonEventListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            final List<BeanModel> models = view.getTaskDetailGird().getSelectionModel().getSelectedItems();
            if (CollectionsUtils.isNotEmpty(models)) {
                if (models.size() == 1) {
                    final TaskDetail taskDetail = models.get(0).getBean();
                    showDeleteTagConform(taskDetail.getId(), taskDetail.getTask().getName());
                } else {
                    List<Long> taskDetailIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        taskDetailIds.add(model.<TaskDetail>getBean().getId());
                    }
                    showDeleteTagConform(taskDetailIds, null);
                }
            }
        }
    }

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
                super.onSuccess(result);
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
                    LoadingUtils.showLoading();
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
