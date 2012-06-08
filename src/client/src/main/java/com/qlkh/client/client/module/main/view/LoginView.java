/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.main.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.main.view.i18n.LoginConstants;
import com.qlkh.client.client.utils.BrowserUtils;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;
import com.smvp4g.mvp.client.widget.TextField;

/**
 * The Class LoginView.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 9:51 AM
 */
@ViewSecurity(showOnlyGuest = true)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = LoginConstants.class)
public class LoginView extends AbstractView<LoginConstants> {

    private Html downloadChromeHtml = new Html(getConstant().downloadChrome());

    private ContentPanel contentPanel = new ContentPanel(new CenterLayout());

    @I18nField
    FormPanel loginPanel = new FormPanel();

    @I18nField
    TextField<String> txtUserName = new TextField<String>();

    @I18nField
    TextField<String> txtPassWord = new TextField<String>();

    @I18nField
    Button btnOk = new Button();

    @I18nField
    Button btnCancel = new Button();

    @Override
    protected void initializeView() {
        txtPassWord.setPassword(true);
        loginPanel.setFrame(true);

        loginPanel.add(txtUserName);
        loginPanel.add(txtPassWord);
        loginPanel.setLabelWidth(100);

        loginPanel.addButton(btnOk);
        loginPanel.addButton(btnCancel);
        loginPanel.setButtonAlign(Style.HorizontalAlignment.CENTER);

        contentPanel.add(loginPanel);

        if (!BrowserUtils.is_chrome()) {
            contentPanel.add(downloadChromeHtml);
        }

        contentPanel.setHeight(Window.getClientHeight() - 90);
        contentPanel.setHeaderVisible(false);

        setWidget(contentPanel);
    }

    public Button getBtnOk() {
        return btnOk;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public TextField<String> getTxtUserName() {
        return txtUserName;
    }

    public TextField<String> getTxtPassWord() {
        return txtPassWord;
    }
}
