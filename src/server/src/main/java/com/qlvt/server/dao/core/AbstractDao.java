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

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlvt.core.client.model.core.AbstractEntity;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The Class AbstractDao.
 *
 * @author Nguyen Duc Dung
 * @since 8/16/11, 10:34 AM
 */
public abstract class AbstractDao<E extends AbstractEntity> extends HibernateDaoSupport implements Dao<E> {

    @Override
    public E saveOrUpdate(E entity) {
        assert entity != null;
        getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public List<E> saveOrUpdate(List<E> entities) {
        if (CollectionUtils.isNotEmpty(entities)) {
            for (E entity : entities) {
                getCurrentSession().saveOrUpdate(entity);
            }
        }
        return entities;
    }

    @Override
    public void delete(E entity) {
        assert entity != null;
        getCurrentSession().delete(entity);
    }

    @Override
    public void deleteById(Class<E> clazz, long id) {
        getCurrentSession().delete(getCurrentSession().get(clazz, id));
    }

    @Override
    public E findById(Class<E> clazz, long id) {
        E entity = (E) getCurrentSession().get(clazz, id);
        return entity;
    }

    @Override
    public List<E> findByIds(Class<E> clazz, List<Long> ids) {
        Criteria criteria = getCurrentSession().createCriteria(clazz).add(Restrictions.in("id", ids));
        List<E> entities = criteria.list();
        if (CollectionUtils.isNotEmpty(entities)) {
            return entities;
        }
        return Collections.emptyList();
    }

    @Override
    public List<E> getAll(Class<E> clazz) {
        Criteria criteria = getCurrentSession().createCriteria(clazz);
        List<E> result = criteria.list();
        return result;
    }

    @Override
    public void deleteByIds(Class<E> clazz, List<Long> ids) {
        List<E> entities = findByIds(clazz, ids);
        if (CollectionUtils.isNotEmpty(entities)) {
            for (E entity : entities) {
                getCurrentSession().delete(entity);
            }
        }
    }

    @Override
    public BasePagingLoadResult<E> getByBeanConfig(Class<E> clazz, BasePagingLoadConfig config, Criterion... criterions) {
        Criteria criteria = getCurrentSession().createCriteria(clazz);
        if (StringUtils.isNotBlank(config.getSortField())) {
            if (config.getSortDir() == Style.SortDir.ASC) {
                criteria.addOrder(Order.asc(config.getSortField()));
            } else if (config.getSortDir() == Style.SortDir.DESC) {
                criteria.addOrder(Order.desc(config.getSortField()));
            }
        }
        if (config.get("hasFilter") != null && (Boolean) config.get("hasFilter")) {
            Map<String, Object> filters = config.get("filters");
            if (filters != null) {
                Criterion criterion = null;
                String joinEntityName = null;
                for (String filter : filters.keySet()) {
                    if (joinEntityName == null 
                            || !joinEntityName.equals(getRootEntityName(filter))) {
                        joinEntityName = getRootEntityName(filter);
                        if (StringUtils.isNotEmpty(joinEntityName)) {
                            criteria.createAlias(joinEntityName, joinEntityName);
                        }
                    }
                    Criterion likeCriterion;
                    Object filterValue = filters.get(filter);
                    if (filterValue instanceof String) {
                        likeCriterion = Restrictions.like(filter, ((String) filterValue).trim(), MatchMode.ANYWHERE).ignoreCase();
                    } else {
                        likeCriterion = Restrictions.like(filter, filterValue);
                    }
                    if (criterion == null) {
                        criterion = likeCriterion;
                    } else {
                        criterion = Restrictions.or(criterion, likeCriterion);
                    }
                }
                if (criterion != null) {
                    criteria.add(criterion);
                }
            }
        }
        if (criterions != null && criterions.length > 0) {
            for (Criterion criterion : criterions) {
                criteria.add(criterion);
            }
        }
        List<E> result = criteria.setFirstResult(config.getOffset()).
                setMaxResults(config.getLimit()).list();
        return new BasePagingLoadResult<E>(result, config.getOffset(), count(clazz, config, criterions));
    }

    @Override
    public int count(Class<E> clazz) {
        Criteria criteria = getCurrentSession().createCriteria(clazz).setProjection(Projections.rowCount());
        Object result = criteria.uniqueResult();
        if (result != null) {
            return ((Long) result).intValue();
        }
        return 0;
    }

    protected int count(Class<E> clazz, BasePagingLoadConfig config, Criterion... criterions) {
        Criteria criteria = getCurrentSession().createCriteria(clazz).setProjection(Projections.rowCount());
        if (config.get("hasFilter") != null && (Boolean) config.get("hasFilter")) {
            Map<String, Object> filters = config.get("filters");
            if (filters != null) {
                Criterion criterion = null;
                String joinEntityName = null;
                for (String filter : filters.keySet()) {
                    if (joinEntityName == null 
                            || !joinEntityName.equals(getRootEntityName(filter))) {
                        joinEntityName = getRootEntityName(filter);
                        if (StringUtils.isNotEmpty(joinEntityName)) {
                            criteria.createAlias(joinEntityName, joinEntityName);
                        }
                    }
                    Criterion likeCriterion;
                    Object filterValue = filters.get(filter);
                    if (filterValue instanceof String) {
                        likeCriterion = Restrictions.like(filter, ((String) filterValue).trim(), MatchMode.ANYWHERE).ignoreCase();
                    } else {
                        likeCriterion = Restrictions.like(filter, filterValue);
                    }
                    if (criterion == null) {
                        criterion = likeCriterion;
                    } else {
                        criterion = Restrictions.or(criterion, likeCriterion);
                    }
                }
                if (criterion != null) {
                    criteria.add(criterion);
                }
            }
        }
        if (criterions != null && criterions.length > 0) {
            for (Criterion criterion : criterions) {
                criteria.add(criterion);
            }
        }
        Object result = criteria.uniqueResult();
        if (result != null) {
            return ((Long) result).intValue();
        }
        return 0;
    }

    protected String getRootEntityName(String path) {
        String entityName = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(path)) {
            int index = path.indexOf(".");
            if (index > 0) {
                entityName = path.substring(0, index);
            }
        }
        return entityName;
    }

    protected Session getCurrentSession() {
        return getSession();
    }
}
