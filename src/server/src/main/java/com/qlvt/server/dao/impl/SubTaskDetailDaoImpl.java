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
import com.qlvt.core.client.model.SubTaskDetail;
import com.qlvt.server.dao.SubTaskDetailDao;
import com.qlvt.server.dao.core.AbstractDao;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The Class SubTaskDetailDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 1/3/12, 8:39 PM
 */
@Singleton
public class SubTaskDetailDaoImpl extends AbstractDao<SubTaskDetail> implements SubTaskDetailDao {

    @Override
    public SubTaskDetail findByTaskDetaiIdAndBranchId(long taskDetailId, long branchId) {
        Criteria criteria = getCurrentSession().createCriteria(SubTaskDetail.class).
                add(Restrictions.eq("taskDetail.id", taskDetailId)).add(Restrictions.eq("branch.id", branchId));
        List<SubTaskDetail> subTaskDetails = criteria.list();
        if (CollectionUtils.isNotEmpty(subTaskDetails)) {
            return subTaskDetails.get(0);
        }
        return null;
    }

    @Override
    public void deleteSubTaskByTaskDetaiIdAndBrandIds(long taskDetailId, List<Long> branchIds) {
        Criteria criteria = getCurrentSession().createCriteria(SubTaskDetail.class).
                add(Restrictions.eq("taskDetail.id", taskDetailId)).add(Restrictions.in("branch.id", branchIds));
        List<SubTaskDetail> subTaskDetails = criteria.list();
        for (SubTaskDetail subTaskDetail : subTaskDetails) {
            getCurrentSession().delete(subTaskDetail);
        }
    }

    @Override
    public List<SubTaskDetail> findBrandId(long brandId) {
        Criteria criteria = getCurrentSession().createCriteria(SubTaskDetail.class).
                add(Restrictions.eq("branch.id", brandId));
        List<SubTaskDetail> subTaskDetails = criteria.list();
        return subTaskDetails;
    }

    @Override
    public List<SubTaskDetail> findByTaskDetailId(long taskDetailId) {
        Criteria criteria = getCurrentSession().createCriteria(SubTaskDetail.class)
                .add(Restrictions.eq("taskDetail.id", taskDetailId));
        List<SubTaskDetail> subTaskDetails = criteria.list();
        return subTaskDetails;
    }
}
