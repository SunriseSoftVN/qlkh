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
