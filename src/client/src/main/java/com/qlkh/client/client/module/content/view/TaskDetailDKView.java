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
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.TaskDetailDKConstant;
import com.qlkh.client.client.module.content.view.security.TaskAnnualDetailDK;
import com.qlkh.client.client.widget.MyNumberField;
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
@ViewSecurity(configuratorClass = TaskAnnualDetailDK.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = TaskDetailDKConstant.class)
public class TaskDetailDKView extends AbstractView<TaskDetailDKConstant> {


    public static final String ID_COLUMN = "id";
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 40;
    public static final String TASK_CODE_COLUMN = "code";
    public static final int TASK_CODE_WIDTH = 60;
    public static final String TASK_NAME_COLUMN = "name";
    public static final int TASK_NAME_WIDTH = 300;
    public static final String TASK_UNIT_COLUMN = "unit";
    public static final int TASK_UNIT_WIDTH = 70;
    public static final String TASK_DEFAULT_COLUMN = "defaultValue";
    public static final int TASK_DEFAULT_WIDTH = 60;
    public static final String TASK_QUOTA_COLUMN = "quota";
    public static final int TASK_QUOTA_WIDTH = 50;

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

    @I18nField
    Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField(emptyText = true)
    TextField<String> txtSearch = new TextField<String>();

    @I18nField
    Button btnSubTaskSave = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnSubTaskRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    private ContentPanel contentPanel = new ContentPanel();

    private ContentPanel taskPanel = new ContentPanel();
    private PagingToolBar taskPagingToolBar;
    private Grid<BeanModel> taskGird;
    private ColumnModel taskColumnModel;

    private ContentPanel subTaskPanel = new ContentPanel();
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
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        taskColumnModel = new ColumnModel(createTaskDetailColumnConfig(selectionModel));
        taskGird = new Grid<BeanModel>(listStore, taskColumnModel);
        taskGird.setBorders(true);
        taskGird.setLoadMask(true);
        taskGird.setStripeRows(true);
        taskGird.setSelectionModel(selectionModel);
        taskGird.addPlugin(selectionModel);
        taskGird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        taskGird.getStore().getLoader().setSortField(ID_COLUMN);
        taskGird.setWidth(500);
        taskGird.addListener(Events.OnKeyDown, new KeyListener() {
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
        taskPanel.setLayout(new FitLayout());
        taskPanel.setWidth("50%");
        taskPanel.add(taskGird);
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

        ColumnConfig taskCodeColumnConfig = new ColumnConfig(TASK_CODE_COLUMN, getConstant().taskCodeColumnTitle(), TASK_CODE_WIDTH);
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
        columnConfigs.add(realValueColumnConfig);
        return columnConfigs;
    }

    public Grid<BeanModel> getTaskGird() {
        return taskGird;
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

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public Button getBtnSubTaskRefresh() {
        return btnSubTaskRefresh;
    }
}
