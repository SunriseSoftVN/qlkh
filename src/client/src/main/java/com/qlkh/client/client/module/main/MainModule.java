/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.main;

import com.google.gwt.user.client.ui.RootPanel;
import com.smvp4g.mvp.client.core.module.AbstractModule;
import com.smvp4g.mvp.client.core.module.annotation.Module;

/**
 * The Class MainModule.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 8:49 AM
 */
@Module
public class MainModule extends AbstractModule {
    @Override
    public void configure() {
    }

    @Override
    public void start() {
        RootPanel.get("loading_panel").setVisible(false);
    }
}
