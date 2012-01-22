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
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.content.place.TaskAnnualDetailPlace;
import com.qlvt.client.client.module.content.view.TaskAnnualDetailView;
import com.qlvt.client.client.service.*;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.client.client.utils.LoadingUtils;
import com.qlvt.core.client.model.Station;
import com.qlvt.core.client.model.SubTaskAnnualDetail;
import com.qlvt.core.client.model.Task;
import com.qlvt.core.client.model.TaskDetail;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.LoginUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Class TaskAnnualDetailPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 1/4/12, 9:14 PM
 */
@Presenter(view = TaskAnnualDetailView.class, place = TaskAnnualDetailPlace.class)
public class TaskAnnualDetailPresenter extends AbstractPresenter<TaskAnnualDetailView> {

    private TaskDetailServiceAsync taskDetailService = TaskDetailService.App.getInstance();
    private TaskServiceAsync taskService = TaskService.App.getInstance();
    private StationServiceAsync stationService = StationService.App.getInstance();
    private BranchServiceAsync branchService = BranchService.App.getInstance();

    private Station currentStation;
    private TaskDetail currentTaskDetailDto;

    private ListStore<BeanModel> taskDtoListStore;

    @Override
    public void onActivate() {
        view.show();
        if (currentStation != null) {
            if (taskDtoListStore != null) {
                //Reload task dto list.
                taskDtoListStore = createTaskDtoListStore();
                ((ComboBox) view.getTaskCodeCellEditor().getField()).setStore(taskDtoListStore);
            }
            view.getTaskPagingToolBar().refresh();
        }
        if (view.getTaskDetailGird() != null) {
            view.getTaskDetailGird().focus();
        }
    }

    @Override
    protected void doBind() {
        LoadingUtils.showLoading();
        taskDtoListStore = createTaskDtoListStore();
        stationService.getStationAndBranchByUserName(LoginUtils.getUserName(), new AbstractAsyncCallback<Station>() {
            @Override
            public void onSuccess(Station result) {
                super.onSuccess(result);
                currentStation = result;
                view.setTaskCodeCellEditor(createTaskCodeCellEditor());
                view.createTaskGrid(createTaskListStore());
                view.getTaskPagingToolBar().bind((PagingLoader<?>) view.getTaskDetailGird().getStore().getLoader());
                view.getTaskPagingToolBar().refresh();
                view.getTaskDetailGird().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
                    @Override
                    public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
                        int index = se.getSelection().size() - 1;
                        if (index >= 0) {
                            currentTaskDetailDto = se.getSelection().get(index).getBean();
                            view.getSubTaskPagingToolBar().refresh();
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
                    TaskDetail taskDetail = new TaskDetail();
                    taskDetail.setStation(currentStation);
                    taskDetail.setYear(1900 + new Date().getYear());
                    taskDetail.setAnnual(true);
                    taskDetail.setCreateBy(1l);
                    taskDetail.setUpdateBy(1l);
                    taskDetail.setCreatedDate(new Date());
                    taskDetail.setUpdatedDate(new Date());
                    BeanModel model = BeanModelLookup.get().getFactory(TaskDetail.class).createModel(taskDetail);
                    view.getTaskDetailGird().getStore().insert(model, view.getTaskDetailGird().getStore().getCount());
                    view.getTaskDetailGird().getView().ensureVisible(view.getTaskDetailGird()
                            .getStore().getCount() - 1, 0, true);
                    view.getTaskDetailGird().startEditing(view.getTaskDetailGird().getStore()
                            .getCount() - 1, 2);
                } else {
                    DiaLogUtils.notify(view.getConstant().loadStationError());
                }
            }
        });
        view.getBtnSave().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                List<TaskDetail> taskDetails = new ArrayList<TaskDetail>();
                for (Record record : view.getTaskDetailGird().getStore().getModifiedRecords()) {
                    taskDetails.add(((BeanModel)record.getModel()).<TaskDetail>getBean());
                }
                if (CollectionsUtils.isNotEmpty(taskDetails)) {
                    LoadingUtils.showLoading();
                    taskDetailService.updateTaskDetails(taskDetails, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            super.onSuccess(result);
                            DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                            view.getTaskPagingToolBar().refresh();
                        }
                    });
                }
            }
        });
        view.getBtnCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                view.getTaskPagingToolBar().refresh();
            }
        });
        view.getBtnSubTaskSave().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                List<SubTaskAnnualDetail> subTaskAnnualDetails = new ArrayList<SubTaskAnnualDetail>();
                for (Record record : view.getSubTaskDetailGird().getStore().getModifiedRecords()) {
                    subTaskAnnualDetails.add(((BeanModel) record.getModel()).<SubTaskAnnualDetail>getBean());
                }
                taskDetailService.updateSubTaskAnnualDetails(subTaskAnnualDetails, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                        view.getSubTaskPagingToolBar().refresh();
                    }
                });
            }
        });
    }

    private ListStore<BeanModel> createSubTaskListStore() {
        RpcProxy<BasePagingLoadResult<SubTaskAnnualDetail>> rpcProxy = new RpcProxy<BasePagingLoadResult<SubTaskAnnualDetail>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<BasePagingLoadResult<SubTaskAnnualDetail>> callback) {
                taskDetailService.getSubTaskAnnualDetails((BasePagingLoadConfig) loadConfig, currentTaskDetailDto.getId(), callback);
            }
        };

        PagingLoader<PagingLoadResult<SubTaskAnnualDetail>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<SubTaskAnnualDetail>>(rpcProxy, new BeanModelReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<BeanModel>(pagingLoader);
    }

    private ListStore<BeanModel> createTaskListStore() {
        RpcProxy<BasePagingLoadResult<TaskDetail>> rpcProxy = new RpcProxy<BasePagingLoadResult<TaskDetail>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<BasePagingLoadResult<TaskDetail>> callback) {
                taskDetailService.getTaskAnnualDetailsForGrid((BasePagingLoadConfig) loadConfig, currentStation.getId(), callback);
            }
        };

        PagingLoader<PagingLoadResult<TaskDetail>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<TaskDetail>>(rpcProxy, new BeanModelReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<BeanModel>(pagingLoader);
    }

    private CellEditor createTaskCodeCellEditor() {
        ComboBox<BeanModel> ccbTask = new ComboBox<BeanModel>();
        ccbTask.setStore(taskDtoListStore);
        ccbTask.setLazyRender(false);
        ccbTask.setTriggerAction(ComboBox.TriggerAction.ALL);
        ccbTask.setForceSelection(true);
        ccbTask.getView().setModelProcessor(new ModelProcessor<BeanModel>() {
            @Override
            public BeanModel prepareData(BeanModel model) {
                Task task = model.getBean();
                model.set("text", task.getCode() +
                        " - " + task.getName());
                return model;
            }
        });
        ccbTask.setSelectOnFocus(true);
        return new CellEditor(ccbTask);
    }

    private ListStore<BeanModel> createTaskDtoListStore() {
        final ListStore<BeanModel> listStore = new ListStore<BeanModel>();
        final BeanModelFactory factory = BeanModelLookup.get().getFactory(Task.class);
        LoadingUtils.showLoading();
        taskService.getAllTasks(new AbstractAsyncCallback<List<Task>>() {
            @Override
            public void onSuccess(List<Task> result) {
                super.onSuccess(result);
                for (Task task : result) {
                    listStore.add(factory.createModel(task));
                }
            }
        });
        return listStore;
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
        final AsyncCallback<Void> callback = new AbstractAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                super.onSuccess(result);
                //Reload grid.
                view.getTaskPagingToolBar().refresh();
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
                        taskDetailService.deleteTaskDetails(taskDetailIds, callback);
                    } else {
                        taskDetailService.deleteTaskDetail(taskDetailIds.get(0), callback);
                    }
                }
            }
        });
    }
}
