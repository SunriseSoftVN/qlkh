/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.qlkh.client.client.constant.ExceptionConstant;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.TaskManagerPlace;
import com.qlkh.client.client.module.content.view.TaskManagerView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.client.client.utils.NumberUtils;
import com.qlkh.client.client.utils.TaskCodeUtils;
import com.qlkh.core.client.action.core.LoadAction;
import com.qlkh.core.client.action.core.LoadResult;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.action.task.*;
import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.core.client.constant.TaskTypeEnum;
import com.qlkh.core.client.model.*;
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

import static com.qlkh.core.client.constant.TaskTypeEnum.*;

/**
 * The Class TaskManagerPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 12/31/11, 2:48 PM
 */
@Presenter(view = TaskManagerView.class, place = TaskManagerPlace.class)
public class TaskManagerPresenter extends AbstractPresenter<TaskManagerView> {

    private static final String[] RELATE_ENTITY_NAMES = {TaskDetailDK.class.getName(),
            TaskDetailKDK.class.getName(), TaskDetailNam.class.getName()};

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;

    private Window taskEditWindow;
    private Window addChildTaskWindow;
    private Window pickChildTaskWindow;
    private Window defaultValueWindow;
    private Task currentTask;
    private TaskQuota currentTaskQuota;

    @Override
    public void onActivate() {
        view.show();
        view.getPagingToolBar().refresh();
        view.getTaskGird().focus();
    }

    @Override
    protected void doBind() {
        view.setQuotaRenderer(new TaskQuotaGridRender());
        view.setTaskChildRenderer(new TaskChildOptionGridRender());
        view.createGrid(GridUtils.createListStore(Task.class));
        view.getPagingToolBar().bind((PagingLoader<?>) view.getTaskGird().getStore().getLoader());
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                taskEditWindow = view.createTaskEditWindow();
                view.getTaskEditPanel().clear();
                view.getCbbTaskType().setSimpleValue(DK);
                view.getCbbTaskType().setEnabled(true);
                currentTask = new Task();
                dispatch.execute(new GetServerTimeAction(), new AbstractAsyncCallback<GetServerTimeResult>() {
                    @Override
                    public void onSuccess(GetServerTimeResult result) {
                        loadTaskQuota(currentTask, result.getYear());
                        taskEditWindow.show();
                        view.getTxtTaskQuota().setVisible(false);
                        taskEditWindow.layout(true);
                    }
                });
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
                    view.getCbbTaskType().setSimpleValue(TaskTypeEnum.
                            valueOf(selectedTask.getTaskTypeCode()));
                    currentTask = selectedTask;
                    dispatch.execute(new GetServerTimeAction(), new AbstractAsyncCallback<GetServerTimeResult>() {
                        @Override
                        public void onSuccess(GetServerTimeResult result) {
                            loadTaskQuota(currentTask, result.getYear());
                        }
                    });
                    dispatch.execute(new CanEditAction(currentTask.getId(), RELATE_ENTITY_NAMES),
                            new AbstractAsyncCallback<CanEditResult>() {
                                @Override
                                public void onSuccess(CanEditResult result) {
                                    view.getCbbTaskType().setEnabled(result.isEditable());
                                    view.getWarningMessage().setVisible(!result.isEditable());
                                    taskEditWindow.show();
                                    if (currentTask.getTaskTypeCode() == DK.getCode()) {
                                        view.getTxtTaskQuota().setVisible(false);
                                    } else {
                                        view.getTxtTaskQuota().setValue(currentTask.getQuota());
                                        view.getTxtYear().setVisible(false);
                                        view.getTxtQuotaQ1().setVisible(false);
                                        view.getTxtQuotaQ2().setVisible(false);
                                        view.getTxtQuotaQ3().setVisible(false);
                                        view.getTxtQuotaQ4().setVisible(false);
                                    }
                                    taskEditWindow.layout(true);
                                }
                            });
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
                    currentTask.setDefaultValue(view.getTxtTaskDefault().getValue().doubleValue());
                    if (view.getTxtTaskQuota().getValue() != null) {
                        currentTask.setQuota(view.getTxtTaskQuota().getValue().intValue());
                    }
                    currentTask.setTaskTypeCode(view.getCbbTaskType().
                            getSimpleValue().getCode());
                    currentTask.setCreateBy(1l);
                    currentTask.setUpdateBy(1l);
                    if (currentTask.getTaskTypeCode() == KDK.getCode()
                            || currentTask.getTaskTypeCode() == DK.getCode()
                            || currentTask.getTaskTypeCode() == NAM.getCode()) {
                        currentTask.setChildTasks(StringUtils.EMPTY);
                    }
                    if (currentTask.getTaskTypeCode() == SUBSUM.getCode()) {
                        if (!NumberUtils.isNumber(currentTask.getCode())
                                || currentTask.getCode().length() < 5) {
                            DiaLogUtils.showMessage(view.getConstant().codeIsNotNumberOrTooShort());
                            return;
                        }
                    }

                    List entities = new ArrayList();
                    entities.add(currentTask);
                    if (currentTask.getTaskTypeCode() == DK.getCode()) {
                        currentTaskQuota.setYear(view.getTxtYear().getValue().intValue());
                        currentTaskQuota.setQ1(view.getTxtQuotaQ1().getValue().intValue());
                        currentTaskQuota.setQ2(view.getTxtQuotaQ2().getValue().intValue());
                        currentTaskQuota.setQ3(view.getTxtQuotaQ3().getValue().intValue());
                        currentTaskQuota.setQ4(view.getTxtQuotaQ4().getValue().intValue());
                        entities.add(currentTaskQuota);
                    }

                    dispatch.execute(new SaveAction(entities), new AbstractAsyncCallback<SaveResult>() {
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
                            updateGrid((Task) result.getEntities().get(0));
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
        view.getBtnPickTaskChildCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                pickChildTaskWindow.hide();
            }
        });
        view.getBtnPickTaskChildOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                String toCode = view.getTxtToCode().getValue();
                String fromCode = view.getTxtFormCode().getValue();
                if (StringUtils.isNotBlank(toCode)) {
                    if (!NumberUtils.isNumber(toCode) || toCode.length() < 5) {
                        DiaLogUtils.showMessage(view.getConstant().codeIsNotNumberOrTooShort());
                    } else {
                        final Task selectedTask = view.getTaskGird().getSelectionModel().getSelectedItem().getBean();
                        selectedTask.setChildTasks(fromCode + TaskCodeUtils.CODE_JOIN + toCode);
                        dispatch.execute(new SaveAction(selectedTask), new AbstractAsyncCallback<SaveResult>() {
                            @Override
                            public void onSuccess(SaveResult result) {
                                if (result.getEntity() != null) {
                                    updateGrid(selectedTask);
                                    pickChildTaskWindow.hide();
                                    DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                                }
                            }
                        });
                    }
                }
            }
        });

        view.getCbbTaskType().addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<TaskTypeEnum>>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<SimpleComboValue<TaskTypeEnum>> se) {
                if (se.getSelectedItem() != null) {
                    if (se.getSelectedItem().getValue() != DK) {
                        view.getTxtQuotaQ1().clear();
                        view.getTxtQuotaQ2().clear();
                        view.getTxtQuotaQ3().clear();
                        view.getTxtQuotaQ4().clear();
                        view.getTxtTaskQuota().setVisible(true);
                        view.getTxtYear().setVisible(false);
                        view.getTxtQuotaQ1().setVisible(false);
                        view.getTxtQuotaQ2().setVisible(false);
                        view.getTxtQuotaQ3().setVisible(false);
                        view.getTxtQuotaQ4().setVisible(false);
                    } else {
                        view.getTxtTaskQuota().setVisible(false);
                        view.getTxtYear().setVisible(true);
                        view.getTxtQuotaQ1().setVisible(true);
                        view.getTxtQuotaQ2().setVisible(true);
                        view.getTxtQuotaQ3().setVisible(true);
                        view.getTxtQuotaQ4().setVisible(true);
                    }
                    taskEditWindow.layout(true);
                }
            }
        });
        view.getBtnDefaultValueCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                defaultValueWindow.hide();
            }
        });
        view.getBtnDefaultValueOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                Task selectedTask = view.getTaskGird().getSelectionModel().getSelectedItem().getBean();
                if (selectedTask != null) {
                    TaskDefaultValue taskDefaultValue = new TaskDefaultValue();
                    taskDefaultValue.setTask(selectedTask);
                    taskDefaultValue.setCreateBy(1l);
                    taskDefaultValue.setUpdateBy(1l);
                    taskDefaultValue.setDefaultValue(view.getTxtDefaultValue().
                            getValue().doubleValue());
                    selectedTask.setDefaultValue(taskDefaultValue.getDefaultValue());

                    List entities = new ArrayList();
                    entities.add(taskDefaultValue);
                    entities.add(selectedTask);

                    dispatch.execute(new SaveAction(entities), new AbstractAsyncCallback<SaveResult>() {
                        @Override
                        public void onSuccess(SaveResult result) {
                            if (CollectionsUtils.isNotEmpty(result.getEntities())) {
                                DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                                defaultValueWindow.hide();
                                updateGrid((Task) result.getEntities().get(1));
                            }
                        }
                    });
                }
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
                return createAddTaskChildAnchor();
            } else if (task != null && task.getTaskTypeCode() == TaskTypeEnum.SUBSUM.getCode()) {
                return createPickTaskRangeAnchor();
            }
            return createChangeDefaultValueAnchor();
        }
    }

    private class TaskQuotaGridRender implements GridCellRenderer<BeanModel> {
        @Override
        public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                             ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
            Task task = model.getBean();
            if (task != null) {
                if (task.getTaskTypeCode() == DK.getCode()) {
                    return new Label("Theo quý");
                } else {
                    return new Label(String.valueOf(task.getQuota()));
                }
            }
            return null;
        }
    }

    private void loadTaskQuota(final Task selectedTask, final Integer year) {
        if (selectedTask.getId() == null) {
            createTaskQuota(selectedTask, year);
        } else {
            dispatch.execute(new LoadTaskQuotaAction(selectedTask.getId()), new AbstractAsyncCallback<LoadTaskQuotaResult>() {
                @Override
                public void onSuccess(LoadTaskQuotaResult result) {
                    if (result.getTaskQuota() == null) {
                        createTaskQuota(selectedTask, year);
                    } else {
                        currentTaskQuota = result.getTaskQuota();
                        view.getTxtYear().setValue(currentTaskQuota.getYear());
                        view.getTxtQuotaQ1().setValue(currentTaskQuota.getQ1());
                        view.getTxtQuotaQ2().setValue(currentTaskQuota.getQ2());
                        view.getTxtQuotaQ3().setValue(currentTaskQuota.getQ3());
                        view.getTxtQuotaQ4().setValue(currentTaskQuota.getQ4());
                        if (currentTaskQuota.getYear() != year) {
                            currentTaskQuota = new TaskQuota();
                            currentTaskQuota.setCreateBy(1l);
                            currentTaskQuota.setUpdateBy(1l);
                            currentTaskQuota.setTask(selectedTask);
                            view.getTxtYear().setValue(year);
                        }
                    }
                }
            });
        }
    }

    private void createTaskQuota(final Task selectedTask, final Integer year) {
        currentTaskQuota = new TaskQuota();
        currentTaskQuota.setCreateBy(1l);
        currentTaskQuota.setUpdateBy(1l);
        currentTaskQuota.setTask(selectedTask);
        view.getTxtYear().setValue(year);
        view.getTxtQuotaQ1().clear();
        view.getTxtQuotaQ2().clear();
        view.getTxtQuotaQ3().clear();
        view.getTxtQuotaQ4().clear();
    }

    private Anchor createChangeDefaultValueAnchor() {
        Anchor anchor = new Anchor(view.getConstant().taskChildOptionAnchor());
        anchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                final Task selectedTask = view.getTaskGird().getSelectionModel().getSelectedItem().getBean();
                if (selectedTask != null) {
                    dispatch.execute(new LoadTaskDefaultAction(selectedTask.getId()),
                            new AbstractAsyncCallback<LoadTaskDefaultResult>() {
                                @Override
                                public void onSuccess(LoadTaskDefaultResult result) {
                                    if (result.getTaskDefaultValue() != null) {
                                        view.getTxtDefaultValue().setValue(result.getTaskDefaultValue().getDefaultValue());
                                    }
                                    defaultValueWindow = view.createChangeTaskDefaultWindow();
                                    defaultValueWindow.show();
                                }
                            });
                }
            }
        });
        return anchor;
    }

    private Anchor createPickTaskRangeAnchor() {
        Anchor anchor = new Anchor(view.getConstant().taskChildOptionAnchor());
        anchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Task selectedTask = view.getTaskGird().getSelectionModel().getSelectedItem().getBean();
                if (selectedTask != null) {
                    if (StringUtils.isNotBlank(selectedTask.getChildTasks())) {
                        view.getTxtFormCode().setValue(TaskCodeUtils.
                                extractFormCode(selectedTask.getChildTasks()));
                        view.getTxtToCode().setValue(TaskCodeUtils.
                                extractToCode(selectedTask.getChildTasks()));
                    } else {
                        view.getTxtFormCode().setValue(TaskCodeUtils.
                                getFromCode(selectedTask.getCode()));
                        view.getTxtToCode().setValue(TaskCodeUtils.
                                getToCode(selectedTask.getCode()));
                    }
                    pickChildTaskWindow = view.createPickTaskRangeWindow();
                    pickChildTaskWindow.show();
                }
            }
        });
        return anchor;
    }

    private Anchor createAddTaskChildAnchor() {
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
        final AsyncCallback<DeleteTaskResult> callback = new AbstractAsyncCallback<DeleteTaskResult>() {
            @Override
            public void onSuccess(DeleteTaskResult result) {
                if (result.isDeleted()) {
                    //Reload grid.
                    view.getPagingToolBar().refresh();
                    DiaLogUtils.notify(view.getConstant().deleteTaskMessageSuccess());
                } else {
                    DiaLogUtils.conform(view.getConstant().deleteTaskMessageError(), new Listener<MessageBoxEvent>() {
                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                            if (be.getButtonClicked().getText().equals("Yes")) {
                                dispatch.execute(new DeleteTaskAction(taskIds, true), new AbstractAsyncCallback<DeleteTaskResult>() {
                                    @Override
                                    public void onSuccess(DeleteTaskResult result) {
                                        view.getPagingToolBar().refresh();
                                        DiaLogUtils.notify(view.getConstant().deleteTaskMessageSuccess());
                                    }
                                });
                            }
                        }
                    });
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
                    dispatch.execute(new DeleteTaskAction(taskIds), callback);
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
