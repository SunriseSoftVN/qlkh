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
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.qlvt.client.client.constant.ExceptionConstant;
import com.qlvt.client.client.core.dispatch.StandardDispatchAsync;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.content.place.TaskManagerPlace;
import com.qlvt.client.client.module.content.view.TaskManagerView;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.client.client.utils.GridUtils;
import com.qlvt.client.client.utils.TaskCodeUtils;
import com.qlvt.core.client.action.core.*;
import com.qlvt.core.client.constant.TaskTypeEnum;
import com.qlvt.core.client.model.Task;
import com.qlvt.core.client.model.TaskDetail;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.shared.ServiceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class TaskManagerPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 12/31/11, 2:48 PM
 */
@Presenter(view = TaskManagerView.class, place = TaskManagerPlace.class)
public class TaskManagerPresenter extends AbstractPresenter<TaskManagerView> {

    private static final String[] RELATE_ENTITY_NAMES = {TaskDetail.class.getName()};

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;

    private Window taskEditWindow;
    private Window addChildTaskWindow;
    private Task currentTask;

    @Override
    public void onActivate() {
        view.show();
        view.getPagingToolBar().refresh();
        view.getTaskGird().focus();
    }

    @Override
    protected void doBind() {
        view.setTaskChildOptionCellRenderer(new TaskChildOptionGridRender());
        view.createGrid(GridUtils.createListStore(Task.class));
        view.getPagingToolBar().bind((PagingLoader<?>) view.getTaskGird().getStore().getLoader());
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                taskEditWindow = view.createTaskEditWindow();
                view.getTaskEditPanel().clear();
                view.getCbbTaskType().setSimpleValue(TaskTypeEnum.DK);
                currentTask = new Task();
                taskEditWindow.show();
            }
        });
        view.getBtnRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                resetFilter();
                view.getPagingToolBar().refresh();
            }
        });
        view.getBtnEdit().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (view.getTaskGird().getSelectionModel().getSelectedItem() != null) {
                    Task selectedTask = view.getTaskGird().getSelectionModel().getSelectedItem().getBean();
                    taskEditWindow = view.createTaskEditWindow();
                    view.getTxtTaskCode().setValue(selectedTask.getCode());
                    view.getTxtTaskName().setValue(selectedTask.getName());
                    view.getTxtTaskUnit().setValue(selectedTask.getUnit());
                    view.getTxtTaskDefault().setValue(selectedTask.getDefaultValue());
                    view.getTxtTaskQuota().setValue(selectedTask.getQuota());
                    view.getCbbTaskType().setSimpleValue(TaskTypeEnum.
                            valueOf(selectedTask.getTaskTypeCode()));
                    currentTask = selectedTask;
                    taskEditWindow.show();
                }
            }
        });
        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
        view.getTxtNameSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                String st = view.getTxtNameSearch().getValue();
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    if (StringUtils.isNotBlank(st)) {
                        view.getTxtCodeSearch().clear();
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getTaskGird().
                                getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put("name", view.getTxtNameSearch().getValue());
                        loadConfig.set("filters", filters);
                    } else {
                        resetFilter();
                    }
                    view.getPagingToolBar().refresh();
                } else if (event.getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    resetFilter();
                    com.google.gwt.user.client.Timer timer = new Timer() {
                        @Override
                        public void run() {
                            view.getPagingToolBar().refresh();
                        }
                    };
                    timer.schedule(100);
                }
            }
        });

        view.getTxtCodeSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    String st = view.getTxtCodeSearch().getValue();
                    view.getTxtNameSearch().clear();
                    if (StringUtils.isNotBlank(st)) {
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getTaskGird().
                                getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put("code", view.getTxtCodeSearch().getValue());
                        loadConfig.set("filters", filters);
                    } else {
                        resetFilter();
                    }
                    view.getPagingToolBar().refresh();
                } else if (event.getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    resetFilter();
                    com.google.gwt.user.client.Timer timer = new Timer() {
                        @Override
                        public void run() {
                            view.getPagingToolBar().refresh();
                        }
                    };
                    timer.schedule(100);
                }
            }
        });

        view.getBtnTaskEditOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (view.getTaskEditPanel().isValid()) {
                    currentTask.setCode(view.getTxtTaskCode().getValue());
                    currentTask.setName(view.getTxtTaskName().getValue());
                    currentTask.setUnit(view.getTxtTaskUnit().getValue());
                    if (view.getTxtTaskDefault().getValue() != null) {
                        currentTask.setDefaultValue(view.getTxtTaskDefault().getValue().doubleValue());
                    }
                    if (view.getTxtTaskQuota().getValue() != null) {
                        currentTask.setQuota(view.getTxtTaskQuota().getValue().intValue());
                    }
                    currentTask.setTaskTypeCode(view.getCbbTaskType().
                            getSimpleValue().getCode());
                    currentTask.setCreateBy(1l);
                    currentTask.setUpdateBy(1l);
                    if (currentTask.getTaskTypeCode() == TaskTypeEnum.KDK.getCode()) {
                        currentTask.setChildTasks(StringUtils.EMPTY);
                    }
                    dispatch.execute(new SaveAction(currentTask), new AbstractAsyncCallback<SaveResult>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            if (caught instanceof ServiceException) {
                                String causeClassName = ((ServiceException) caught).getCauseClassname();
                                if (causeClassName.contains(ExceptionConstant.DATA_EXCEPTION)) {
                                    DiaLogUtils.showMessage(view.getConstant().existCodeMessage());
                                }
                            } else {
                                super.onFailure(caught);
                            }
                        }

                        @Override
                        public void onSuccess(SaveResult result) {
                            taskEditWindow.hide();
                            DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                            updateGrid(result.<Task>getEntity());
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
        view.getBtnAddTaskChildCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                addChildTaskWindow.hide();
            }
        });
        view.getBtnAddTaskChild().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                BeanModel model = view.getCbbChildTask().getValue();
                view.getChildTaskGrid().getStore().add(model);
                view.getCbbChildTask().getStore().remove(model);
                view.getCbbChildTask().reset();
            }
        });
        view.getBtnDeleteTaskChild().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                BeanModel selectedModel = view.getChildTaskGrid().getSelectionModel().getSelectedItem();
                if (selectedModel != null) {
                    view.getChildTaskGrid().getStore().remove(selectedModel);
                    view.getCbbChildTask().getStore().add(selectedModel);
                }
            }
        });
        view.getBtnAddTaskChildOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                String childIds = StringUtils.EMPTY;
                for (BeanModel model : view.getChildTaskGrid().getStore().getModels()) {
                    Task childTask = model.getBean();
                    childIds += childTask.getCode() + TaskCodeUtils.CODE_SEPARATOR;
                }
                final Task selectedTask = view.getTaskGird().getSelectionModel().getSelectedItem().getBean();
                selectedTask.setChildTasks(childIds);
                dispatch.execute(new SaveAction(selectedTask), new AbstractAsyncCallback<SaveResult>() {
                    @Override
                    public void onSuccess(SaveResult result) {
                        if (result.getEntity() != null) {
                            updateGrid(selectedTask);
                            addChildTaskWindow.hide();
                            DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                        }
                    }
                });
            }
        });
    }

    private void updateGrid(Task task) {
        boolean isNotFound = true;
        BeanModelFactory factory = BeanModelLookup.get().getFactory(Task.class);
        BeanModel updateModel = factory.createModel(task);
        for (BeanModel model : view.getTaskGird().getStore().getModels()) {
            if (task.getId().equals(model.<Task>getBean().getId())) {
                int index = view.getTaskGird().getStore().indexOf(model);
                view.getTaskGird().getStore().remove(model);
                view.getTaskGird().getStore().insert(updateModel, index);
                isNotFound = false;
            }
        }
        if (isNotFound) {
            view.getTaskGird().getStore().add(updateModel);
            view.getTaskGird().getView().ensureVisible(view.getTaskGird()
                    .getStore().getCount() - 1, 1, false);
        }
        view.getTaskGird().getSelectionModel().select(updateModel, false);
    }

    private class TaskChildOptionGridRender implements GridCellRenderer<BeanModel> {
        @Override
        public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
            Task task = model.getBean();
            if (task != null && task.getTaskTypeCode() == TaskTypeEnum.SUM.getCode()) {
                return createTaskChildOptionAnchor();
            }
            return null;
        }
    }

    private Anchor createTaskChildOptionAnchor() {
        Anchor anchor = new Anchor(view.getConstant().taskChildOptionAnchor());
        anchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dispatch.execute(new LoadAction(Task.class.getName()), new AbstractAsyncCallback<LoadResult>() {
                    @Override
                    public void onSuccess(LoadResult result) {
                        Task selectedTask = view.getTaskGird().getSelectionModel().getSelectedItem().getBean();
                        List<String> childTaskCodes = new ArrayList<String>();
                        if (selectedTask != null) {
                            childTaskCodes = TaskCodeUtils.getChildTaskCodes(selectedTask.getChildTasks());
                        }
                        BeanModelFactory factory = BeanModelLookup.get().getFactory(Task.class);
                        ListStore<BeanModel> cbbStore = new ListStore<BeanModel>();
                        ListStore<BeanModel> childTaskGridStore = new ListStore<BeanModel>();
                        for (Task task : result.<Task>getList()) {
                            if (!task.getId().equals(selectedTask.getId())) {
                                if (!childTaskCodes.contains(task.getCode())) {
                                    cbbStore.add(factory.createModel(task));
                                } else {
                                    childTaskGridStore.add(factory.createModel(task));
                                }
                            }
                        }
                        view.getCbbChildTask().setStore(cbbStore);
                        addChildTaskWindow = view.createAddTaskChildWindow(childTaskGridStore);
                        addChildTaskWindow.show();
                    }
                });
            }
        });
        return anchor;
    }

    private class DeleteButtonEventListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            final List<BeanModel> models = view.getTaskGird().getSelectionModel().getSelectedItems();
            if (CollectionsUtils.isNotEmpty(models)) {
                if (models.size() == 1) {
                    final Task task = (Task) models.get(0).getBean();
                    showDeleteTagConform(task.getId(), task.getName());
                } else {
                    List<Long> taskIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        final Task task = (Task) model.getBean();
                        taskIds.add(task.getId());
                    }
                    showDeleteTagConform(taskIds, null);
                }
            }
        }
    }

    private void showDeleteTagConform(long tagId, String tagName) {
        List<Long> tagIds = new ArrayList<Long>(1);
        tagIds.add(tagId);
        showDeleteTagConform(tagIds, tagName);
    }

    private void showDeleteTagConform(final List<Long> taskIds, String tagName) {
        assert taskIds != null;
        String deleteMessage;
        final AsyncCallback<DeleteResult> callback = new AbstractAsyncCallback<DeleteResult>() {
            @Override
            public void onSuccess(DeleteResult result) {
                if (result.isResult()) {
                    //Reload grid.
                    view.getPagingToolBar().refresh();
                    DiaLogUtils.notify(view.getConstant().deleteTaskMessageSuccess());
                } else {
                    DiaLogUtils.showMessage(view.getConstant().deleteTaskMessageError());
                }
            }
        };
        final boolean hasManyTag = taskIds.size() > 1;
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
                        dispatch.execute(new DeleteAction(Task.class.getName(), taskIds,
                                RELATE_ENTITY_NAMES), callback);
                    } else {
                        dispatch.execute(new DeleteAction(Task.class.getName(), taskIds.get(0),
                                RELATE_ENTITY_NAMES), callback);
                    }
                }
            }
        });
    }

    private void resetFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getTaskGird().
                getStore().getLoadConfig();
        loadConfig.set("hasFilter", false);
        loadConfig.set("filters", null);
        view.getTxtNameSearch().clear();
        view.getTxtCodeSearch().clear();
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
