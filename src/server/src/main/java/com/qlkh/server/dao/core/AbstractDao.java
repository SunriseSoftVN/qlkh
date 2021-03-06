/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.core;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.model.core.AbstractEntity;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import org.hibernate.criterion.*;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.ArrayList;
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
        List<String> joinAlias = new ArrayList<String>();
        if (StringUtils.isNotBlank(config.getSortField())) {
            String joinEntityName = getRootEntityName(config.getSortField());
            if (StringUtils.isNotEmpty(joinEntityName)) {
                joinAlias.add(joinEntityName);
            }
            if (config.getSortDir() == Style.SortDir.ASC) {
                criteria.addOrder(Order.asc(config.getSortField()));
            } else if (config.getSortDir() == Style.SortDir.DESC) {
                criteria.addOrder(Order.desc(config.getSortField()));
            }
        }
        if (config.get("hasFilter") != null && config.<Boolean>get("hasFilter")) {
            Map<String, Object> filters = config.get("filters");
            if (filters != null) {
                Criterion criterion = null;
                String joinEntityName;
                for (String filter : filters.keySet()) {
                    joinEntityName = getRootEntityName(filter);
                    if (StringUtils.isNotEmpty(joinEntityName) && !joinAlias.contains(joinEntityName)) {
                        joinAlias.add(joinEntityName);
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

        if (!joinAlias.isEmpty()) {
            for (String alias : joinAlias) {
                criteria.createAlias(alias, alias);
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
