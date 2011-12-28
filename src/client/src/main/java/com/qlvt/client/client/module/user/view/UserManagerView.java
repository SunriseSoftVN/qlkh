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

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.user.view.i18n.UserManagerConstant;
import com.qlvt.client.client.module.user.view.security.UserManagerSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

/**
 * The Class UserManagerView.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 4:51 PM
 */
@ViewSecurity(configuratorClass = UserManagerSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = UserManagerConstant.class)
public class UserManagerView extends AbstractView<UserManagerConstant> {

    private ContentPanel contentPanel = new ContentPanel();

    @Override
    protected void initializeView() {
        contentPanel.setHeaderVisible(false);
        contentPanel.setHeight(500);
        setWidget(contentPanel);
    }
}
