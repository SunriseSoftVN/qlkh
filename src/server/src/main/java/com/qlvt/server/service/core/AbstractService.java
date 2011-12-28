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

package com.qlvt.server.service.core;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.qlvt.core.system.SystemUtil;
import com.qlvt.server.util.fixed.LegacySerializationPolicyFixed;

import javax.servlet.http.HttpServletRequest;

/**
 * The Class AbstractService.
 *
 * @author Nguyen Duc Dung
 * @since 8/16/11, 10:33 AM
 */
public class AbstractService extends RemoteServiceServlet {
    @Override
    protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL, String strongName) {
        if (SystemUtil.isProductionMode()) {
            return super.doGetSerializationPolicy(request, moduleBaseURL, strongName);
        }
        return LegacySerializationPolicyFixed.getInstance();
    }
}