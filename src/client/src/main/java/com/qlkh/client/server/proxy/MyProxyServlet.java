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

package com.qlkh.client.server.proxy;

import com.qlkh.core.configuration.ConfigurationServerUtil;

import javax.servlet.ServletConfig;

/**
 * The Class MyProxyServlet.
 *
 * @author Nguyen Duc Dung
 * @since 8/16/11, 9:58 AM
 */
public class MyProxyServlet extends ProxyServlet {

    @Override
    public void init(ServletConfig servletConfig) {
        setProxyHost(ConfigurationServerUtil.getConfiguration().developmentModeServerHostName());
        setFollowRedirects(false);
        setRemovePrefix(true);
        setProxyPort(Integer.parseInt(ConfigurationServerUtil.getConfiguration().developmentModeServerPort()));
    }
}
