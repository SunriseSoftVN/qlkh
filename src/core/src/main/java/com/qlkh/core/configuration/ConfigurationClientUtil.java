/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.configuration;

import com.google.gwt.core.client.GWT;

/**
 * The Class SystemUtil for Client.
 * <p>
 * <b>Note:</b>
 * This class is only work on client side.
 * </p>
 *
 * @author Nguyen Duc Dung
 * @since 8/24/11, 7:50 PM
 */
public final class ConfigurationClientUtil {

    private static ApplicationConfiguration configuration;


    private ConfigurationClientUtil() {
        //Hide it.
    }

    /**
     * Get application configurations.
     *
     * @return
     */
    public static ApplicationConfiguration getConfiguration() {
        if (configuration == null && GWT.isClient()) {
            configuration = GWT.create(ApplicationConfiguration.class);
        }
        return configuration;
    }

    /**
     * Check application is being in production mode or not.
     *
     * @return true, if it's being in.
     */
    public static boolean isProductionMode() {
        return getConfiguration() != null && ApplicationConfiguration.PRODUCTION_MODE
                .equals(getConfiguration().applicationMode());
    }

    /**
     * Return Server base url
     *
     * @return
     */
    public static String getServerBaseUrl() {
        if (isProductionMode()) {
            return getConfiguration().productionModeServerBaseUrl();
        }
        return getConfiguration().developmentModeServerBaseUrl();
    }

}
