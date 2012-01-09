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
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Collections;
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
        session.saveOrUpdate(entity);
        closeSession();
        return entity;
    }

    @Override
    public List<E> saveOrUpdate(List<E> entities) {
        if (CollectionUtils.isNotEmpty(entities)) {
            openSession();
            for (E entity : entities) {
                session.saveOrUpdate(entity);
            }
            closeSession();
        }
        return entities;
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
    public List<E> findByIds(Class<E> clazz, List<Long> ids) {
        openSession();
        Criteria criteria = session.createCriteria(clazz).add(Restrictions.in("id", ids));
        List<E> entities = criteria.list();
        if (CollectionUtils.isNotEmpty(entities)) {
            return entities;
        }
        closeSession();
        return Collections.emptyList();
    }

    @Override
    public List<E> getAll(Class<E> clazz) {
        openSession();
        Criteria criteria = session.createCriteria(clazz);
        List<E> result = criteria.list();
        closeSession();
        return result;
    }

    @Override
    public void deleteByIds(Class<E> clazz, List<Long> ids) {
        List<E> entities = findByIds(clazz, ids);
        if (CollectionUtils.isNotEmpty(entities)) {
            openSession();
            for (E entity : entities) {
                session.delete(entity);
            }
            closeSession();
        }
    }

    @Override
    public List<E> getByBeanConfig(Class<E> clazz, PagingLoadConfig config, Criterion... criterions) {
        openSession();
        Criteria criteria = session.createCriteria(clazz)
                .setFirstResult(config.getOffset()).setMaxResults(config.getLimit());
//        if (StringUtils.isNotBlank(config.getSortField())) {
//            if (config.getSortDir() == SortDir.ASC) {
//                criteria.addOrder(Order.asc(config.getSortField()));
//            } else if (config.getSortDir() == SortDir.DESC) {
//                criteria.addOrder(Order.desc(config.getSortField()));
//            }
//        }
        if (criterions != null && criterions.length > 0) {
            for (Criterion criterion : criterions) {
                criteria.add(criterion);
            }
        }
        List<E> result = criteria.list();
        closeSession();
        return result;
    }

    @Override
    public int count(Class<E> clazz) {
        openSession();
        Criteria criteria = session.createCriteria(clazz).setProjection(Projections.rowCount());
        Object result = criteria.uniqueResult();
        if (result != null) {
            return ((Long) result).intValue();
        }
        closeSession();
        return 0;
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
