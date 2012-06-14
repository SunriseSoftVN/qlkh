/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.view.share;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
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
import com.qlkh.client.client.module.content.view.i18n.TaskDetailDKConstant;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.widget.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class AbstractTaskDetailView.
 *
 * @author Nguyen Duc Dung
 * @since 6/11/12, 6:08 PM
 */
public class AbstractTaskDetailView<C extends TaskDetailDKConstant> extends AbstractView<C> {

    public static final String ID_COLUMN = "id";
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 35;
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

    public static final int TASK_LIST_SIZE = 100;

    protected ContentPanel contentPanel = new ContentPanel();

    private Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));
    private TextField<String> txtSearch = new TextField<String>();
    private Button btnSubTaskSave = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));
    private Button btnSubTaskRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));


    private ContentPanel taskPanel = new ContentPanel();
    private PagingToolBar taskPagingToolBar;
    private Grid<BeanModel> taskGird;
    private ColumnModel taskColumnModel;

    private ContentPanel subTaskPanel = new ContentPanel();
    private PagingToolBar subTaskPagingToolBar;
    private EditorGrid<BeanModel> subTaskDetailGird;

    @Override
    protected void initializeView() {
        btnRefresh.setText(getConstant().btnRefresh());
        txtSearch.setEmptyText(getConstant().txtSearch());
        btnSubTaskSave.setText(getConstant().btnSubTaskSave());
        btnSubTaskRefresh.setText(getConstant().btnSubTaskRefresh());
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

    public Grid<BeanModel> getTaskGird() {
        return taskGird;
    }

    public Button getBtnRefresh() {
        return btnRefresh;
    }

    public PagingToolBar getTaskPagingToolBar() {
        return taskPagingToolBar;
    }

    public TextField<String> getTxtSearch() {
        return txtSearch;
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public void createSubTaskGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        subTaskDetailGird = new EditorGrid<BeanModel>(listStore, createSubTaskModel());
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
        subTaskPanel.setTopComponent(toolBar);

        subTaskPanel.setBodyBorder(false);
        subTaskPanel.setHeaderVisible(false);
        subTaskPanel.setHeight(Window.getClientHeight() - 90);
        subTaskPanel.setLayout(new FitLayout());
        subTaskPanel.setWidth("50%");
        subTaskPanel.add(subTaskDetailGird);
        subTaskPanel.setBottomComponent(subTaskPagingToolBar);
        contentPanel.add(subTaskPanel, new RowData(-1, 1));
        contentPanel.layout();
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

    public Button getBtnSubTaskRefresh() {
        return btnSubTaskRefresh;
    }

    protected ColumnModel createSubTaskModel() {
        return new ColumnModel(createSubTaskColumnConfigs());
    }

    protected List<ColumnConfig> createSubTaskColumnConfigs() {
        return null;
    }
}
