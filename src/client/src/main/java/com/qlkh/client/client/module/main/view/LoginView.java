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
import com.google.gwt.user.client.ui.HTML;
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

    private Html downloadChromeOrFireFoxHtml = new Html(getConstant().downloadChromeOrFireFox());

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

        if (!BrowserUtils.is_chrome() && !BrowserUtils.is_firefox()) {
            contentPanel.add(downloadChromeOrFireFoxHtml);
        }

        contentPanel.add(new HTML("<div>" +
                "<p><b>Cập nhật phiên bản 1.4.2 - 31/07/2014</b></p>" +
                "<ul>" +
                "<li>- Tính năng copy dữ liệu của giá vật tư xẻ copy đè dữ liệu nếu giá của quý trước có thay đổi.</li>" +
                "</ul>" +
                "<p><b>Cập nhật phiên bản 1.4.1 - 07/02/2014</b></p>" +
                "<ul>" +
                "<li>- Sửa lỗi sai định dạng số trong Excel.</li>" +
                "</ul>" +
                "<p><b>Cập nhật phiên bản 1.4.0</b></p>" +
                "<ul>" +
                "<li>- User xí nghiệp có thể xem định mức cho từng công việc.</li>" +
                "<li>- Số chứng từ hoá đơn xẽ tự động reset về 1 khi chuyển sang năm mới.</li>" +
                "</ul></div>"));

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
