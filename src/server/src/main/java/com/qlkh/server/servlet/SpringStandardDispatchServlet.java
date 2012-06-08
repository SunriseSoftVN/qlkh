/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.servlet;

import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.qlkh.core.configuration.ConfigurationServerUtil;
import com.qlkh.server.util.fixed.LegacySerializationPolicyFixed;
import net.customware.gwt.dispatch.server.Dispatch;
import net.customware.gwt.dispatch.server.standard.AbstractStandardDispatchServlet;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * The Class SpringStandardDispatchServlet.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 3:47 AM
 */
public class SpringStandardDispatchServlet extends AbstractStandardDispatchServlet {

    private Dispatch dispatch;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext ctx = WebApplicationContextUtils.
                getRequiredWebApplicationContext(config.getServletContext());
        dispatch = BeanFactoryUtils.beanOfType(ctx, Dispatch.class);
    }

    @Override
    protected Dispatch getDispatch() {
        return dispatch;
    }

    @Override
    protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL, String strongName) {
        if (ConfigurationServerUtil.isProductionMode()) {
            return super.doGetSerializationPolicy(request, moduleBaseURL, strongName);
        }
        return LegacySerializationPolicyFixed.getInstance();
    }
}
