/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.main.view;

import com.extjs.gxt.ui.client.widget.Html;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.core.configuration.ConfigurationClientUtil;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

/**
 * The Class BannerView.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 8:57 AM
 */
@View(parentDomId = DomIdConstant.BANNER_PANEL)
public class BannerView extends AbstractView {
    @Override
    protected void initializeView() {
        setWidget(new Html("<h3 style='color:white'>" +
                ConfigurationClientUtil.getConfiguration().applicationTitle() +
                " " +
                ConfigurationClientUtil.getConfiguration().applicationVersion() +
                "<h3>"));
    }
}
