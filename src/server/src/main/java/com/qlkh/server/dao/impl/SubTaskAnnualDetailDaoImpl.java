/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.SubTaskAnnualDetail;
import com.qlkh.server.dao.SubTaskAnnualDetailDao;
import com.qlkh.server.dao.core.AbstractDao;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The Class SubTaskAnnualDetailDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 1/4/12, 9:09 PM
 */
public class SubTaskAnnualDetailDaoImpl extends AbstractDao<SubTaskAnnualDetail> implements SubTaskAnnualDetailDao {

    @Override
    public SubTaskAnnualDetail findByTaskDetaiIdAndBranchId(long taskDetailId, long branchId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SubTaskAnnualDetail.class).
                add(Restrictions.eq("taskDetail.id", taskDetailId)).add(Restrictions.eq("branch.id", branchId));
        List<SubTaskAnnualDetail> subTaskAnnualDetails = getHibernateTemplate().findByCriteria(criteria);
        if (CollectionUtils.isNotEmpty(subTaskAnnualDetails)) {
            return subTaskAnnualDetails.get(0);
        }
        return null;
    }

    @Override
    public void deleteSubAnnualTaskByTaskDetaiIdAndBrandIds(long taskDetailId, List<Long> branchIds) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SubTaskAnnualDetail.class).
                add(Restrictions.eq("taskDetail.id", taskDetailId)).add(Restrictions.in("branch.id", branchIds));
        List<SubTaskAnnualDetail> subTaskAnnualDetails = getHibernateTemplate().findByCriteria(criteria);
        for (SubTaskAnnualDetail subTaskAnnualDetail : subTaskAnnualDetails) {
            getHibernateTemplate().delete(subTaskAnnualDetail);
        }
    }

    @Override
    public List<SubTaskAnnualDetail> findByBrandId(long brandId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SubTaskAnnualDetail.class).
                add(Restrictions.eq("branch.id", brandId));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<SubTaskAnnualDetail> findByTaskDetailId(long taskDetailId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SubTaskAnnualDetail.class).
                add(Restrictions.eq("taskDetail.id", taskDetailId));
        return getHibernateTemplate().findByCriteria(criteria);
    }
}
