/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.utils;

import com.qlkh.core.configuration.ConfigurationClientUtil;
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
        return ConfigurationClientUtil.getServerBaseUrl() + relativeDownloadUrl;
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
