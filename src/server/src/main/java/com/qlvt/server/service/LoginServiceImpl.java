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

package com.qlvt.server.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.qlvt.client.client.service.LoginService;
import com.qlvt.core.client.exception.UserAuthenticationException;
import com.qlvt.core.client.model.User;
import com.qlvt.server.dao.UserDao;
import com.qlvt.server.service.core.AbstractService;
import com.qlvt.server.transaction.Transaction;

/**
 * The Class LoginServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 10:18 AM
 */
@Singleton
@Transaction
public class LoginServiceImpl extends AbstractService implements LoginService {

    @Inject
    private UserDao userDao;

    @Override
    public User checkLogin(String userName, String passWord) throws UserAuthenticationException {
        User user = userDao.findByUserName(userName);
        if (user != null && user.getPassWord().equals(passWord)) {
            return user;
        } else {
            throw new UserAuthenticationException("Username or Password is wrong");
        }
    }
}
