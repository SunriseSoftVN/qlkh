/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.utils;

import com.qlkh.core.configuration.ConfigurationClientUtil;

/**
 * The Class ServiceUtils.
 *
 * @author Nguyen Duc Dung
 * @since 12/23/11, 9:34 AM
 */
public class ServiceUtils {

    private ServiceUtils() {

    }

    public static String getServiceEntryPoint() {
        if (ConfigurationClientUtil.isProductionMode()) {
            return ConfigurationClientUtil.getServerBaseUrl()
                    + ConfigurationClientUtil.getConfiguration().serverServletRootPath();
        }
        //Set proxy servlet for development mode, to split up gwt server and gwt client to 2 projects.
        return ConfigurationClientUtil.getConfiguration().developmentModeClientBaseUrl()
                + ConfigurationClientUtil.getConfiguration().developmentModeClientProxyPath();
    }
}