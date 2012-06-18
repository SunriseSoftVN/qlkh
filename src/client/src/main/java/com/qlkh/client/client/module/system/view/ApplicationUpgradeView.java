/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.system.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.system.view.i18n.ApplicationUpgradeConstant;
import com.qlkh.client.client.module.system.view.security.ApplicationUpgradeSecurity;
import com.smvp4g.mvp.client.core.i18n.I18nField;
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

    @I18nField
    FormPanel upgradePanel = new FormPanel();

    @I18nField
    Button upgradeV11 = new Button();

    private ContentPanel contentPanel = new ContentPanel();

    @Override
    protected void initializeView() {
        upgradePanel.setFrame(true);
        upgradePanel.setAutoWidth(true);
        upgradePanel.setAutoHeight(true);

        upgradePanel.add(upgradeV11);

        contentPanel.add(upgradePanel);
        contentPanel.setHeaderVisible(false);
        contentPanel.setFrame(true);
        contentPanel.setHeight(Window.getClientHeight() - 90);
        contentPanel.setLayout(new RowLayout(Style.Orientation.HORIZONTAL));
        setWidget(contentPanel);
    }

    public Button getUpgradeV11() {
        return upgradeV11;
    }
}