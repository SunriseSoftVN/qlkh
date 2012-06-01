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

import com.qlvt.core.client.model.core.AbstractEntity;
import com.qlvt.server.dao.core.GeneralDao;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

/**
 * The Class GeneralDao.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 11:42 AM
 */
public class GeneralDaoImpl extends HibernateDaoSupport implements GeneralDao {

    @SuppressWarnings("unchecked")
    @Override
    public <E extends AbstractEntity> E findById(String entityName, long id) {
        return (E) getHibernateTemplate().get(entityName, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends AbstractEntity> List<E> getAll(String entityName) {
        Type type = getSessionFactory().getTypeHelper().entity(entityName);
        if (type != null) {
            return getHibernateTemplate().loadAll(type.getReturnedClass());
        }
        return null;
    }
}
