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
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.view.i18n.TaskManagerConstant;
import com.qlvt.client.client.module.content.view.security.TaskManagerSecurity;
import com.qlvt.client.client.widget.MyFitLayout;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;
import com.smvp4g.mvp.client.widget.TextField;

import java.util.ArrayList;
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

    public static final String ID_COLUMN = "id";
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
    public static final int TASK_LIST_SIZE = 50;

    @I18nField
    Button btnAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnEdit = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnDelete = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField
    NumberField txtTaskCode = new NumberField();

    @I18nField
    TextField<String> txtTaskName = new TextField<String>();

    @I18nField
    TextField<String> txtTaskUnit = new TextField<String>();

    @I18nField
    NumberField txtTaskDefault = new NumberField();

    @I18nField
    Button btnTaskEditOk = new Button();

    @I18nField
    Button btnTaskEditCancel = new Button();

    @I18nField(emptyText = true)
    TextField<String> txtSearch = new TextField<String>();


    private ContentPanel contentPanel = new ContentPanel();
    private PagingToolBar pagingToolBar;
    private Grid<BeanModel> taskGird;

    private FormPanel taskEditPanel = new FormPanel();

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
        taskGird.getStore().getLoader().setSortField(ID_COLUMN);
        taskGird.addListener(Events.OnKeyDown, new KeyListener() {
            @Override
            public void handleEvent(ComponentEvent e) {
                if (e.getKeyCode() == 112) {
                    btnAdd.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 113) {
                    btnEdit.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 115) {
                    btnRefresh.fireEvent(Events.Select);
                }
            }
        });

        pagingToolBar = new PagingToolBar(TASK_LIST_SIZE);
        ToolBar toolBar = new ToolBar();
        txtSearch.setWidth(200);
        toolBar.add(txtSearch);
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

        window.add(taskEditPanel);
        window.addButton(btnTaskEditOk);
        window.addButton(btnTaskEditCancel);
        window.setSize(380, 200);
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

    public TextField<String> getTxtSearch() {
        return txtSearch;
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

    public NumberField getTxtTaskCode() {
        return txtTaskCode;
    }
}
