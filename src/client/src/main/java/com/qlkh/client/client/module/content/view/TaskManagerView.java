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

package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelProcessor;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.TaskManagerConstant;
import com.qlkh.client.client.module.content.view.security.TaskManagerSecurity;
import com.qlkh.client.client.widget.MyFitLayout;
import com.qlkh.client.client.widget.MyNumberField;
import com.qlkh.core.client.constant.TaskTypeEnum;
import com.qlkh.core.client.model.Task;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;
import com.smvp4g.mvp.client.widget.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Class TaskManagerView.
 *
 * @author Nguyen Duc Dung
 * @since 12/31/11, 2:48 PM
 */
@ViewSecurity(configuratorClass = TaskManagerSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = TaskManagerConstant.class)
public class TaskManagerView extends AbstractView<TaskManagerConstant> {

    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 40;
    public static final String TASK_CODE_COLUMN = "code";
    public static final int TASK_CODE_WIDTH = 100;
    public static final String TASK_NAME_COLUMN = "name";
    public static final int TASK_NAME_WIDTH = 300;
    public static final String TASK_UNIT_COLUMN = "unit";
    public static final int TASK_UNIT_WIDTH = 70;
    public static final String TASK_DEFAULT_VALUE_COLUMN = "defaultValue";
    public static final int TASK_DEFAULT_VALUE_WIDTH = 70;
    public static final String TASK_QUOTA_COLUMN = "quota";
    public static final int TASK_QUOTA_WIDTH = 70;
    public static final String TASK_TYPE_CODE_COLUMN = "taskTypeCode";
    public static final int TASK_TYPE_CODE_WIDTH = 70;
    public static final String TASK_CHILD_COLUMN = "childTasks";
    public static final int TASK_CHILD_WIDTH = 170;
    public static final String TASK_CHILD_OPTION_COLUMN = "childTaskOptions";
    public static final int TASK_CHILD_OPTION_WIDTH = 70;

    public static final int TASK_LIST_SIZE = 200;

    @I18nField
    Button btnAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnEdit = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnDelete = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField
    TextField<String> txtTaskCode = new TextField<String>();

    @I18nField
    TextField<String> txtTaskName = new TextField<String>();

    @I18nField
    TextField<String> txtTaskUnit = new TextField<String>();

    @I18nField
    MyNumberField txtTaskDefault = new MyNumberField();

    @I18nField
    MyNumberField txtTaskQuota = new MyNumberField();

    @I18nField
    SimpleComboBox<TaskTypeEnum> cbbTaskType = new SimpleComboBox<TaskTypeEnum>();

    @I18nField
    ComboBox<BeanModel> cbbChildTask = new ComboBox<BeanModel>();

    @I18nField
    Button btnTaskEditOk = new Button();

    @I18nField
    Button btnTaskEditCancel = new Button();

    @I18nField
    Button btnAddTaskChild = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnDeleteTaskChild = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnAddTaskChildOk = new Button();

    @I18nField
    Button btnAddTaskChildCancel = new Button();

    @I18nField(emptyText = true)
    TextField<String> txtNameSearch = new TextField<String>();

    @I18nField(emptyText = true)
    TextField<String> txtCodeSearch = new TextField<String>();

    private ContentPanel contentPanel = new ContentPanel();
    private PagingToolBar pagingToolBar;
    private Grid<BeanModel> taskGird;
    private GridCellRenderer<BeanModel> taskChildOptionCellRenderer;

    private FormPanel taskEditPanel = new FormPanel();
    private VerticalPanel addChildTaskPanel;
    private Grid<BeanModel> childTaskGrid;
    private ColumnModel childTaskColumnModel;

    /**
     * Create Grid on View.
     */
    public void createGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        ColumnModel cm = new ColumnModel(createColumnConfig(selectionModel));
        taskGird = new Grid<BeanModel>(listStore, cm);
        taskGird.setBorders(true);
        taskGird.setLoadMask(true);
        taskGird.setStripeRows(true);
        taskGird.setSelectionModel(selectionModel);
        taskGird.addPlugin(selectionModel);
        taskGird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        taskGird.getStore().getLoader().setSortField(TASK_CODE_COLUMN);
        taskGird.addListener(Events.OnKeyDown, new KeyListener() {
            @Override
            public void handleEvent(ComponentEvent e) {
                if (e.getKeyCode() == 112) {
                    //F1
                    btnAdd.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 113 || e.getKeyCode() == KeyCodes.KEY_ENTER) {
                    //F2 or Enter
                    btnEdit.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 115) {
                    //F4
                    btnRefresh.fireEvent(Events.Select);
                }
            }
        });

        pagingToolBar = new PagingToolBar(TASK_LIST_SIZE);
        ToolBar toolBar = new ToolBar();
        txtCodeSearch.setWidth(100);
        toolBar.add(txtCodeSearch);
        toolBar.add(new SeparatorToolItem());
        txtNameSearch.setWidth(150);
        toolBar.add(txtNameSearch);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnEdit);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnRefresh);

        contentPanel.setLayout(new MyFitLayout());
        contentPanel.add(taskGird);
        contentPanel.setTopComponent(toolBar);
        contentPanel.setBottomComponent(pagingToolBar);
        contentPanel.setHeaderVisible(false);
        contentPanel.setHeight(Window.getClientHeight() - 90);
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                contentPanel.layout(true);
            }
        });
        setWidget(contentPanel);
    }

    private List<ColumnConfig> createColumnConfig(CheckBoxSelectionModel<BeanModel> selectionModel) {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
        columnConfigs.add(selectionModel.getColumn());
        ColumnConfig sttColumnConfig = new ColumnConfig(STT_COLUMN, getConstant().sttColumnTitle(), STT_COLUMN_WIDTH);
        sttColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                                 ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
                if (model.get(STT_COLUMN) == null) {
                    model.set(STT_COLUMN, rowIndex + 1);
                }
                return new Text(String.valueOf(model.get(STT_COLUMN)));
            }
        });
        columnConfigs.add(sttColumnConfig);

        ColumnConfig taskCodeColumnConfig = new ColumnConfig(TASK_CODE_COLUMN, getConstant().taskCodeColumnTitle(), TASK_CODE_WIDTH);
        columnConfigs.add(taskCodeColumnConfig);
        ColumnConfig stationNameColumnConfig = new ColumnConfig(TASK_NAME_COLUMN, getConstant().taskNameColumnTitle(),
                TASK_NAME_WIDTH);
        columnConfigs.add(stationNameColumnConfig);

        ColumnConfig unitColumnConfig = new ColumnConfig(TASK_UNIT_COLUMN, getConstant().taskUnitColumnTitle(),
                TASK_UNIT_WIDTH);
        columnConfigs.add(unitColumnConfig);

        ColumnConfig defaultValueColumnConfig = new ColumnConfig(TASK_DEFAULT_VALUE_COLUMN, getConstant().taskDefaultValueColumnTitle(),
                TASK_DEFAULT_VALUE_WIDTH);
        columnConfigs.add(defaultValueColumnConfig);

        ColumnConfig quotaColumnConfig = new ColumnConfig(TASK_QUOTA_COLUMN, getConstant().taskQuotaColumnTitle(),
                TASK_QUOTA_WIDTH);
        columnConfigs.add(quotaColumnConfig);

        ColumnConfig taskTypeCodeColumnConfig = new ColumnConfig(TASK_TYPE_CODE_COLUMN, getConstant().taskTypeCodeColumnTitle(),
                TASK_TYPE_CODE_WIDTH);
        taskTypeCodeColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config,
                                 int rowIndex, int colIndex, ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
                Task task = model.getBean();
                String taskType = StringUtils.EMPTY;
                if (task != null) {
                    taskType = TaskTypeEnum.valueOf(task.getTaskTypeCode()).name();
                }
                return new Text(taskType);
            }
        });
        columnConfigs.add(taskTypeCodeColumnConfig);

        ColumnConfig taskChildColumnConfig = new ColumnConfig(TASK_CHILD_COLUMN, getConstant().taskChildsColumnTitle(),
                TASK_CHILD_WIDTH);
        columnConfigs.add(taskChildColumnConfig);

        ColumnConfig taskChildOptionColumnConfig = new ColumnConfig(TASK_CHILD_OPTION_COLUMN,
                getConstant().taskChildOptionColumnTitle(), TASK_CHILD_OPTION_WIDTH);
        taskChildOptionColumnConfig.setRenderer(getTaskChildOptionCellRenderer());
        columnConfigs.add(taskChildOptionColumnConfig);

        return columnConfigs;
    }

    private List<ColumnConfig> createChildTaskColumnConfigs() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig sttColumnConfig = new ColumnConfig(STT_COLUMN, getConstant().sttColumnTitle(), STT_COLUMN_WIDTH);
        sttColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                                 ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
                if (model.get(STT_COLUMN) == null) {
                    model.set(STT_COLUMN, rowIndex + 1);
                }
                return new Text(String.valueOf(model.get(STT_COLUMN)));
            }
        });
        columnConfigs.add(sttColumnConfig);

        ColumnConfig taskCodeColumnConfig = new ColumnConfig(TASK_CODE_COLUMN, getConstant().taskCodeColumnTitle(), TASK_CODE_WIDTH);
        columnConfigs.add(taskCodeColumnConfig);
        ColumnConfig stationNameColumnConfig = new ColumnConfig(TASK_NAME_COLUMN, getConstant().taskNameColumnTitle(),
                TASK_NAME_WIDTH);
        columnConfigs.add(stationNameColumnConfig);

        return columnConfigs;
    }

    public com.extjs.gxt.ui.client.widget.Window createTaskEditWindow() {
        com.extjs.gxt.ui.client.widget.Window window = new com.extjs.gxt.ui.client.widget.Window();
        if (!taskEditPanel.isRendered()) {
            taskEditPanel.setHeaderVisible(false);
            taskEditPanel.setBodyBorder(false);
            taskEditPanel.setBorders(false);
            taskEditPanel.setLabelWidth(120);
        }

        if (!txtTaskCode.isRendered()) {
            txtTaskCode.setAllowBlank(false);
            txtTaskCode.setSelectOnFocus(true);
            txtTaskCode.setMaxLength(11);
        }
        taskEditPanel.add(txtTaskCode);
        window.setFocusWidget(txtTaskCode);

        if (!txtTaskName.isRendered()) {
            txtTaskName.setAllowBlank(false);
            txtTaskName.setSelectOnFocus(true);
        }
        taskEditPanel.add(txtTaskName);

        if (!txtTaskUnit.isRendered()) {
            txtTaskUnit.setSelectOnFocus(true);
            txtTaskUnit.setAllowBlank(false);
        }
        taskEditPanel.add(txtTaskUnit);

        if (!txtTaskDefault.isRendered()) {
            txtTaskDefault.setSelectOnFocus(true);
            txtTaskDefault.setAllowBlank(false);
        }
        taskEditPanel.add(txtTaskDefault);

        if (!txtTaskQuota.isRendered()) {
            txtTaskQuota.setSelectOnFocus(true);
            txtTaskQuota.setAllowBlank(false);
            txtTaskQuota.setAllowDecimals(false);
            txtTaskQuota.setAllowNegative(false);
        }
        taskEditPanel.add(txtTaskQuota);
        if (!cbbTaskType.isRendered()) {
            cbbTaskType.setEditable(false);
            cbbTaskType.setAllowBlank(false);
            cbbTaskType.setSelectOnFocus(true);
            cbbTaskType.add(Arrays.asList(TaskTypeEnum.values()));
            cbbTaskType.setTriggerAction(ComboBox.TriggerAction.ALL);
            cbbTaskType.setForceSelection(true);
        }
        taskEditPanel.add(cbbTaskType);

        window.add(taskEditPanel);
        window.addButton(btnTaskEditOk);
        window.addButton(btnTaskEditCancel);
        window.setSize(380, 250);
        window.setResizable(false);
        window.setModal(true);
        window.setHeading(getConstant().taskEditPanel());
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                taskEditPanel.reset();
                taskGird.focus();
            }
        });
        return window;
    }

    public com.extjs.gxt.ui.client.widget.Window createAddTaskChildWindow(ListStore<BeanModel> childTaskGridStore) {
        com.extjs.gxt.ui.client.widget.Window window = new com.extjs.gxt.ui.client.widget.Window();
        addChildTaskPanel = new VerticalPanel();
        addChildTaskPanel.setBorders(false);
        addChildTaskPanel.setSpacing(4);
        addChildTaskPanel.setTableHeight("100%");
        addChildTaskPanel.setTableWidth("100%");
        HorizontalPanel hp = new HorizontalPanel();
        cbbChildTask.setWidth(400);
        cbbChildTask.setForceSelection(true);
        hp.add(cbbChildTask);
        hp.add(new Text());
        hp.add(btnAddTaskChild);
        hp.add(new Text());
        hp.add(btnDeleteTaskChild);
        addChildTaskPanel.add(hp);

        if (!cbbChildTask.isRendered()) {
            cbbChildTask.setForceSelection(true);
            cbbChildTask.setTriggerAction(ComboBox.TriggerAction.ALL);
            cbbChildTask.getView().setModelProcessor(new ModelProcessor<BeanModel>() {
                @Override
                public BeanModel prepareData(BeanModel model) {
                    Task task = model.getBean();
                    if (task != null) {
                        model.set("text", task.getCode() + " " + task.getName());
                        return model;
                    }
                    return null;
                }
            });
        }

        childTaskColumnModel = new ColumnModel(createChildTaskColumnConfigs());
        childTaskGrid = new Grid<BeanModel>(childTaskGridStore, childTaskColumnModel);
        childTaskGrid.setBorders(true);
        childTaskGrid.setHeight(150);
        addChildTaskPanel.add(childTaskGrid);

        window.add(addChildTaskPanel);
        window.addButton(btnAddTaskChildOk);
        window.addButton(btnAddTaskChildCancel);
        window.setSize(540, 250);
        window.setResizable(false);
        window.setModal(true);
        window.setHeading(getConstant().addChildTaskPanel());
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                cbbChildTask.reset();
                childTaskGrid.focus();
            }
        });

        return window;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public Button getBtnEdit() {
        return btnEdit;
    }

    public Button getBtnRefresh() {
        return btnRefresh;
    }

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }

    public Grid<BeanModel> getTaskGird() {
        return taskGird;
    }

    public TextField<String> getTxtNameSearch() {
        return txtNameSearch;
    }

    public FormPanel getTaskEditPanel() {
        return taskEditPanel;
    }

    public Button getBtnTaskEditCancel() {
        return btnTaskEditCancel;
    }

    public Button getBtnTaskEditOk() {
        return btnTaskEditOk;
    }

    public NumberField getTxtTaskDefault() {
        return txtTaskDefault;
    }

    public TextField<String> getTxtTaskUnit() {
        return txtTaskUnit;
    }

    public TextField<String> getTxtTaskName() {
        return txtTaskName;
    }

    public TextField<String> getTxtTaskCode() {
        return txtTaskCode;
    }

    public MyNumberField getTxtTaskQuota() {
        return txtTaskQuota;
    }

    public SimpleComboBox<TaskTypeEnum> getCbbTaskType() {
        return cbbTaskType;
    }

    public GridCellRenderer<BeanModel> getTaskChildOptionCellRenderer() {
        return taskChildOptionCellRenderer;
    }

    public void setTaskChildOptionCellRenderer(GridCellRenderer<BeanModel> taskChildOptionCellRenderer) {
        this.taskChildOptionCellRenderer = taskChildOptionCellRenderer;
    }

    public ComboBox<BeanModel> getCbbChildTask() {
        return cbbChildTask;
    }

    public Button getBtnAddTaskChildOk() {
        return btnAddTaskChildOk;
    }

    public Button getBtnAddTaskChildCancel() {
        return btnAddTaskChildCancel;
    }

    public Button getBtnAddTaskChild() {
        return btnAddTaskChild;
    }

    public Grid<BeanModel> getChildTaskGrid() {
        return childTaskGrid;
    }

    public Button getBtnDeleteTaskChild() {
        return btnDeleteTaskChild;
    }

    public TextField<String> getTxtCodeSearch() {
        return txtCodeSearch;
    }
}
