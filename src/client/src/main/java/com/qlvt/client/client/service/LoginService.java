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

package com.qlvt.client.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.qlvt.client.client.utils.ServiceUtils;
import com.qlvt.core.client.exception.UserAuthenticationException;
import com.qlvt.core.client.model.User;
import com.smvp4g.mvp.client.core.service.RemoteService;

/**
 * The Class LoginService.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 10:13 AM
 */
@RemoteServiceRelativePath("Login")
public interface LoginService extends RemoteService<LoginService> {

    User checkLogin(String userName, String passWord) throws UserAuthenticationException;

    public static class App {
        private static final LoginServiceAsync ourInstance = (LoginServiceAsync) GWT.create(LoginService.class);

        public static LoginServiceAsync getInstance() {
            ServiceUtils.configureServiceEntryPoint(LoginService.class, ourInstance);
            return ourInstance;
        }
    }
}
