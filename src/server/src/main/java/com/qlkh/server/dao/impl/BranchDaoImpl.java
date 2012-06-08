/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.Branch;
import com.qlkh.server.dao.BranchDao;
import com.qlkh.server.dao.core.AbstractDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The Class BranchDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 1/2/12, 12:54 PM
 */
public class BranchDaoImpl extends AbstractDao<Branch> implements BranchDao {
    @Override
    public List<Branch> findByStationId(long stationId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Branch.class)
                .add(Restrictions.eq("station.id", stationId));
        return getHibernateTemplate().findByCriteria(criteria);
    }
}
