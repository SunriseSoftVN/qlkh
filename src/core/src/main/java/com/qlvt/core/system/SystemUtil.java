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

package com.qlvt.core.system;

import com.google.gwt.core.client.GWT;
import com.qlvt.core.configuration.ApplicationConfiguration;

/**
 * The Class SystemUtil.
 *<p>
 * <b>Note:</b>
 * This class will be shared between client and server.
 *</p>
 * @author Nguyen Duc Dung
 * @since 8/24/11, 7:50 PM
 */
public final class SystemUtil {

    private static ApplicationConfiguration configuration;

    public SystemUtil() {
        //Hide it.
    }

    /**
     * TODO using for sever side to set configuration.It will remove in the future @dungvn3000
     *  Set application configurations.
     * @param configuration
     */
    @Deprecated
    public static void setConfiguration(ApplicationConfiguration configuration) {
        SystemUtil.configuration = configuration;
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
     * @return
     */
    public static String getServerBaseUrl() {
        if (isProductionMode()) {
            return getConfiguration().productionModeServerBaseUrl();
        }
        return getConfiguration().developmentModeServerBaseUrl();
    }
}
