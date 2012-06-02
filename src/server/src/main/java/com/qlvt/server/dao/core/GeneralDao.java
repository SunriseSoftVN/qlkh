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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import java.util.List;

/**
 * The Class GeneralDao.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 11:51 AM
 */
public interface GeneralDao extends Dao {

    <E extends AbstractEntity> List<E> getAll(String entityName);

    <E extends AbstractEntity> List<E> getAll(Class<E> entityClass);

    <E extends AbstractEntity> List<E> getAll(Class<E> entityClass, Order... orders);

    <E extends AbstractEntity> E findById(String entityName, long id);

    <E extends AbstractEntity> List<E> findCriteria(Class<E> entityClass, Criterion... criterion);

    <E extends AbstractEntity> List<E> findCriteria(String entityName, Criterion... criterion);

    <E extends AbstractEntity> E findById(Class<E> entityClass, long id);

    <E extends AbstractEntity> E saveOrUpdate(E entity);

    <E extends AbstractEntity> void saveOrUpdate(List<E> entities);

    <E extends AbstractEntity> void delete(E entity);

    <E extends AbstractEntity> void deleteById(String entityName, long id);

    <E extends AbstractEntity> void deleteById(Class<E> entityClass, long id);

    <E extends AbstractEntity> void deleteByIds(String entityName, List<Long> ids);

    <E extends AbstractEntity> List<E> findRelateEntityById(String entityName, long id, String relateEntity);
}
