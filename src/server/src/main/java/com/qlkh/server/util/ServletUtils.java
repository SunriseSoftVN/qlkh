/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * The Class ServletUtils.
 *
 * @author Nguyen Duc Dung
 * @since 3/6/12, 6:06 PM
 */
public final class ServletUtils {

    private static final String SPE_PATH = "/";
    private static final String CLASS_PATH = "WEB-INF/classes/";

    private ServletUtils() {

    }

    public static ServletUtils getInstance() {
        return new ServletUtils();
    }

    public String getRealPath(String dirName, String fileName) {
        return new StringBuilder().append(SPE_PATH)
                .append(this.getClass()
                        .getResource(SPE_PATH).getPath()
                        .replaceFirst(SPE_PATH, StringUtils.EMPTY).
                                replace(CLASS_PATH, dirName + SPE_PATH)).append(fileName).toString();
    }

    public String getBaseUrl(HttpServletRequest request) {
        if ((request.getServerPort() == 80) ||
                (request.getServerPort() == 443))
            return request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        else
            return request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
