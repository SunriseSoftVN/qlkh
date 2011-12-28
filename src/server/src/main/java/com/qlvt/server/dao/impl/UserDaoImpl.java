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

package com.qlvt.server.dao.impl;

import com.google.inject.Singleton;
import com.qlvt.core.client.model.User;
import com.qlvt.server.dao.UserDao;
import com.qlvt.server.dao.core.AbstractDao;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The Class UserDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 12/27/11, 7:06 PM
 */
@Singleton
public class UserDaoImpl extends AbstractDao<User> implements UserDao {
    @Override
    public User findByUserName(String userName) {
        openSession();
        Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("userName", userName));
        List<User> users = criteria.list();
        closeSession();
        if (CollectionsUtils.isNotEmpty(users)) {
            return users.get(0);
        }
        return null;
    }
}
