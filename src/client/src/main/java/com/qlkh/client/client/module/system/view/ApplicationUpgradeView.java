/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.system.view;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.system.view.i18n.ApplicationUpgradeConstant;
import com.qlkh.client.client.module.system.view.security.ApplicationUpgradeSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

/**
 * The Class ApplicationUpgradeView.
 *
 * @author Nguyen Duc Dung
 * @since 6/16/12, 7:04 PM
 */
@ViewSecurity(configuratorClass = ApplicationUpgradeSecurity.class)
@View(constantsClass = ApplicationUpgradeConstant.class, parentDomId = DomIdConstant.CONTENT_PANEL)
public class ApplicationUpgradeView extends AbstractView<ApplicationUpgradeConstant> {

    private ContentPanel contentPanel = new ContentPanel(new CenterLayout());

    @Override
    protected void initializeView() {
        contentPanel.setHeight(Window.getClientHeight() - 90);
        contentPanel.setHeaderVisible(false);
        setWidget(contentPanel);
    }

}
