/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.core;

import com.qlkh.core.client.model.core.AbstractEntity;
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

    <E extends AbstractEntity> List<E> saveOrUpdate(List<E> entities);

    <E extends AbstractEntity> void delete(E entity);

    <E extends AbstractEntity> void deleteById(String entityName, long id);

    <E extends AbstractEntity> void deleteById(Class<E> entityClass, long id);

    <E extends AbstractEntity> void deleteByIds(String entityName, List<Long> ids);

    <E extends AbstractEntity> void deleteByIds(Class<E> entityClass, List<Long> ids);

    <E extends AbstractEntity> List<E> findRelateEntityById(String entityName, long id, String relateEntity);
}
