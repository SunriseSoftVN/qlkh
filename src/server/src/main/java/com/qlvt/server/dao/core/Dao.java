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

import java.util.List;

/**
 * The Class IDao.
 *
 * @author Nguyen Duc Dung
 * @since 8/16/11, 10:35 AM
 */
public interface Dao<E extends AbstractEntity> {
    /**
     * Save or Update a entity.
     *
     * @param entity
     * @return
     */
    E saveOrUpdate(E entity);

    /**
     * Save or Update entities.
     * @param entities
     * @return
     */
    List<E> saveOrUpdate(List<E> entities);

    /**
     * Delete a entity.
     *
     * @param entity
     */
    void delete(E entity);

    /**
     * Delete a entity by entity id.
     *
     * @param clazz
     * @param id
     */
    void deleteById(Class<E> clazz, long id);

    /**
     * Delete a entity by entity ids.
     *
     * @param clazz
     * @param ids
     */
    void deleteByIds(Class<E> clazz, List<Long> ids);

    /**
     * Find a entity by id.
     *
     * @param clazz
     * @param id
     * @return
     */
    E findById(Class<E> clazz, long id);

    /**
     * Find a entity by id.
     *
     * @param clazz
     * @param ids
     * @return
     */
    List<E> findByIds(Class<E> clazz, List<Long> ids);

    /**
     * Get all entity in data store.
     *
     * @param clazz
     * @return
     */
    List<E> getAll(Class<E> clazz);
}
