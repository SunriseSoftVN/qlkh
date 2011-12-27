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

package com.qlvt.server.servlet.listener;

import com.allen_sauer.gwt.log.client.Log;
import com.mattbertolini.hermes.Hermes;
import com.qlvt.core.configuration.ApplicationConfiguration;
import com.qlvt.core.system.SystemUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

/**
 * The Class ApplicationContextListener.
 *
 * @author Nguyen Duc Dung
 * @since 8/16/11, 9:38 AM
 */
public class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //Register your config.
        try {
            //TODO it will remove in future when i found a general solution. @dungvn3000
            SystemUtil.setConfiguration(Hermes.get(ApplicationConfiguration.class, ""));
        } catch (IOException e) {
            Log.error("Can't find properties file.", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
