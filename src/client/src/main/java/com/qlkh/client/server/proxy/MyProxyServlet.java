/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
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
