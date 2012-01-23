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
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.view.i18n.TaskAnnualDetailConstant;
import com.qlvt.client.client.module.content.view.security.TaskAnnualDetailSecurity;
import com.qlvt.client.client.widget.MyCheckBoxSelectionModel;
import com.qlvt.core.client.model.SubTaskAnnualDetail;
import com.qlvt.core.client.model.TaskDetail;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.utils.StringUtils;
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
    public static final int STT_COLUMN_WIDTH = 50;
    public static final String TASK_CODE_COLUMN = "task";
    public static final int TASK_CODE_WIDTH = 100;
    public static final String TASK_NAME_COLUMN = "task.name";
    public static final int TASK_NAME_WIDTH = 300;
    public static final String TASK_UNIT_COLUMN = "task.unit";
    public static final int TASK_UNIT_WIDTH = 70;
    public static final String BRANCH_NAME_COLUMN = "branch.name";
    public static int BRANCH_NAME_WIDTH = 150;
    public static final String LAST_YEAR_VALUE_COLUMN = "lastYearValue";
    public static final int LAST_YEAR_VALUE_WIDTH = 100;
    public static final String INCREASE_VALUE_COLUMN = "increaseValue";
    public static final int INCREASE_VALUE_WIDTH = 100;
    public static final String DECREASE_VALUE_COLUMN = "decreaseValue";
    public static final int DECREASE_VALUE_WIDTH = 100;
    public static final String REAL_VALUE_COLUMN = "realValue";
    public static final int REAL_VALUE_WIDTH = 60;
    public static final int TASK_LIST_SIZE = 50;

    @I18nField
    Button btnAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnDelete = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnSave = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnCancel = new Button(null, IconHelper.createPath("assets/images/icons/fam/cancel.png"));

    @I18nField(emptyText = true)
    TextField<String> txtSearch = new TextField<String>();

    @I18nField
    Button btnSubTaskSave = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnSubTaskCancel = new Button(null, IconHelper.createPath("assets/images/icons/fam/cancel.png"));

    private ContentPanel contentPanel = new ContentPanel();

    private ContentPanel taskPanel = new ContentPanel();
    private PagingToolBar taskPagingToolBar;
    private EditorGrid<BeanModel> taskDetailGird;
    private CellEditor taskCodeCellEditor;
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
        MyCheckBoxSelectionModel<BeanModel> selectionModel = new MyCheckBoxSelectionModel<BeanModel>();
        taskColumnModel = new ColumnModel(createTaskColumnConfig(selectionModel));
        taskDetailGird = new EditorGrid<BeanModel>(listStore, taskColumnModel);
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
                } else if (e.getKeyCode() == 113) {
                    btnSave.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 115) {
                    btnCancel.fireEvent(Events.Select);
                }
            }
        });

        taskPagingToolBar = new PagingToolBar(TASK_LIST_SIZE);
        ToolBar toolBar = new ToolBar();

        txtSearch.setWidth(200);
        toolBar.add(txtSearch);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnSave);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnCancel);
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
        MyCheckBoxSelectionModel<BeanModel> selectionModel = new MyCheckBoxSelectionModel<BeanModel>();
        subTaskColumnModel = new ColumnModel(createSubTaskColumnConfigs(selectionModel));
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
                    btnSubTaskCancel.fireEvent(Events.Select);
                }
            }
        });

        subTaskPagingToolBar = new PagingToolBar(TASK_LIST_SIZE);
        ToolBar toolBar = new ToolBar();
        toolBar.add(btnSubTaskSave);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnSubTaskCancel);
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

        ColumnConfig taskCodeColumnConfig = new ColumnConfig(TASK_CODE_COLUMN, getConstant().taskCodeColumnTitle(), TASK_CODE_WIDTH);
        taskCodeColumnConfig.setEditor(getTaskCodeCellEditor());
        taskCodeColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                                 ListStore<BeanModel> taskDetailDtoListStore, Grid<BeanModel> taskDetailDtoGrid) {
                String code = StringUtils.EMPTY;
                TaskDetail taskDetail = model.getBean();
                if (taskDetail.getTask() != null) {
                    code = String.valueOf(taskDetail.getTask().getCode());
                }
                return new Text(code);
            }
        });

        columnConfigs.add(taskCodeColumnConfig);
        ColumnConfig taskNameColumnConfig = new ColumnConfig(TASK_NAME_COLUMN, getConstant().taskNameColumnTitle(),
                TASK_NAME_WIDTH);
        columnConfigs.add(taskNameColumnConfig);

        ColumnConfig unitColumnConfig = new ColumnConfig(TASK_UNIT_COLUMN, getConstant().taskUnitColumnTitle(),
                TASK_UNIT_WIDTH);
        columnConfigs.add(unitColumnConfig);

        for (ColumnConfig columnConfig : columnConfigs) {
            columnConfig.setMenuDisabled(true);
        }

        return columnConfigs;
    }

    private List<ColumnConfig> createSubTaskColumnConfigs(CheckBoxSelectionModel<BeanModel> selectionModel) {
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
        NumberField lastYearValueNumberField = new NumberField();
        lastYearValueNumberField.setSelectOnFocus(true);
        lastYearValueColumnConfig.setEditor(new CellEditor(lastYearValueNumberField));
        lastYearValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        columnConfigs.add(lastYearValueColumnConfig);

        ColumnConfig increaseValueColumnConfig = new ColumnConfig(INCREASE_VALUE_COLUMN,
                getConstant().increaseValueColumnTitle(), INCREASE_VALUE_WIDTH);
        NumberField increaseValueNumberField = new NumberField();
        increaseValueNumberField.setSelectOnFocus(true);
        increaseValueColumnConfig.setEditor(new CellEditor(increaseValueNumberField));
        increaseValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        columnConfigs.add(increaseValueColumnConfig);

        ColumnConfig decreaseValueColumnConfig = new ColumnConfig(DECREASE_VALUE_COLUMN,
                getConstant().decreaseValueColumnTitle(), DECREASE_VALUE_WIDTH);
        NumberField decreaseValueNumberField = new NumberField();
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
                return result;
            }
        });
        columnConfigs.add(realValueColumnConfig);
        return columnConfigs;
    }

    public EditorGrid<BeanModel> getTaskDetailGird() {
        return taskDetailGird;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public Button getBtnSave() {
        return btnSave;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public PagingToolBar getTaskPagingToolBar() {
        return taskPagingToolBar;
    }

    public void setTaskCodeCellEditor(CellEditor taskCodeCellEditor) {
        this.taskCodeCellEditor = taskCodeCellEditor;
    }

    public CellEditor getTaskCodeCellEditor() {
        return taskCodeCellEditor;
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
}
