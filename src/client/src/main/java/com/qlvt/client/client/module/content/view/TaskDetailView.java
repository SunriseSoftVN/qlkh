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
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.view.i18n.TaskDetailConstant;
import com.qlvt.client.client.module.content.view.security.TaskDetailSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

/**
 * The Class TaskDetailView.
 *
 * @author Nguyen Duc Dung
 * @since 1/1/12, 3:36 PM
 */
@ViewSecurity(configuratorClass = TaskDetailSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = TaskDetailConstant.class)
public class TaskDetailView extends AbstractView<TaskDetailConstant> {

    private ContentPanel contentPanel = new ContentPanel();
    private PagingToolBar pagingToolBar;
    private EditorGrid<BeanModel> taskDetailGird;

    @Override
    protected void initializeView() {
        contentPanel.setHeaderVisible(false);
        contentPanel.setHeight(500);
        setWidget(contentPanel);
    }

}
