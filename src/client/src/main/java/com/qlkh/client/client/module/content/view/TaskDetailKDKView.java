/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.TaskDetailKDKConstant;
import com.qlkh.client.client.module.content.view.security.TaskDetailKDKSecurity;
import com.qlkh.client.client.widget.MyCheckBoxSelectionModel;
import com.qlkh.client.client.widget.MyNumberField;
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
@ViewSecurity(configuratorClass = TaskDetailKDKSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = TaskDetailKDKConstant.class)
public class TaskDetailKDKView extends AbstractView<TaskDetailKDKConstant> {

    public static final String ID_COLUMN = "id";
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 40;
    public static final String TASK_DETAIL_CODE_COLUMN = "code";
    public static final int TASK_DETAIL_CODE_WIDTH = 60;
    public static final String TASK_DETAIL_NAME_COLUMN = "name";
    public static final int TASK_DETAIL_NAME_WIDTH = 300;
    public static final String TASK_DETAIL_UNIT_COLUMN = "unit";
    public static final int TASK_DETAIL_UNIT_WIDTH = 70;
    public static final String TASK_DETAIL_DEFAULT_COLUMN = "defaultValue";
    public static final int TASK_DETAIL_DEFAULT_WIDTH = 60;
    public static final String TASK_DETAIL_QUOTA_COLUMN = "quota";
    public static final int TASK_DETAIL_QUOTA_WIDTH = 50;

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
    Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField
    Button btnSubTaskSave = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnSubTaskRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField(emptyText = true)
    TextField<String> txtSearch = new TextField<String>();

    private ContentPanel contentPanel = new ContentPanel();

    private ContentPanel taskPanel = new ContentPanel(new FillLayout());
    private PagingToolBar taskPagingToolBar;
    private Grid<BeanModel> taskGrid;
    private ColumnModel taskColumnModel;

    private ContentPanel subTaskPanel = new ContentPanel(new FillLayout());
    private PagingToolBar subTaskPagingToolBar;
    private EditorGrid<BeanModel> subTaskDetailGird;
    private ColumnModel subTaskColumnModel;


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
        taskColumnModel = new ColumnModel(createTaskColumnConfig(selectionModel));
        taskGrid = new Grid<BeanModel>(listStore, taskColumnModel);
        taskGrid.setBorders(true);
        taskGrid.setLoadMask(true);
        taskGrid.setStripeRows(true);
        taskGrid.setSelectionModel(selectionModel);
        taskGrid.addPlugin(selectionModel);
        taskGrid.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        taskGrid.getStore().getLoader().setSortField(ID_COLUMN);
        taskGrid.setWidth(500);
        taskGrid.addListener(Events.OnKeyDown, new KeyListener() {
            @Override
            public void handleEvent(ComponentEvent e) {
                if (e.getKeyCode() == 115) {
                    btnRefresh.fireEvent(Events.Select);
                }
            }
        });

        taskPagingToolBar = new PagingToolBar(TASK_LIST_SIZE);
        ToolBar toolBar = new ToolBar();

        txtSearch.setWidth(170);
        toolBar.add(txtSearch);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnRefresh);
        taskPanel.setHeaderVisible(false);
        taskPanel.setHeight(Window.getClientHeight() - 90);
        taskPanel.setWidth("50%");
        taskPanel.add(taskGrid);
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

    public Grid<BeanModel> getTaskGrid() {
        return taskGrid;
    }
}
