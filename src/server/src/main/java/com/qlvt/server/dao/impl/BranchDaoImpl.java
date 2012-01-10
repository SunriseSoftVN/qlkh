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

package com.qlvt.server.dao.impl;

import com.google.inject.Singleton;
import com.qlvt.core.client.model.Branch;
import com.qlvt.server.dao.BranchDao;
import com.qlvt.server.dao.core.AbstractDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The Class BranchDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 1/2/12, 12:54 PM
 */
@Singleton
public class BranchDaoImpl extends AbstractDao<Branch> implements BranchDao {
    @Override
    public List<Branch> findByStationId(long stationId) {
        openSession();
        Criteria criteria = session.createCriteria(Branch.class)
                .add(Restrictions.eq("station.id", stationId));
        List<Branch> branches = criteria.list();
        closeSession();
        return branches;
    }
}
