/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.SubTaskDetail;
import com.qlkh.server.dao.SubTaskDetailDao;
import com.qlkh.server.dao.core.AbstractDao;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The Class SubTaskDetailDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 1/3/12, 8:39 PM
 */
public class SubTaskDetailDaoImpl extends AbstractDao<SubTaskDetail> implements SubTaskDetailDao {

    @Override
    public SubTaskDetail findByTaskDetaiIdAndBranchId(long taskDetailId, long branchId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SubTaskDetail.class).
                add(Restrictions.eq("taskDetail.id", taskDetailId)).add(Restrictions.eq("branch.id", branchId));
        List<SubTaskDetail> subTaskDetails = getHibernateTemplate().findByCriteria(criteria);
        if (CollectionUtils.isNotEmpty(subTaskDetails)) {
            return subTaskDetails.get(0);
        }
        return null;
    }

    @Override
    public void deleteSubTaskByTaskDetaiIdAndBrandIds(long taskDetailId, List<Long> branchIds) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SubTaskDetail.class).
                add(Restrictions.eq("taskDetail.id", taskDetailId)).add(Restrictions.in("branch.id", branchIds));
        List<SubTaskDetail> subTaskDetails = getHibernateTemplate().findByCriteria(criteria);
        for (SubTaskDetail subTaskDetail : subTaskDetails) {
            getHibernateTemplate().delete(subTaskDetail);
        }
    }

    @Override
    public List<SubTaskDetail> findBrandId(long brandId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SubTaskDetail.class).
                add(Restrictions.eq("branch.id", brandId));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<SubTaskDetail> findByTaskDetailId(long taskDetailId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SubTaskDetail.class)
                .add(Restrictions.eq("taskDetail.id", taskDetailId));
        return getHibernateTemplate().findByCriteria(criteria);
    }
}
