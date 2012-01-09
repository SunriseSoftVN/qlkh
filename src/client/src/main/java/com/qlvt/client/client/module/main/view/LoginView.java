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

import com.google.gwt.user.client.Window;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.main.view.i18n.LoginConstant;
import com.qlvt.client.client.widget.MyCenterLayoutContainer;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
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
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = LoginConstant.class)
public class LoginView extends AbstractView<LoginConstant> {

    private MyCenterLayoutContainer centerLayoutContainer = new MyCenterLayoutContainer();
    private VerticalLayoutContainer loginPanel = new VerticalLayoutContainer();

    private TextField txtUserName = new TextField();

    private PasswordField txtPassWord = new PasswordField();

    @I18nField
    FramedPanel contentPanel = new FramedPanel();

    @I18nField
    FieldLabel lblUserName = new FieldLabel(txtUserName);

    @I18nField
    FieldLabel lblPassword = new FieldLabel(txtPassWord);

    @I18nField
    TextButton btnOk = new TextButton();

    @I18nField
    TextButton btnCancel = new TextButton();
    
    @Override
    protected void initializeView() {
        loginPanel.add(lblUserName, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        loginPanel.add(lblPassword, new VerticalLayoutContainer.VerticalLayoutData(1, -1));

        contentPanel.add(loginPanel);
        btnOk.setWidth(50);
        contentPanel.addButton(btnOk);
        btnCancel.setWidth(50);
        contentPanel.addButton(btnCancel);
        contentPanel.setButtonAlign(BoxLayoutContainer.BoxLayoutPack.CENTER);
        contentPanel.setBodyStyle("padding: 6px");
        contentPanel.setWidth(250);

        centerLayoutContainer.add(contentPanel);
        centerLayoutContainer.setPixelSize(Window.getClientWidth(), Window.getClientHeight() - 80);
        centerLayoutContainer.setFlexibleHeight(-80);
        centerLayoutContainer.setBorders(true);

        setWidget(centerLayoutContainer);
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
