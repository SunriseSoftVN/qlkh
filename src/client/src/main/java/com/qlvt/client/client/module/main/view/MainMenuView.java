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

package com.qlvt.client.client.module.main.view;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.place.TestPlace;
import com.qlvt.client.client.module.main.view.security.MainMenuViewSecutiry;
import com.smvp4g.mvp.client.core.eventbus.annotation.HistoryHandler;
import com.smvp4g.mvp.client.core.security.FieldSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;
import com.smvp4g.mvp.client.widget.MenuLink;

/**
 * The Class MainMenuView.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 9:07 AM
 */
@ViewSecurity(configuratorClass = MainMenuViewSecutiry.class)
@View(parentDomId = DomIdConstant.TOP_PANEL)
public class MainMenuView extends AbstractView {

    @FieldSecurity
    @HistoryHandler
    MenuLink menuLink = new MenuLink("Menu 1", TestPlace.class);

    Anchor ancLogout = new Anchor("Logout");

    private HorizontalPanel mainPanel = new HorizontalPanel();

    @Override
    protected void initializeView() {
        mainPanel.add(menuLink);
        mainPanel.add(ancLogout);
        mainPanel.setSpacing(5);
        setWidget(mainPanel);
        setStyleName("topmenu");
    }

    public Anchor getAncLogout() {
        return ancLogout;
    }
}
