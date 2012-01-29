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

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.qlvt.client.client.service.UserService;
import com.qlvt.core.client.exception.CodeExistException;
import com.qlvt.core.client.model.User;
import com.qlvt.server.dao.UserDao;
import com.qlvt.server.service.core.AbstractService;
import com.qlvt.server.transaction.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

/**
 * The Class UserServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 5:17 PM
 */
@Transaction
@Singleton
public class UserServiceImpl extends AbstractService implements UserService {

    @Inject
    private UserDao userDao;

    @Override
    public BasePagingLoadResult<User> getUsersForGrid(BasePagingLoadConfig config) {
        return userDao.getByBeanConfig(User.class, config);
    }

    @Override
    public void deleteUserById(long userId) {
        userDao.deleteById(User.class, userId);
    }

    @Override
    public void deleteUserByIds(List<Long> userIds) {
        userDao.deleteByIds(User.class, userIds);
    }

    @Override
    public void updateUser(User user) throws CodeExistException {
        try {
            userDao.saveOrUpdate(user);
        } catch (ConstraintViolationException ex) {
            throw new CodeExistException(ex.getMessage());
        }
    }

    @Override
    public void updateUsers(List<User> users) {
        userDao.saveOrUpdate(users);
    }
}
