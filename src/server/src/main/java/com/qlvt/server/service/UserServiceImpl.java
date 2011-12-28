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
import com.qlvt.core.client.model.User;
import com.qlvt.server.dao.UserDao;
import com.qlvt.server.service.core.AbstractService;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class UserServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 5:17 PM
 */
@Singleton
public class UserServiceImpl extends AbstractService implements UserService {

    @Inject
    private UserDao userDao;

    @Override
    public BasePagingLoadResult<List<User>> getUsersForGrid(BasePagingLoadConfig config) {
        List<User> users = userDao.getAll(User.class);

        ArrayList<User> subList = new ArrayList<User>();
        int start = config.getOffset();
        int limit = users.size();
        if (config.getLimit() > 0) {
            limit = Math.min(start + config.getLimit(), limit);
        }
        for (int i = config.getOffset(); i < limit; i++) {
            subList.add(users.get(i));
        }

        return new BasePagingLoadResult(subList, config.getOffset(), users.size());
    }
}