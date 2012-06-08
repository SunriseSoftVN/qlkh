/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.utils;

import com.google.gwt.core.client.GWT;
import com.qlkh.client.client.utils.i18n.ApplicationMessageConstants;

/**
 * The Utils Class using for get generic message of this application.
 * <p>
 * <b>Example:</b>
 * </p>
 * <p>
 * <b>RPC connect error.</b>
 * </p>
 * <p>
 * <b>Server shutdown.</b>
 * </p>
 *
 * @author dungvn3000
 * @since 6/17/11, 12:00 PM
 */
public final class ApplicationMessageUtils {

    private static ApplicationMessageConstants constants = GWT.create(ApplicationMessageConstants.class);

    private ApplicationMessageUtils() {
        // Hide it.
    }

    /**
     * Get generic of this application.
     *
     * @return
     */
    public static ApplicationMessageConstants getConstants() {
        return constants;
    }

}