/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.User;
import com.qlkh.server.dao.UserDao;
import com.qlkh.server.dao.core.AbstractDao;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The Class UserDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 12/27/11, 7:06 PM
 */
public class UserDaoImpl extends AbstractDao<User> implements UserDao {
    @Override
    public User findByUserName(String userName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class).add(Restrictions.eq("userName", userName));
        List<User> users = getHibernateTemplate().findByCriteria(criteria);
        if (CollectionsUtils.isNotEmpty(users)) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public List<User> findByStationId(long stationId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class).add(Restrictions.eq("station.id", stationId));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<User> findByStationIds(List<Long> stationIds) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class).add(Restrictions.in("station.id",stationIds));
        return getHibernateTemplate().findByCriteria(criteria);
    }
}
