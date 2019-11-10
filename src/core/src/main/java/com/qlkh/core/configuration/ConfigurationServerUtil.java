/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
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
