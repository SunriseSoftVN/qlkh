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

package com.qlvt.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Window;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.view.i18n.TaskAnnualDetailConstant;
import com.qlvt.client.client.module.content.view.security.TaskAnnualDetailSecurity;
import com.qlvt.client.client.widget.MyNumberField;
import com.qlvt.core.client.model.SubTaskAnnualDetail;
import com.qlvt.core.client.model.Task;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;
import com.smvp4g.mvp.client.widget.TextField;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Class TaskAnnualDetailView.
 *
 * @author Nguyen Duc Dung
 * @since 1/4/12, 9:12 PM
 */
@ViewSecurity(configuratorClass = TaskAnnualDetailSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = TaskAnnualDetailConstant.class)
public class TaskAnnualDetailView extends AbstractView<TaskAnnualDetailConstant> {


    public static final String ID_COLUMN = "id";
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 40;
    public static final String TASK_DETAIL_CODE_COLUMN = "task.code";
    public static final int TASK_DETAIL_CODE_WIDTH = 60;
    public static final String TASK_DETAIL_NAME_COLUMN = "task.name";
    public static final int TASK_DETAIL_NAME_WIDTH = 300;
    public static final String TASK_DETAIL_UNIT_COLUMN = "task.unit";
    public static final int TASK_DETAIL_UNIT_WIDTH = 70;
    public static final String TASK_DETAIL_DEFAULT_COLUMN = "task.defaultValue";
    public static final int TASK_DETAIL_DEFAULT_WIDTH = 60;
    public static final String TASK_DETAIL_QUOTA_COLUMN = "task.quota";
    public static final int TASK_DETAIL_QUOTA_WIDTH = 50;

    public static final String TASK_CODE_COLUMN = "code";
    public static final int TASK_CODE_WIDTH = 60;
    public static final String TASK_NAME_COLUMN = "name";
    public static final int TASK_NAME_WIDTH = 300;
    public static final String TASK_UNIT_COLUMN = "unit";
    public static final int TASK_UNIT_WIDTH = 70;
    public static final String TASK_DEFAULT_COLUMN = "defaultValue";
    public static final int TASK_DEFAULT_WIDTH = 70;
    public static final String TASK_QUOTA_COLUMN = "quota";
    public static final int TASK_QUOTA_WIDTH = 60;

    public static final String BRANCH_NAME_COLUMN = "branch.name";
    public static int BRANCH_NAME_WIDTH = 150;
    public static final String LAST_YEAR_VALUE_COLUMN = "lastYearValue";
    public static final int LAST_YEAR_VALUE_WIDTH = 70;
    public static final String INCREASE_VALUE_COLUMN = "increaseValue";
    public static final int INCREASE_VALUE_WIDTH = 70;
    public static final String DECREASE_VALUE_COLUMN = "decreaseValue";
    public static final int DECREASE_VALUE_WIDTH = 70;
    public static final String REAL_VALUE_COLUMN = "realValue";
    public static final int REAL_VALUE_WIDTH = 80;

    public static final int TASK_LIST_SIZE = 100;
    public static final int TASK_EDIT_LIST_SIZE = 20;

    @I18nField
    Button btnAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnDelete = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnEdit = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField(emptyText = true)
    TextField<String> txtSearch = new TextField<String>();

    @I18nField
    Button btnSubTaskSave = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnSubTaskRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField
    Button btnTaskEditOk = new Button();

    @I18nField
    Button btnTaskEditCancel = new Button();

    @I18nField(emptyText = true)
    TextField<String> txtTaskSearch = new TextField<String>();

    private ContentPanel contentPanel = new ContentPanel();

    private ContentPanel taskPanel = new ContentPanel();
    private PagingToolBar taskPagingToolBar;
    private Grid<BeanModel> taskDetailGird;
    private ColumnModel taskDetailColumnModel;

    private ContentPanel subTaskPanel = new ContentPanel();
    private PagingToolBar subTaskPagingToolBar;
    private EditorGrid<BeanModel> subTaskDetailGird;
    private ColumnModel subTaskColumnModel;

    private VerticalPanel taskEditPanel = new VerticalPanel();
    private ContentPanel taskGridPanel = new ContentPanel();
    private Grid<BeanModel> taskGrid;
    private ColumnModel taskColumnModel;
    private PagingToolBar taskEditPagingToolBar;

    @Override
    protected void initializeView() {
        contentPanel.setHeaderVisible(false);
        contentPanel.setHeight(Window.getClientHeight() - 90);
        contentPanel.setLayout(new RowLayout(Style.Orientation.HORIZONTAL));
        setWidget(contentPanel);
    }

    /**
     * Create Grid on View.
     */
    public void createTaskGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        taskDetailColumnModel = new ColumnModel(createTaskDetailColumnConfig(selectionModel));
        taskDetailGird = new Grid<BeanModel>(listStore, taskDetailColumnModel);
        taskDetailGird.setBorders(true);
        taskDetailGird.setLoadMask(true);
        taskDetailGird.setStripeRows(true);
        taskDetailGird.setSelectionModel(selectionModel);
        taskDetailGird.addPlugin(selectionModel);
        taskDetailGird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        taskDetailGird.getStore().getLoader().setSortField(ID_COLUMN);
        taskDetailGird.setWidth(500);
        taskDetailGird.addListener(Events.OnKeyDown, new KeyListener() {
            @Override
            public void handleEvent(ComponentEvent e) {
                if (e.getKeyCode() == 112) {
                    btnAdd.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 113 || e.getKeyCode() == KeyCodes.KEY_ENTER) {
                    btnEdit.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 115) {
                    btnRefresh.fireEvent(Events.Select);
                }
            }
        });

        taskPagingToolBar = new PagingToolBar(TASK_LIST_SIZE);
        ToolBar toolBar = new ToolBar();

        txtSearch.setWidth(170);
        toolBar.add(txtSearch);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnEdit);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnRefresh);
        taskPanel.setHeaderVisible(false);
        taskPanel.setHeight(Window.getClientHeight() - 90);
        taskPanel.setLayout(new FitLayout());
        taskPanel.setWidth("50%");
        taskPanel.add(taskDetailGird);
        taskPanel.setTopComponent(toolBar);
        taskPanel.setBottomComponent(taskPagingToolBar);
        taskPanel.setBodyBorder(false);
        contentPanel.add(taskPanel, new RowData(-1, 1, new Margins(0, 2, 0, 0)));
        contentPanel.layout();
    }

    public void createSubTaskGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        subTaskColumnModel = new ColumnModel(createSubTaskColumnConfigs());
        subTaskDetailGird = new EditorGrid<BeanModel>(listStore, subTaskColumnModel);
        subTaskDetailGird.setBorders(true);
        subTaskDetailGird.setLoadMask(true);
        subTaskDetailGird.setStripeRows(true);
        subTaskDetailGird.setSelectionModel(selectionModel);
        subTaskDetailGird.addPlugin(selectionModel);
        subTaskDetailGird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        subTaskDetailGird.getStore().getLoader().setSortField(ID_COLUMN);
        subTaskDetailGird.setWidth(500);
        subTaskDetailGird.addListener(Events.OnKeyDown, new KeyListener() {
            @Override
            public void handleEvent(ComponentEvent e) {
                if (e.getKeyCode() == 113) {
                    btnSubTaskSave.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 115) {
                    btnSubTaskRefresh.fireEvent(Events.Select);
                }
            }
        });

        subTaskPagingToolBar = new PagingToolBar(TASK_LIST_SIZE);
        ToolBar toolBar = new ToolBar();
        toolBar.add(btnSubTaskSave);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnSubTaskRefresh);
        subTaskPanel.setBodyBorder(false);
        subTaskPanel.setHeaderVisible(false);
        subTaskPanel.setHeight(Window.getClientHeight() - 90);
        subTaskPanel.setLayout(new FitLayout());
        subTaskPanel.setWidth("50%");
        subTaskPanel.add(subTaskDetailGird);
        subTaskPanel.setTopComponent(toolBar);
        subTaskPanel.setBottomComponent(subTaskPagingToolBar);
        contentPanel.add(subTaskPanel, new RowData(-1, 1));
        contentPanel.layout();
    }

    private List<ColumnConfig> createTaskDetailColumnConfig(CheckBoxSelectionModel<BeanModel> selectionModel) {
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

        ColumnConfig taskCodeColumnConfig = new ColumnConfig(TASK_DETAIL_CODE_COLUMN, getConstant().taskCodeColumnTitle(), TASK_DETAIL_CODE_WIDTH);
        columnConfigs.add(taskCodeColumnConfig);

        ColumnConfig taskNameColumnConfig = new ColumnConfig(TASK_DETAIL_NAME_COLUMN, getConstant().taskNameColumnTitle(),
                TASK_DETAIL_NAME_WIDTH);
        columnConfigs.add(taskNameColumnConfig);

        ColumnConfig unitColumnConfig = new ColumnConfig(TASK_DETAIL_UNIT_COLUMN, getConstant().taskUnitColumnTitle(),
                TASK_DETAIL_UNIT_WIDTH);
        columnConfigs.add(unitColumnConfig);

        ColumnConfig defaultColumnConfig = new ColumnConfig(TASK_DETAIL_DEFAULT_COLUMN, getConstant().taskDefaultValueColumnTitle(),
                TASK_DETAIL_DEFAULT_WIDTH);
        columnConfigs.add(defaultColumnConfig);

        ColumnConfig quotaColumnConfig = new ColumnConfig(TASK_DETAIL_QUOTA_COLUMN, getConstant().taskQuotaColumnTitle(),
                TASK_DETAIL_QUOTA_WIDTH);
        columnConfigs.add(quotaColumnConfig);

        return columnConfigs;
    }

    private List<ColumnConfig> createSubTaskColumnConfigs() {
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

        ColumnConfig branchNameColumnConfig = new ColumnConfig(BRANCH_NAME_COLUMN, getConstant().branchNameColumnTitle(),
                BRANCH_NAME_WIDTH);
        columnConfigs.add(branchNameColumnConfig);

        String year = " " + String.valueOf((1900 + new Date().getYear() - 1));
        ColumnConfig lastYearValueColumnConfig = new ColumnConfig(LAST_YEAR_VALUE_COLUMN,
                getConstant().lastYearValueColumnTitle() + year, LAST_YEAR_VALUE_WIDTH);
        MyNumberField lastYearValueNumberField = new MyNumberField();
        lastYearValueNumberField.setSelectOnFocus(true);
        lastYearValueColumnConfig.setEditor(new CellEditor(lastYearValueNumberField));
        lastYearValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        columnConfigs.add(lastYearValueColumnConfig);

        ColumnConfig increaseValueColumnConfig = new ColumnConfig(INCREASE_VALUE_COLUMN,
                getConstant().increaseValueColumnTitle(), INCREASE_VALUE_WIDTH);
        MyNumberField increaseValueNumberField = new MyNumberField();
        increaseValueNumberField.setSelectOnFocus(true);
        increaseValueColumnConfig.setEditor(new CellEditor(increaseValueNumberField));
        increaseValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        columnConfigs.add(increaseValueColumnConfig);

        ColumnConfig decreaseValueColumnConfig = new ColumnConfig(DECREASE_VALUE_COLUMN,
                getConstant().decreaseValueColumnTitle(), DECREASE_VALUE_WIDTH);
        MyNumberField decreaseValueNumberField = new MyNumberField();
        decreaseValueNumberField.setSelectOnFocus(true);
        decreaseValueColumnConfig.setEditor(new CellEditor(decreaseValueNumberField));
        decreaseValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        columnConfigs.add(decreaseValueColumnConfig);

        SummaryColumnConfig realValueColumnConfig = new SummaryColumnConfig(REAL_VALUE_COLUMN,
                getConstant().realValueColumnTitle(), REAL_VALUE_WIDTH);
        realValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        realValueColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                                 ListStore<BeanModel> taskDetailDtoListStore, Grid<BeanModel> taskDetailDtoGrid) {
                SubTaskAnnualDetail taskAnnualDetail = model.getBean();
                Double increaseValue = taskAnnualDetail.getIncreaseValue();
                if (increaseValue == null) {
                    increaseValue = 0d;
                }
                Double decreaseValue = taskAnnualDetail.getDecreaseValue();
                if (decreaseValue == null) {
                    decreaseValue = 0d;
                }
                Double lastYearValue = taskAnnualDetail.getLastYearValue();
                if (lastYearValue == null) {
                    lastYearValue = 0d;
                }
                Double result = lastYearValue + increaseValue - decreaseValue;
                if (result == 0) {
                    return "";
                }
                SubTaskAnnualDetail subTaskAnnualDetail = model.getBean();
                subTaskAnnualDetail.setRealValue(result);
                return result;
            }
        });
        columnConfigs.add(realValueColumnConfig);
        return columnConfigs;
    }

    private List<ColumnConfig> createTaskColumnConfigs() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig taskCodeColumnConfig = new ColumnConfig(TASK_CODE_COLUMN, getConstant().taskCodeColumnTitle(), TASK_CODE_WIDTH);
        taskCodeColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                                 ListStore<BeanModel> taskDetailDtoListStore, Grid<BeanModel> taskDetailDtoGrid) {
                Task task = model.getBean();
                return new Text(task.getCode());
            }
        });

        columnConfigs.add(taskCodeColumnConfig);
        ColumnConfig taskNameColumnConfig = new ColumnConfig(TASK_NAME_COLUMN, getConstant().taskNameColumnTitle(),
                TASK_NAME_WIDTH);
        columnConfigs.add(taskNameColumnConfig);

        ColumnConfig unitColumnConfig = new ColumnConfig(TASK_UNIT_COLUMN, getConstant().taskUnitColumnTitle(),
                TASK_UNIT_WIDTH);
        columnConfigs.add(unitColumnConfig);

        ColumnConfig defaultColumnConfig = new ColumnConfig(TASK_DEFAULT_COLUMN, getConstant().taskDefaultValueColumnTitle(),
                TASK_DEFAULT_WIDTH);
        columnConfigs.add(defaultColumnConfig);

        ColumnConfig quotaColumnConfig = new ColumnConfig(TASK_QUOTA_COLUMN, getConstant().taskQuotaColumnTitle(),
                TASK_QUOTA_WIDTH);
        columnConfigs.add(quotaColumnConfig);

        for (ColumnConfig columnConfig : columnConfigs) {
            columnConfig.setMenuDisabled(true);
        }

        return columnConfigs;
    }

    public com.extjs.gxt.ui.client.widget.Window createTaskEditWindow(ListStore<BeanModel> taskListStore) {
        com.extjs.gxt.ui.client.widget.Window window = new com.extjs.gxt.ui.client.widget.Window();
        if (!taskEditPanel.isRendered()) {
            taskEditPanel.setBorders(false);
            taskEditPanel.setSpacing(4);
            taskEditPanel.setTableHeight("100%");
            taskEditPanel.setTableWidth("100%");
        }

        if (!txtTaskSearch.isRendered()) {
            txtTaskSearch.setSelectOnFocus(true);
            txtTaskSearch.setWidth(170);
            taskEditPanel.add(txtTaskSearch);
        }
        window.setFocusWidget(txtTaskSearch);

        if (taskGrid == null) {
            taskColumnModel = new ColumnModel(createTaskColumnConfigs());
            taskGrid = new Grid<BeanModel>(taskListStore, taskColumnModel);
            taskGrid.addListener(Events.OnKeyDown, new KeyListener() {
                @Override
                public void handleEvent(ComponentEvent e) {
                    if (e.getKeyCode() == KeyCodes.KEY_ENTER) {
                        btnTaskEditOk.fireEvent(Events.Select);
                    }
                }
            });
            taskGrid.setLoadMask(true);
            taskGrid.setHeight(270);
            taskGrid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
            taskGridPanel.add(taskGrid);

            taskEditPagingToolBar = new PagingToolBar(TASK_EDIT_LIST_SIZE);
            taskGridPanel.setBottomComponent(taskEditPagingToolBar);
            taskGridPanel.setBodyBorder(false);
            taskGridPanel.setHeaderVisible(false);
            taskEditPanel.add(taskGridPanel);
        }

        window.add(taskEditPanel);
        window.addButton(btnTaskEditOk);
        window.addButton(btnTaskEditCancel);
        window.setSize(640, 400);
        window.setResizable(false);
        window.setModal(true);
        window.setHeading(getConstant().taskEditPanelTitle());
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                txtTaskSearch.clear();
                taskGrid.getStore().clearFilters();
                taskDetailGird.focus();
            }
        });
        return window;
    }

    public Grid<BeanModel> getTaskDetailGird() {
        return taskDetailGird;
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

    public PagingToolBar getTaskPagingToolBar() {
        return taskPagingToolBar;
    }

    public EditorGrid<BeanModel> getSubTaskDetailGird() {
        return subTaskDetailGird;
    }

    public PagingToolBar getSubTaskPagingToolBar() {
        return subTaskPagingToolBar;
    }

    public Button getBtnSubTaskSave() {
        return btnSubTaskSave;
    }

    public TextField<String> getTxtSearch() {
        return txtSearch;
    }

    public Button getBtnTaskEditCancel() {
        return btnTaskEditCancel;
    }

    public Button getBtnTaskEditOk() {
        return btnTaskEditOk;
    }

    public Button getBtnSubTaskRefresh() {
        return btnSubTaskRefresh;
    }

    public Grid<BeanModel> getTaskGrid() {
        return taskGrid;
    }

    public TextField<String> getTxtTaskSearch() {
        return txtTaskSearch;
    }

    public PagingToolBar getTaskEditPagingToolBar() {
        return taskEditPagingToolBar;
    }
}
