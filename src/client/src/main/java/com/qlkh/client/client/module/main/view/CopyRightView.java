/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.main.view;

import com.google.gwt.user.client.ui.HTML;
import com.qlkh.client.client.constant.DomIdConstant;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

@View(parentDomId = DomIdConstant.BOTTOM_PANEL)
public class CopyRightView extends AbstractView {
    @Override
    protected void initializeView() {
        HTML html = new HTML("<p style='text-align:center'>" +
                "© Copyright 2012 <a href='http://www.sunrisesoft.vn/'>Công ty TNHH SunriseSoft</a> " +
                "-Phone: <a href='#'>08.62896162</a> - Hotline: <a href='#'>0905611699</a> gặp Dũng - Email góp ý: <a href='mailto:dungvn3000@gmail.com'>dungvn3000@gmail.com</a></p>");
        setWidget(html);
    }
}
