/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.configuration;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * The Class ApplicationConfiguration.
 *
 * @author Nguyen Duc Dung
 * @since 8/24/11, 7:47 PM
 */
public interface ApplicationConfiguration extends ConstantsWithLookup {

    /**
     * PRODUCTION_MODE
     */
    public static final String PRODUCTION_MODE = "PRODUCTION";

    /**
     * DEVELOPMENT_MODE
     */
    public static final String DEVELOPMENT_MODE = "DEV";

    String applicationName();
    String applicationMode();
    String applicationTitle();
    String applicationVersion();
    String developmentModeClientBaseUrl();
    String developmentModeClientProxyPath();
    String developmentModeServerBaseUrl();
    String developmentModeServerHostName();
    String developmentModeServerPort();
    String productionModeServerBaseUrl();
    String serverServletRootPath();
}
