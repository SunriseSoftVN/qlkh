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
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.view.i18n.TaskDetailConstant;
import com.qlvt.client.client.module.content.view.security.TaskDetailSecurity;
import com.qlvt.core.client.dto.TaskDetailDto;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

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
    public static final int STT_COLUMN_WIDTH = 50;
    public static final String TASK_CODE_COLUMN = "task";
    public static final int TASK_CODE_WIDTH = 100;
    public static final String TASK_NAME_COLUMN = "task.name";
    public static final int TASK_NAME_WIDTH = 300;
    public static final String TASK_UNIT_COLUMN = "task.unit";
    public static final int TASK_UNIT_WIDTH = 70;
    public static final int TASK_LIST_SIZE = 50;

    @I18nField
    Button btnAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnDelete = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnSave = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnCancel = new Button(null, IconHelper.createPath("assets/images/icons/fam/cancel.png"));

    private ContentPanel contentPanel = new ContentPanel();
    private PagingToolBar pagingToolBar;
    private EditorGrid<TaskDetailDto> taskDetailGird;
    private CellEditor taskCodeCellEditor;
    private ColumnModel columnModel;

    /**
     * Create Grid on View.
     */
    public void createGrid(ListStore<TaskDetailDto> listStore, List<String> branchNames) {
        CheckBoxSelectionModel<TaskDetailDto> selectionModel = new CheckBoxSelectionModel<TaskDetailDto>();
        columnModel = new ColumnModel(createColumnConfig(selectionModel, branchNames));
        columnModel.addHeaderGroup(0, 0, new HeaderGroupConfig(getConstant().taskHeaderGroup(), 1, 5));
        int column = 5;
        for (String branchName : branchNames) {
            columnModel.addHeaderGroup(0, column, new HeaderGroupConfig(branchName,1, 4));
            column += 4;
        }
        taskDetailGird = new EditorGrid<TaskDetailDto>(listStore, columnModel);
        taskDetailGird.setBorders(true);
        taskDetailGird.setLoadMask(true);
        taskDetailGird.setStripeRows(true);
        taskDetailGird.setSelectionModel(selectionModel);
        taskDetailGird.addPlugin(selectionModel);
        taskDetailGird.getStore().getLoader().setSortDir(Style.SortDir.DESC);
        taskDetailGird.getStore().getLoader().setSortField(ID_COLUMN);

        pagingToolBar = new PagingToolBar(TASK_LIST_SIZE);
        ToolBar toolBar = new ToolBar();
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnSave);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnCancel);

        contentPanel.setHeaderVisible(false);
        contentPanel.setHeight(500);
        contentPanel.setLayout(new FitLayout());
        contentPanel.add(taskDetailGird);
        contentPanel.setTopComponent(toolBar);
        contentPanel.setBottomComponent(pagingToolBar);
        setWidget(contentPanel);
    }

    private List<ColumnConfig> createColumnConfig(CheckBoxSelectionModel<TaskDetailDto> selectionModel, 
                                                  List<String> branchNames) {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
        columnConfigs.add(selectionModel.getColumn());
        ColumnConfig sttColumnConfig = new ColumnConfig(STT_COLUMN, getConstant().sttColumnTitle(), STT_COLUMN_WIDTH);
        sttColumnConfig.setRenderer(new GridCellRenderer<TaskDetailDto>() {
            @Override
            public Object render(TaskDetailDto model, String property, ColumnData config, int rowIndex, int colIndex,
                                 ListStore<TaskDetailDto> beanModelListStore, Grid<TaskDetailDto> beanModelGrid) {
                if (model.get(STT_COLUMN) == null) {
                    model.set(STT_COLUMN, rowIndex + 1);
                }
                return new Text(String.valueOf(model.get(STT_COLUMN)));
            }
        });
        columnConfigs.add(sttColumnConfig);

        ColumnConfig taskCodeColumnConfig = new ColumnConfig(TASK_CODE_COLUMN, getConstant().taskCodeColumnTitle(), TASK_CODE_WIDTH);
        taskCodeColumnConfig.setEditor(getTaskCodeCellEditor());
        taskCodeColumnConfig.setRenderer(new GridCellRenderer<TaskDetailDto>() {
            @Override
            public Object render(TaskDetailDto model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<TaskDetailDto> taskDetailDtoListStore, Grid<TaskDetailDto> taskDetailDtoGrid) {
                String code = StringUtils.EMPTY;
                if (model.getTask() != null) {
                    code = String.valueOf(model.getTask().getCode());
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
        
        
        for (String branchName : branchNames) {
            ColumnConfig q1ColumnConfig = new ColumnConfig(branchName + ".q1", "q1", 50);
            columnConfigs.add(q1ColumnConfig);

            ColumnConfig q2ColumnConfig = new ColumnConfig(branchName + ".q2", "q2", 50);
            columnConfigs.add(q2ColumnConfig);

            ColumnConfig q3ColumnConfig = new ColumnConfig(branchName + ".q3", "q3", 50);
            columnConfigs.add(q3ColumnConfig);

            ColumnConfig q4ColumnConfig = new ColumnConfig(branchName + ".q4", "q4", 50);
            columnConfigs.add(q4ColumnConfig);
        }
        
        return columnConfigs;
    }

    public EditorGrid<TaskDetailDto> getTaskDetailGird() {
        return taskDetailGird;
    }

    public void setTaskDetailGird(EditorGrid<TaskDetailDto> taskDetailGird) {
        this.taskDetailGird = taskDetailGird;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }

    public void setBtnAdd(Button btnAdd) {
        this.btnAdd = btnAdd;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(Button btnDelete) {
        this.btnDelete = btnDelete;
    }

    public Button getBtnSave() {
        return btnSave;
    }

    public void setBtnSave(Button btnSave) {
        this.btnSave = btnSave;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(Button btnCancel) {
        this.btnCancel = btnCancel;
    }

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }

    public void setPagingToolBar(PagingToolBar pagingToolBar) {
        this.pagingToolBar = pagingToolBar;
    }

    public void setTaskCodeCellEditor(CellEditor taskCodeCellEditor) {
        this.taskCodeCellEditor = taskCodeCellEditor;
    }

    public CellEditor getTaskCodeCellEditor() {
        return taskCodeCellEditor;
    }
}
