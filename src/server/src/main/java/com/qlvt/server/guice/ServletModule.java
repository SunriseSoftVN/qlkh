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

package com.qlvt.server.guice;

import com.qlvt.core.system.SystemUtil;
import com.qlvt.server.service.*;

/**
 * The Class ServletModule.
 *
 * @author Nguyen Duc Dung
 * @since 8/16/11, 9:39 AM
 */
public class ServletModule extends com.google.inject.servlet.ServletModule {
    @Override
    protected void configureServlets() {
        String servletRootPath = SystemUtil.getConfiguration().serverServletRootPath();
        serve(servletRootPath + "/Login").with(LoginServiceImpl.class);
        serve(servletRootPath + "/User").with(UserServiceImpl.class);
        serve(servletRootPath + "/Station").with(StationServiceImpl.class);
        serve(servletRootPath + "/Task").with(TaskServiceImpl.class);
        serve(servletRootPath + "/TaskDetail").with(TaskDetailServiceImpl.class);
        serve(servletRootPath + "/Branch").with(BranchServiceImpl.class);
    }
}
