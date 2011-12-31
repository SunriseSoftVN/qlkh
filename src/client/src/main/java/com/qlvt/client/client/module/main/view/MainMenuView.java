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

import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.place.StationManagerPlace;
import com.qlvt.client.client.module.content.place.TaskManagerPlace;
import com.qlvt.client.client.module.main.view.i18n.MainMenuConstant;
import com.qlvt.client.client.module.main.view.security.MainMenuViewSecutiry;
import com.qlvt.client.client.module.user.place.UserManagerPlace;
import com.smvp4g.mvp.client.core.eventbus.annotation.HistoryHandler;
import com.smvp4g.mvp.client.core.i18n.I18nField;
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
@View(parentDomId = DomIdConstant.TOP_PANEL, constantsClass = MainMenuConstant.class)
public class MainMenuView extends AbstractView<MainMenuConstant> {

    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlUserManager = new MenuLink(UserManagerPlace.class);

    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlStationManage = new MenuLink(StationManagerPlace.class);

    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlTaskManage = new MenuLink(TaskManagerPlace.class);

    @FieldSecurity
    @I18nField
    Anchor ancLogout = new Anchor("");

    @FieldSecurity
    @I18nField
    Label lblWelcome = new Label();

    private LayoutContainer mainPanel = new LayoutContainer();

    private HBoxLayout layout = new HBoxLayout();

    @Override
    protected void initializeView() {
        layout.setPadding(new Padding(5));
        layout.setHBoxLayoutAlign(HBoxLayout.HBoxLayoutAlign.MIDDLE);
        mainPanel.setLayout(layout);
        mainPanel.add(mnlUserManager, new HBoxLayoutData(new Margins(0, 5, 0, 0)));
        mainPanel.add(mnlStationManage, new HBoxLayoutData(new Margins(0, 5, 0, 0)));
        mainPanel.add(mnlTaskManage, new HBoxLayoutData(new Margins(0, 5, 0, 0)));

        HBoxLayoutData flex = new HBoxLayoutData();
        flex.setFlex(1);
        mainPanel.add(new Text(), flex);
        lblWelcome.setWidth("200px");
        lblWelcome.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        mainPanel.add(lblWelcome, new HBoxLayoutData(new Margins(0, 5, 0, 0)));
        mainPanel.add(ancLogout);
        setWidget(mainPanel);
        setStyleName("topmenu");
    }

    public HBoxLayout getLayout() {
        return layout;
    }

    public Anchor getAncLogout() {
        return ancLogout;
    }

    public Label getLblWelcome() {
        return lblWelcome;
    }
}
