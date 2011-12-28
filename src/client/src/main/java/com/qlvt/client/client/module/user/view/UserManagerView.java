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

package com.qlvt.client.client.module.user.view;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.user.view.i18n.UserManagerConstant;
import com.qlvt.client.client.module.user.view.security.UserManagerSecurity;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class UserManagerView.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 4:51 PM
 */
@ViewSecurity(configuratorClass = UserManagerSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = UserManagerConstant.class)
public class UserManagerView extends AbstractView<UserManagerConstant> {

    private static final String STT_COLUMN = "id";
    private static final int STT_COLUMN_WIDTH = 100;
    private static final String USER_NAME_COLUMN = "userName";
    private static final int USER_NAME_COLUMN_WIDTH = 200;
    private static final String USER_PASSWORD_COLUMN = "passWord";
    private static final int USER_PASSWORD_COLUMN_WIDTH = 300;
    private static final int USER_LIST_SIZE = 50;

    @I18nField
    Button btnAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnDelete = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnSave = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnCancel = new Button(null, IconHelper.createPath("assets/images/icons/fam/cross.png"));

    private ContentPanel contentPanel = new ContentPanel();
    private PagingToolBar pagingToolBar;
    private EditorGrid<BeanModel> usersGrid;

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

        usersGrid = new EditorGrid<BeanModel>(listStore, cm);
        usersGrid.setBorders(true);
        usersGrid.setLoadMask(true);
        usersGrid.setStripeRows(true);
        usersGrid.setSelectionModel(selectionModel);
        usersGrid.addPlugin(selectionModel);

        pagingToolBar = new PagingToolBar(USER_LIST_SIZE);

        ToolBar toolBar = new ToolBar();
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnSave);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnCancel);

        contentPanel.setLayout(new FitLayout());
        contentPanel.add(usersGrid);
        contentPanel.setTopComponent(toolBar);
        contentPanel.setBottomComponent(pagingToolBar);
        contentPanel.layout();
    }

    private List<ColumnConfig> createColumnConfig(CheckBoxSelectionModel<BeanModel> selectionModel) {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
        columnConfigs.add(selectionModel.getColumn());
        columnConfigs.add(new ColumnConfig(STT_COLUMN, getConstant().sttColumnTitle(), STT_COLUMN_WIDTH));
        ColumnConfig userNameColumnConfig = new ColumnConfig(USER_NAME_COLUMN, getConstant().userNameColumnTitle(),
                USER_NAME_COLUMN_WIDTH);
        userNameColumnConfig.setEditor(new CellEditor(new TextField<String>()));
        columnConfigs.add(userNameColumnConfig);

        ColumnConfig passWordColumnConfig = new ColumnConfig(USER_PASSWORD_COLUMN, getConstant().passWordColumnTitle(),
                USER_PASSWORD_COLUMN_WIDTH);
        passWordColumnConfig.setEditor(new CellEditor(new TextField<String>()));
        columnConfigs.add(passWordColumnConfig);

        return columnConfigs;
    }

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }

    public Grid<BeanModel> getUsersGrid() {
        return usersGrid;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }
}
