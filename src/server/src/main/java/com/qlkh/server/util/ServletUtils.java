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
        return new StringBuilder().append(SPE_PATH).append(this.getClass().getResource(SPE_PATH).getPath().replaceFirst(SPE_PATH, StringUtils.EMPTY).
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
