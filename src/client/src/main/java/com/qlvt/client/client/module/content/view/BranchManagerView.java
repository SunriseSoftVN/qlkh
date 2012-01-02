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
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.view.i18n.BranchManagerConstant;
import com.qlvt.client.client.module.content.view.security.BranchManagerSecurity;
import com.qlvt.core.client.model.Branch;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class BranchManagerView.
 *
 * @author Nguyen Duc Dung
 * @since 1/2/12, 1:02 PM
 */
@ViewSecurity(configuratorClass = BranchManagerSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = BranchManagerConstant.class)
public class BranchManagerView extends AbstractView<BranchManagerConstant> {

    public static final String ID_COLUMN = "id";
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 50;
    public static final String BRANCH_NAME_COLUMN = "name";
    public static final int BRANCH_NAME_WIDTH = 300;
    public static final String STATION_NAME_COLUMN = "station";
    public static final int STATION_NAME_WIDTH = 300;
    public static final int BRANCH_LIST_SIZE = 50;

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
    private EditorGrid<BeanModel> branchsGird;

    private CellEditor stationCellEditor;

    @Override
    protected void initializeView() {
        contentPanel.setHeaderVisible(false);
        contentPanel.setHeight(500);
        setWidget(contentPanel);
    }

    /**
     * Create Grid on View.
     */
    public void createGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        ColumnModel cm = new ColumnModel(createColumnConfig(selectionModel));
        branchsGird = new EditorGrid<BeanModel>(listStore, cm);
        branchsGird.setBorders(true);
        branchsGird.setLoadMask(true);
        branchsGird.setStripeRows(true);
        branchsGird.setSelectionModel(selectionModel);
        branchsGird.addPlugin(selectionModel);
        branchsGird.getStore().getLoader().setSortDir(Style.SortDir.DESC);
        branchsGird.getStore().getLoader().setSortField(ID_COLUMN);

        pagingToolBar = new PagingToolBar(BRANCH_LIST_SIZE);
        ToolBar toolBar = new ToolBar();
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnSave);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnCancel);

        contentPanel.setLayout(new FitLayout());
        contentPanel.add(branchsGird);
        contentPanel.setTopComponent(toolBar);
        contentPanel.setBottomComponent(pagingToolBar);
        contentPanel.layout();
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
        ColumnConfig branchNameColumnConfig = new ColumnConfig(BRANCH_NAME_COLUMN, getConstant().branchNameColumnTitle(),
                BRANCH_NAME_WIDTH);
        branchNameColumnConfig.setEditor(new CellEditor(new TextField<String>()));
        columnConfigs.add(branchNameColumnConfig);

        ColumnConfig stationNameColumnConfig = new ColumnConfig(STATION_NAME_COLUMN, getConstant().stationNameColumnTitle(),
                STATION_NAME_WIDTH);
        stationNameColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
                Branch branch = model.getBean();
                String st = StringUtils.EMPTY;
                if (branch.getStation() != null) {
                    st = branch.getStation().getName();
                }
                return new Text(st);
            }
        });
        stationNameColumnConfig.setEditor(getStationCellEditor());
        columnConfigs.add(stationNameColumnConfig);
        return columnConfigs;
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

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }

    public EditorGrid<BeanModel> getBranchsGird() {
        return branchsGird;
    }

    public CellEditor getStationCellEditor() {
        return stationCellEditor;
    }

    public void setStationCellEditor(CellEditor stationCellEditor) {
        this.stationCellEditor = stationCellEditor;
    }
}
