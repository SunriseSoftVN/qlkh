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
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.content.place.TaskDetailPlace;
import com.qlvt.client.client.module.content.view.TaskDetailView;
import com.qlvt.client.client.module.content.view.TaskManagerView;
import com.qlvt.client.client.service.TaskDetailService;
import com.qlvt.client.client.service.TaskDetailServiceAsync;
import com.qlvt.client.client.service.TaskService;
import com.qlvt.client.client.service.TaskServiceAsync;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.client.client.utils.LoadingUtils;
import com.qlvt.core.client.model.Task;
import com.qlvt.core.client.model.TaskDetail;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TaskDetailPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 1/1/12, 3:40 PM
 */
@Presenter(view = TaskDetailView.class, place = TaskDetailPlace.class)
public class TaskDetailPresenter extends AbstractPresenter<TaskDetailView> {

    private TaskDetailServiceAsync taskDetailService = TaskDetailService.App.getInstance();
    private TaskServiceAsync taskService = TaskService.App.getInstance();

    @Override
    public void onActivate() {
        view.show();
        view.getPagingToolBar().refresh();
    }

    @Override
    protected void doBind() {
        view.setTaskCodeCellEditor(createTaskCodeCellEditor());
        view.createGrid(createTaskListStore());
        view.getPagingToolBar().bind((PagingLoader<?>) view.getTaskDetailGird().getStore().getLoader());
        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
    }

    private ListStore<BeanModel> createTaskListStore() {
        RpcProxy<BasePagingLoadResult<List<TaskDetail>>> rpcProxy = new RpcProxy<BasePagingLoadResult<List<TaskDetail>>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<BasePagingLoadResult<List<TaskDetail>>> callback) {
                taskDetailService.getTaskDetailsForGrid((BasePagingLoadConfig) loadConfig, callback);
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
        final ListStore<BeanModel> store = new ListStore<BeanModel>();
        final BeanModelFactory factory = BeanModelLookup.get().getFactory(Task.class);
        LoadingUtils.showLoading();
        taskService.getAllTasks(new AbstractAsyncCallback<List<Task>>() {
            @Override
            public void onSuccess(List<Task> result) {
                super.onSuccess(result);
                store.add(factory.createModel(result));
            }
        });
        final ComboBox<BeanModel> ccbTask = new ComboBox<BeanModel>();
        ccbTask.setStore(store);
        ccbTask.setTriggerAction(ComboBox.TriggerAction.ALL);
        ccbTask.setForceSelection(true);
        ccbTask.setDisplayField(TaskManagerView.TASK_CODE_COLUMN);
        return new CellEditor(ccbTask);
    }

    private class DeleteButtonEventListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            final List<BeanModel> models = view.getTaskDetailGird().getSelectionModel().getSelectedItems();
            if (CollectionsUtils.isNotEmpty(models)) {
                if (models.size() == 1) {
                    final TaskDetail taskDetail = (TaskDetail) models.get(0).getBean();
                    showDeleteTagConform(taskDetail.getId(), taskDetail.getTask().getName());
                } else {
                    List<Long> taskDetailIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        final Task task = (Task) model.getBean();
                        taskDetailIds.add(task.getId());
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
