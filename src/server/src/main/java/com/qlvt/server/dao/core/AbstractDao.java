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

package com.qlvt.server.dao.core;

import com.qlvt.core.client.model.core.AbstractEntity;
import com.qlvt.server.util.SessionFactoryUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;

import java.util.List;

/**
 * The Class AbstractDao.
 *
 * @author Nguyen Duc Dung
 * @since 8/16/11, 10:34 AM
 */
public abstract class AbstractDao<E extends AbstractEntity> implements Dao<E> {

    protected Session session;

    @Override
    public E saveOrUpdate(E entity) {
        assert entity != null;
        openSession();
        session.save(entity);
        closeSession();
        return entity;
    }

    @Override
    public void delete(E entity) {
        assert entity != null;
        openSession();
        session.delete(entity);
        closeSession();
    }

    @Override
    public void deleteById(Class<E> clazz, long id) {
        openSession();
        session.delete(session.get(clazz, id));
        closeSession();
    }

    @Override
    public E findById(Class<E> clazz, long id) {
        openSession();
        E entity = (E) session.get(clazz, id);
        closeSession();
        return entity;
    }

    @Override
    public List<E> getAll(Class<E> clazz) {
        openSession();
        Criteria criteria = session.createCriteria(clazz);
        List<E> result = criteria.list();
        closeSession();
        return result;
    }

    protected void openSession() {
        session = SessionFactoryUtil.getInstance().openSession();
        session.beginTransaction();
    }

    protected void closeSession() {
        session.flush();
        session.close();
    }
}
