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

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.qlvt.client.client.constant.DomIdConstant;
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
@View(parentDomId = DomIdConstant.CONTENT_PANEL)
public class LoginView extends AbstractView {

    private ContentPanel contentPanel = new ContentPanel(new CenterLayout());
    private FormPanel loginPanel = new FormPanel();

    private TextField<String> txtUserName = new TextField<String>();
    private TextField<String> txtPassWord = new TextField<String>();

    private Button btnOk = new Button("Ok");
    private Button btnCancel = new Button("Cancel");
    
    @Override
    protected void initializeView() {
        txtUserName.setFieldLabel("Username");
        txtPassWord.setFieldLabel("Password");

        loginPanel.setFrame(true);
        loginPanel.setHeading("User Login");

        loginPanel.add(txtUserName);
        loginPanel.add(txtPassWord);

        loginPanel.addButton(btnOk);
        loginPanel.addButton(btnCancel);
        loginPanel.setButtonAlign(Style.HorizontalAlignment.CENTER);

        contentPanel.add(loginPanel);
        contentPanel.setHeight(500);
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
