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

package com.qlvt.client.client.utils;

import com.qlvt.core.system.SystemUtil;
import com.smvp4g.mvp.client.core.utils.StringUtils;

/**
 * The Class UrlUtils.
 *
 * @author Nguyen Duc Dung
 * @since 9/12/11, 3:21 PM
 */
public final class UrlUtils {

    private static final String HISTORY_TOKEN_CHAR = "#";

    private UrlUtils() {
        //Hide it
    }

    /**
     * Return url base on application mode
     *
     * @param relativeDownloadUrl
     * @return
     */
    public static String buildApplicationDownloadUrl(String relativeDownloadUrl) {
        return SystemUtil.getServerBaseUrl() + relativeDownloadUrl;
    }

    /**
     * Remove history token for a gwt url.
     *
     * @param url
     * @return
     */
    public static String removeHistoryToken(String url) {
        if (StringUtils.isNotBlank(url)) {
            int index = url.indexOf(HISTORY_TOKEN_CHAR);
            if (index > 0) {
                url = url.substring(0 , index);
            }
        }
        return url;
    }

}
