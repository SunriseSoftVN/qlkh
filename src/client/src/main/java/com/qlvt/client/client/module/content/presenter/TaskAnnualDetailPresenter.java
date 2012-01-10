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
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.content.place.TaskAnnualDetailPlace;
import com.qlvt.client.client.module.content.view.TaskAnnualDetailView;
import com.qlvt.client.client.module.content.view.TaskManagerView;
import com.qlvt.client.client.service.*;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.client.client.utils.LoadingUtils;
import com.qlvt.core.client.dto.SubTaskAnnualDetailDto;
import com.qlvt.core.client.dto.TaskDetailDto;
import com.qlvt.core.client.dto.TaskDto;
import com.qlvt.core.client.model.Branch;
import com.qlvt.core.client.model.Station;
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
    private List<String> branchNames = new ArrayList<String>();

    private ListStore<TaskDto> taskDtoListStore;

    @Override
    public void onActivate() {
        view.show();
        if (currentStation != null) {
            if (taskDtoListStore != null) {
                //Reload task dto list.
                taskDtoListStore = createTaskDtoListStore();
                ((ComboBox) view.getTaskCodeCellEditor().getField()).setStore(taskDtoListStore);
            }
            view.getPagingToolBar().refresh();
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
                for (Branch branch : currentStation.getBranches()) {
                    branchNames.add(branch.getName());
                }
                view.createGrid(createTaskListStore(), branchNames);
                view.getPagingToolBar().bind((PagingLoader<?>) view.getTaskDetailGird().getStore().getLoader());
                view.getPagingToolBar().refresh();
            }
        });
        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (currentStation != null) {
                    TaskDetailDto taskDetail = new TaskDetailDto();
                    taskDetail.setStation(currentStation);
                    taskDetail.setYear(1900 + new Date().getYear());
                    taskDetail.setAnnual(true);
                    taskDetail.setCreateBy(1l);
                    taskDetail.setUpdateBy(1l);
                    taskDetail.setCreatedDate(new Date());
                    taskDetail.setUpdatedDate(new Date());
                    for (Branch branch : currentStation.getBranches()) {
                        int year = 1900 + new Date().getYear();
                        SubTaskAnnualDetailDto subTaskAnnualDetailDto = new SubTaskAnnualDetailDto();
                        subTaskAnnualDetailDto.setTaskDetail(taskDetail);
                        subTaskAnnualDetailDto.setBranch(branch);
                        subTaskAnnualDetailDto.setCurrentYear(year);
                        subTaskAnnualDetailDto.setLastYear(year - 1);
                        subTaskAnnualDetailDto.setCreateBy(1l);
                        subTaskAnnualDetailDto.setUpdateBy(1l);
                        subTaskAnnualDetailDto.setCreatedDate(new Date());
                        subTaskAnnualDetailDto.setUpdatedDate(new Date());
                        taskDetail.set(branch.getName(), subTaskAnnualDetailDto);
                    }
                    view.getTaskDetailGird().getStore().insert(taskDetail,
                            view.getTaskDetailGird().getStore().getCount());
                } else {
                    DiaLogUtils.notify(view.getConstant().loadStationError());
                }
            }
        });
        view.getBtnSave().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                List<TaskDetailDto> taskDetails = new ArrayList<TaskDetailDto>();
                for (Record record : view.getTaskDetailGird().getStore().getModifiedRecords()) {
                    taskDetails.add((TaskDetailDto) record.getModel());
                }
                if (CollectionsUtils.isNotEmpty(taskDetails)) {
                    LoadingUtils.showLoading();
                    taskDetailService.updateTaskDetailDtos(taskDetails, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            super.onSuccess(result);
                            DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                            view.getPagingToolBar().refresh();
                        }
                    });
                }
            }
        });
        view.getBtnCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                view.getPagingToolBar().refresh();
            }
        });
    }

    private ListStore<TaskDetailDto> createTaskListStore() {
        RpcProxy<BasePagingLoadResult<TaskDetailDto>> rpcProxy = new RpcProxy<BasePagingLoadResult<TaskDetailDto>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<BasePagingLoadResult<TaskDetailDto>> callback) {
                taskDetailService.getTaskAnnualDetailsForGrid((BasePagingLoadConfig) loadConfig, currentStation.getId(), callback);
            }
        };

        PagingLoader<PagingLoadResult<TaskDetailDto>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<TaskDetailDto>>(rpcProxy, new ModelReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<TaskDetailDto>(pagingLoader);
    }

    private CellEditor createTaskCodeCellEditor() {
        ComboBox<TaskDto> ccbTask = new ComboBox<TaskDto>();
        ccbTask.setStore(taskDtoListStore);
        ccbTask.setLazyRender(false);
        ccbTask.setTriggerAction(ComboBox.TriggerAction.ALL);
        ccbTask.setForceSelection(true);
        ccbTask.setDisplayField(TaskManagerView.TASK_CODE_COLUMN);
        return new CellEditor(ccbTask);
    }

    private ListStore<TaskDto> createTaskDtoListStore() {
        final ListStore<TaskDto> taskDtos = new ListStore<TaskDto>();
        LoadingUtils.showLoading();
        taskService.getAllTaskDtos(new AbstractAsyncCallback<List<TaskDto>>() {
            @Override
            public void onSuccess(List<TaskDto> result) {
                super.onSuccess(result);
                taskDtos.add(result);
            }
        });
        return taskDtos;
    }

    private class DeleteButtonEventListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            final List<TaskDetailDto> models = view.getTaskDetailGird().getSelectionModel().getSelectedItems();
            if (CollectionsUtils.isNotEmpty(models)) {
                if (models.size() == 1) {
                    final TaskDetailDto taskDetail = models.get(0);
                    showDeleteTagConform(taskDetail.getId(), taskDetail.getTask().getName());
                } else {
                    List<Long> taskDetailIds = new ArrayList<Long>(models.size());
                    for (TaskDetailDto taskDetail : models) {
                        taskDetailIds.add(taskDetail.getId());
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
                view.getPagingToolBar().refresh();
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
