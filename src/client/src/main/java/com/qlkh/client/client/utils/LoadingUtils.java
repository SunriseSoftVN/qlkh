/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.utils;

import com.google.gwt.user.client.ui.RootPanel;

/**
 * The Class LoadingUtils.
 *
 * @author Nguyen Duc Dung
 * @since 9/15/11, 6:26 PM
 */
public final class LoadingUtils {

    private static final String LOADING_PANEL = "loading_panel";

    private LoadingUtils() {
        //Hide it
    }

    /**
     * Hide loading panel.
     */
    public static void hideLoading() {
        RootPanel.get(LOADING_PANEL).setVisible(false);
    }

    /**
     * Show Loading panel.
     */
    public static void showLoading() {
        RootPanel.get(LOADING_PANEL).setVisible(true);
    }

}
