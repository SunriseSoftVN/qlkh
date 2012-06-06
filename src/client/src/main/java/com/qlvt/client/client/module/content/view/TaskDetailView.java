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
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Window;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.view.i18n.TaskDetailConstant;
import com.qlvt.client.client.module.content.view.security.TaskDetailSecurity;
import com.qlvt.client.client.widget.MyCheckBoxSelectionModel;
import com.qlvt.client.client.widget.MyNumberField;
import com.qlvt.core.client.model.Task;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;
import com.smvp4g.mvp.client.widget.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TaskDetailView.
 *
 * @author Nguyen Duc Dung
 * @since 1/1/12, 3:36 PM
 */
@ViewSecurity(configuratorClass = TaskDetailSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = TaskDetailConstant.class)
public class TaskDetailView extends AbstractView<TaskDetailConstant> {

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
    public static final String Q1_UNIT_COLUMN = "q1";
    public static final int Q1_UNIT_WIDTH = 70;
    public static final String Q2_UNIT_COLUMN = "q2";
    public static final int Q2_UNIT_WIDTH = 70;
    public static final String Q3_UNIT_COLUMN = "q3";
    public static final int Q3_UNIT_WIDTH = 70;
    public static final String Q4_UNIT_COLUMN = "q4";
    public static final int Q4_UNIT_WIDTH = 70;

    public static final int TASK_LIST_SIZE = 100;
    public static final int TASK_EDIT_LIST_SIZE = 20;

    @I18nField
    Button btnAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnDelete = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField
    Button btnSubTaskSave = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnSubTaskRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField(emptyText = true)
    TextField<String> txtSearch = new TextField<String>();

    @I18nField(emptyText = true)
    TextField<String> txtTaskSearch = new TextField<String>();

    @I18nField
    Button btnTaskEditOk = new Button();

    @I18nField
    Button btnTaskEditCancel = new Button();

    private ContentPanel contentPanel = new ContentPanel();

    private ContentPanel taskPanel = new ContentPanel(new FillLayout());
    private PagingToolBar taskPagingToolBar;
    private Grid<BeanModel> taskDetailGird;
    private ColumnModel taskDetailColumnModel;

    private ContentPanel subTaskPanel = new ContentPanel(new FillLayout());
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
        MyCheckBoxSelectionModel<BeanModel> selectionModel = new MyCheckBoxSelectionModel<BeanModel>();
        taskDetailColumnModel = new ColumnModel(createTaskColumnConfig(selectionModel));
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
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnRefresh);
        taskPanel.setHeaderVisible(false);
        taskPanel.setHeight(Window.getClientHeight() - 90);
        taskPanel.setWidth("50%");
        taskPanel.add(taskDetailGird);
        taskPanel.setTopComponent(toolBar);
        taskPanel.setBottomComponent(taskPagingToolBar);
        taskPanel.setBodyBorder(false);
        contentPanel.add(taskPanel, new RowData(-1, 1, new Margins(0, 2, 0, 0)));
        contentPanel.layout();
    }

    public void createSubTaskGrid(ListStore<BeanModel> listStore,
                                  boolean q1, boolean q2, boolean q3, boolean q4) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        subTaskColumnModel = new ColumnModel(createSubTaskColumnConfigs(q1, q2, q3, q4));
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
        subTaskPanel.setWidth("50%");
        subTaskPanel.add(subTaskDetailGird);
        subTaskPanel.setTopComponent(toolBar);
        subTaskPanel.setBottomComponent(subTaskPagingToolBar);
        contentPanel.add(subTaskPanel, new RowData(-1, 1));
        contentPanel.layout();
    }

    private List<ColumnConfig> createTaskColumnConfig(CheckBoxSelectionModel<BeanModel> selectionModel) {
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

        ColumnConfig taskCodeColumnConfig = new ColumnConfig(TASK_DETAIL_CODE_COLUMN, getConstant().taskCodeColumnTitle(),
                TASK_DETAIL_CODE_WIDTH);
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

    private List<ColumnConfig> createSubTaskColumnConfigs(boolean q1, boolean q2, boolean q3, boolean q4) {
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

        ColumnConfig q1ColumnConfig = new ColumnConfig(Q1_UNIT_COLUMN, getConstant().q1ColumnTitle(), Q1_UNIT_WIDTH);
        if (!q1) {
            MyNumberField q1NumberField = new MyNumberField();
            q1NumberField.setSelectOnFocus(true);
            q1ColumnConfig.setEditor(new CellEditor(q1NumberField));
        }
        columnConfigs.add(q1ColumnConfig);

        ColumnConfig q2ColumnConfig = new ColumnConfig(Q2_UNIT_COLUMN, getConstant().q2ColumnTitle(), Q2_UNIT_WIDTH);
        if (!q2) {
            MyNumberField q2NumberField = new MyNumberField();
            q2NumberField.setSelectOnFocus(true);
            q2ColumnConfig.setEditor(new CellEditor(q2NumberField));
        }
        columnConfigs.add(q2ColumnConfig);

        ColumnConfig q3ColumnConfig = new ColumnConfig(Q3_UNIT_COLUMN, getConstant().q3ColumnTitle(), Q3_UNIT_WIDTH);
        if (!q3) {
            MyNumberField q3NumberField = new MyNumberField();
            q3NumberField.setSelectOnFocus(true);
            q3ColumnConfig.setEditor(new CellEditor(q3NumberField));
        }
        columnConfigs.add(q3ColumnConfig);

        ColumnConfig q4ColumnConfig = new ColumnConfig(Q4_UNIT_COLUMN, getConstant().q4ColumnTitle(), Q4_UNIT_WIDTH);
        if (!q4) {
            MyNumberField q4NumberField = new MyNumberField();
            q4NumberField.setSelectOnFocus(true);
            q4ColumnConfig.setEditor(new CellEditor(q4NumberField));
        }
        columnConfigs.add(q4ColumnConfig);

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

    public Button getBtnRefresh() {
        return btnRefresh;
    }

    public PagingToolBar getSubTaskPagingToolBar() {
        return subTaskPagingToolBar;
    }

    public EditorGrid<BeanModel> getSubTaskDetailGird() {
        return subTaskDetailGird;
    }

    public PagingToolBar getTaskPagingToolBar() {
        return taskPagingToolBar;
    }

    public Button getBtnSubTaskRefresh() {
        return btnSubTaskRefresh;
    }

    public Button getBtnSubTaskSave() {
        return btnSubTaskSave;
    }

    public TextField<String> getTxtSearch() {
        return txtSearch;
    }

    public Button getBtnTaskEditOk() {
        return btnTaskEditOk;
    }

    public Button getBtnTaskEditCancel() {
        return btnTaskEditCancel;
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
