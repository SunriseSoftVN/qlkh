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
import com.qlvt.core.client.model.SubTaskAnnualDetail;
import com.qlvt.server.dao.SubTaskAnnualDetailDao;
import com.qlvt.server.dao.core.AbstractDao;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The Class SubTaskAnnualDetailDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 1/4/12, 9:09 PM
 */
@Singleton
public class SubTaskAnnualDetailDaoImpl extends AbstractDao<SubTaskAnnualDetail> implements SubTaskAnnualDetailDao {

    @Override
    public SubTaskAnnualDetail getSubAnnualTaskByTaskDetaiIdAndBranchId(long taskDetailId, long branchId) {
        openSession();
        Criteria criteria = session.createCriteria(SubTaskAnnualDetail.class).
                add(Restrictions.eq("taskDetail.id", taskDetailId)).add(Restrictions.eq("branch.id", branchId));
        List<SubTaskAnnualDetail> subTaskAnnualDetails = criteria.list();
        if (CollectionUtils.isNotEmpty(subTaskAnnualDetails)) {
            return subTaskAnnualDetails.get(0);
        }
        closeSession();
        return null;
    }

    @Override
    public void deleteSubAnnualTaskByTaskDetaiIdAndBrandIds(long taskDetailId, List<Long> branchIds) {
        openSession();
        Criteria criteria = session.createCriteria(SubTaskAnnualDetail.class).
                add(Restrictions.eq("taskDetail.id", taskDetailId)).add(Restrictions.in("branch.id", branchIds));
        List<SubTaskAnnualDetail> subTaskAnnualDetails = criteria.list();
        for (SubTaskAnnualDetail subTaskAnnualDetail : subTaskAnnualDetails) {
            session.delete(subTaskAnnualDetail);
        }
        closeSession();
    }

    @Override
    public List<SubTaskAnnualDetail> findBrandId(long brandId) {
        openSession();
        Criteria criteria = session.createCriteria(SubTaskAnnualDetail.class).
                add(Restrictions.eq("branch.id", brandId));
        List<SubTaskAnnualDetail> subTaskDetails = criteria.list();
        closeSession();
        return subTaskDetails;
    }
}
