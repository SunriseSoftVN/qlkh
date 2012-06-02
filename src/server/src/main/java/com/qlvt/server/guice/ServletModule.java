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

import com.google.inject.matcher.Matchers;
import com.qlvt.core.system.SystemUtil;
import com.qlvt.server.service.*;
import com.qlvt.server.servlet.ReportServlet;
import com.qlvt.server.transaction.Transaction;
import com.qlvt.server.transaction.TransactionInterceptor;
import com.qlvt.server.transaction.TransactionMethodMatcher;

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
        serve(servletRootPath + "/login").with(LoginServiceImpl.class);
        serve(servletRootPath + "/user").with(UserServiceImpl.class);
        serve(servletRootPath + "/station").with(StationServiceImpl.class);
        serve(servletRootPath + "/task").with(TaskServiceImpl.class);
        serve(servletRootPath + "/taskDetail").with(TaskDetailServiceImpl.class);
        serve(servletRootPath + "/branch").with(BranchServiceImpl.class);
        serve(servletRootPath + "/reportService").with(ReportServiceImpl.class);
        serve(servletRootPath + "/report").with(ReportServlet.class);
        bindInterceptor(Matchers.annotatedWith(Transaction.class), new TransactionMethodMatcher(),
                new TransactionInterceptor());
    }
}
