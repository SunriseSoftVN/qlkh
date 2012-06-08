/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.core.AbstractEntity;
import com.qlkh.server.dao.core.AbstractDao;
import com.qlkh.server.dao.core.GeneralDao;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
 * The Class GeneralDao.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 11:42 AM
 */
@SuppressWarnings("unchecked")
public class GeneralDaoImpl extends AbstractDao implements GeneralDao {

    @Override
    public <E extends AbstractEntity> List<E> findRelateEntityById(String entityName, long id, String relateEntity) {
        Type entityType = getSessionFactory().getTypeHelper().entity(entityName);
        Type relateEntityType = getSessionFactory().getTypeHelper().entity(relateEntity);
        if (entityType != null && relateEntityType != null) {
            Class<?> entityClass = entityType.getReturnedClass();
            Class<?> relateEntityClass = relateEntityType.getReturnedClass();

            //Find entity class field name inside relate entity.
            String fieldName = null;
            for (Field field : relateEntityClass.getDeclaredFields()) {
                if(field.getType() == entityClass) {
                    fieldName = field.getName();
                    break;
                }
            }
            if (fieldName != null) {
                DetachedCriteria criteria = DetachedCriteria.forEntityName(relateEntity);
                criteria.add(Restrictions.eq(fieldName + ".id", id));
                return getHibernateTemplate().findByCriteria(criteria);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public <E extends AbstractEntity> void deleteByIds(String entityName, List<Long> ids) {
        for (Long id : ids) {
            if (id != null) {
                deleteById(entityName, id);
            }
        }
    }

    @Override
    public <E extends AbstractEntity> void deleteByIds(Class<E> entityClass, List<Long> ids) {
        for (Long id : ids) {
            if (id != null) {
                deleteById(entityClass, id);
            }
        }
    }

    @Override
    public <E extends AbstractEntity> void deleteById(String entityName, long id) {
        Object entity = getHibernateTemplate().get(entityName, id);
        if (entity != null) {
            getHibernateTemplate().delete(entity);
        }
    }

    @Override
    public <E extends AbstractEntity> void deleteById(Class<E> entityClass, long id) {
        Object entity = getHibernateTemplate().get(entityClass, id);
        if (entity != null) {
            getHibernateTemplate().delete(entity);
        }
    }

    @Override
    public <E extends AbstractEntity> void delete(E entity) {
        getHibernateTemplate().delete(entity);
    }

    @Override
    public <E extends AbstractEntity> E findById(String entityName, long id) {
        return (E) getHibernateTemplate().get(entityName, id);
    }

    @Override
    public <E extends AbstractEntity> E findById(Class<E> entityClass, long id) {
        return getHibernateTemplate().get(entityClass, id);
    }

    @Override
    public <E extends AbstractEntity> List<E> findCriteria(String entityName, Criterion... criterions) {
        DetachedCriteria criteria = DetachedCriteria.forEntityName(entityName);
        for (Criterion criterion: criterions) {
            if (criterion != null) {
                criteria.add(criterion);
            }
        }
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public <E extends AbstractEntity> List<E> findCriteria(Class<E> entityClass, Criterion... criterions) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
        for (Criterion criterion: criterions) {
            if (criterion != null) {
                criteria.add(criterion);
            }
        }
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public <E extends AbstractEntity> List<E> getAll(Class<E> entityClass, Order... orders) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
        for(Order order : orders) {
            if (order != null) {
                criteria.addOrder(order);
            }
        }
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public <E extends AbstractEntity> List<E> getAll(Class<E> entityClass) {
        return getHibernateTemplate().loadAll(entityClass);
    }

    @Override
    public <E extends AbstractEntity> List<E> getAll(String entityName) {
        Type type = getSessionFactory().getTypeHelper().entity(entityName);
        if (type != null) {
            return getHibernateTemplate().loadAll(type.getReturnedClass());
        }
        return null;
    }

    @Override
    public <E extends AbstractEntity> E saveOrUpdate(E entity) {
        getHibernateTemplate().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public <E extends AbstractEntity> List<E> saveOrUpdate(List<E> entities) {
        getHibernateTemplate().saveOrUpdateAll(entities);
        return entities;
    }
}
