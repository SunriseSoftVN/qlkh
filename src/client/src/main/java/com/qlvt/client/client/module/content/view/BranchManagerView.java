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

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.view.i18n.BranchManagerConstant;
import com.qlvt.client.client.module.content.view.security.BranchManagerSecurity;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

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
    public static final String STATION_NAME_COLUMN = "name";
    public static final int STATION_NAME_WIDTH = 300;
    public static final int STATION_LIST_SIZE = 50;

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
    private EditorGrid<BeanModel> stationsGird;

    @Override
    protected void initializeView() {
        contentPanel.setHeaderVisible(false);
        contentPanel.setHeight(500);
        setWidget(contentPanel);
    }

}
