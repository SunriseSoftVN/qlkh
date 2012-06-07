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

package com.qlvt.server.servlet;

import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.qlvt.core.configuration.ConfigurationServerUtil;
import com.qlvt.server.util.fixed.LegacySerializationPolicyFixed;
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
