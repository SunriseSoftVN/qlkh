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
import org.hibernate.criterion.*;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

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
    public <E extends AbstractEntity> BasePagingLoadResult<E> getByBeanConfig(String entityName, BasePagingLoadConfig config, Criterion... criterions) {
        DetachedCriteria criteria = buildCriteria(entityName, config, criterions);
        List<E> result = getHibernateTemplate().findByCriteria(criteria, config.getOffset(), config.getLimit());
        return new BasePagingLoadResult<E>(result, config.getOffset(), count(entityName, config, criterions));
    }

    private int count(String entityName, BasePagingLoadConfig config, Criterion... criterions) {
        DetachedCriteria criteria = buildCriteria(entityName, config, criterions);
        criteria.setProjection(Projections.rowCount());
        List result = getHibernateTemplate().findByCriteria(criteria);
        if (result != null && result.size() > 0) {
            return ((Long) result.get(0)).intValue();
        }
        return 0;
    }

    private DetachedCriteria buildCriteria(String entityName, BasePagingLoadConfig config, Criterion... criterions) {
        DetachedCriteria criteria = DetachedCriteria.forEntityName(entityName);
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
                if (criterion != null) {
                    criteria.add(criterion);
                }
            }
        }
        return criteria;
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
}
