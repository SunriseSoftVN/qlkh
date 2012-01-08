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

import com.qlvt.client.client.constant.DomIdConstant;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
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

    private ContentPanel contentPanel = new ContentPanel();
    private FramedPanel loginPanel = new FramedPanel();

    private TextField txtUserName = new TextField();
    private FieldLabel lblUserName = new FieldLabel(txtUserName, "Username");

    private PasswordField txtPassWord = new PasswordField();
    private FieldLabel lblPassword = new FieldLabel(txtPassWord, "Password");

    private TextButton btnOk = new TextButton("Ok");
    private TextButton btnCancel = new TextButton("Cancel");
    
    @Override
    protected void initializeView() {
        loginPanel.setHeadingText("User Login");

        loginPanel.add(lblUserName);
        loginPanel.add(lblPassword);

        loginPanel.addButton(btnOk);
        loginPanel.addButton(btnCancel);
        loginPanel.setButtonAlign(BoxLayoutContainer.BoxLayoutPack.CENTER);

        contentPanel.add(loginPanel);
        contentPanel.setHeight(500);
        contentPanel.setHeaderVisible(false);
        setWidget(contentPanel);
    }

    public TextButton getBtnOk() {
        return btnOk;
    }

    public TextButton getBtnCancel() {
        return btnCancel;
    }

    public TextField getTxtUserName() {
        return txtUserName;
    }

    public PasswordField getTxtPassWord() {
        return txtPassWord;
    }
}
