/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.utils;

/**
 * The Class BrowserUtils.
 *
 * @author Nguyen Duc Dung
 * @since 5/27/12, 3:53 PM
 */
public final class BrowserUtils {

    private BrowserUtils() {

    }

    public static native boolean is_chrome() /*-{
        var is_chrome = /chrome/.test(navigator.userAgent.toLowerCase());
        return is_chrome;
    }-*/;

    public static native boolean is_firefox() /*-{
        return navigator.userAgent.indexOf("Firefox") != -1;
    }-*/;

    public static native boolean is_Java_Enable() /*-{
//        return $wnd.deployJava.getJREs().length > 0;
        return false;
    }-*/;
}
