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

import com.mattbertolini.hermes.Hermes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The Class SystemUtil for server.
 * <p>
 * <b>Note:</b>
 * This class is only work on sever side.
 * </p>
 *
 * @author Nguyen Duc Dung
 * @since 8/24/11, 7:50 PM
 */
public final class ConfigurationServerUtil {

    private static Logger logger = LoggerFactory.getLogger(ConfigurationServerUtil.class);

    private static ApplicationConfiguration configuration;

    static {
        try {
            configuration = Hermes.get(ApplicationConfiguration.class, "");
        } catch (IOException e) {
            logger.error("Can't find properties file.", e);
        }
    }

    private ConfigurationServerUtil() {
        //Hide it.
    }

    /**
     * Get application configurations.
     *
     * @return
     */
    public static ApplicationConfiguration getConfiguration() {
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
