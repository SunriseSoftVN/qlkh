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
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.place.*;
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
    MenuLink mnlStationManager = new MenuLink(StationManagerPlace.class);


    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlBranchManager = new MenuLink(BranchManagerPlace.class);

    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlTaskManage = new MenuLink(TaskManagerPlace.class);

    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlTaskDetail = new MenuLink(TaskDetailPlace.class);

    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlTaskAnnualDetail = new MenuLink(TaskAnnualDetailPlace.class);

    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlReport = new MenuLink(ReportPlace.class);

    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mlLock = new MenuLink(StationLockPlace.class);

    @FieldSecurity
    @I18nField
    Anchor ancLogout = new Anchor("");

    @FieldSecurity
    @I18nField
    HTML lblWelcome = new HTML();

    private LayoutContainer mainPanel = new LayoutContainer();

    private HBoxLayout layout = new HBoxLayout();

    @Override
    protected void initializeView() {
        layout.setPadding(new Padding(5));
        layout.setHBoxLayoutAlign(HBoxLayout.HBoxLayoutAlign.MIDDLE);
        mainPanel.setLayout(layout);

        //Admin
        mnlUserManager.setStyleName("menulink");
        mainPanel.add(mnlUserManager, new HBoxLayoutData(new Margins(0, 5, 0, 0)));
        mnlStationManager.setStyleName("menulink");
        mainPanel.add(mnlStationManager, new HBoxLayoutData(new Margins(0, 5, 0, 0)));
        mnlBranchManager.setStyleName("menulink");
        mainPanel.add(mnlBranchManager, new HBoxLayoutData(new Margins(0, 5, 0, 0)));

        //Manager
        mnlTaskManage.setStyleName("menulink");
        mainPanel.add(mnlTaskManage, new HBoxLayoutData(new Margins(0, 5, 0, 0)));

        //User
        mnlTaskAnnualDetail.setStyleName("menulink");
        mainPanel.add(mnlTaskAnnualDetail, new HBoxLayoutData(new Margins(0, 5, 0, 0)));
        mnlTaskDetail.setStyleName("menulink");
        mainPanel.add(mnlTaskDetail, new HBoxLayoutData(new Margins(0, 5, 0, 0)));

        //Report
        mnlReport.setStyleName("menulink");
        mainPanel.add(mnlReport, new HBoxLayoutData(new Margins(0, 5, 0, 0)));

        //Lock
        mlLock.setStyleName("menulink");
        mainPanel.add(mlLock, new HBoxLayoutData(new Margins(0, 5, 0, 0)));

        HBoxLayoutData flex = new HBoxLayoutData();
        flex.setFlex(1);
        mainPanel.add(new Text(), flex);
        lblWelcome.setWidth("200px");
        lblWelcome.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        mainPanel.add(lblWelcome, new HBoxLayoutData(new Margins(0, 5, 0, 0)));
        mainPanel.add(ancLogout);
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                mainPanel.setWidth(event.getWidth() - 5);
            }
        });
        setWidget(mainPanel);
        setStyleName("topmenu");
    }

    public HBoxLayout getLayout() {
        return layout;
    }

    public Anchor getAncLogout() {
        return ancLogout;
    }

    public HTML getLblWelcome() {
        return lblWelcome;
    }
}
