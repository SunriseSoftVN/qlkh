/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.TaskDetailKDK;
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
public class SubTaskDetailDaoImpl extends AbstractDao<TaskDetailKDK> implements SubTaskDetailDao {

    @Override
    public TaskDetailKDK findByTaskDetaiIdAndBranchId(long taskDetailId, long branchId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TaskDetailKDK.class).
                add(Restrictions.eq("taskDetail.id", taskDetailId)).add(Restrictions.eq("branch.id", branchId));
        List<TaskDetailKDK> taskDetailKDKs = getHibernateTemplate().findByCriteria(criteria);
        if (CollectionUtils.isNotEmpty(taskDetailKDKs)) {
            return taskDetailKDKs.get(0);
        }
        return null;
    }

    @Override
    public void deleteSubTaskByTaskDetaiIdAndBrandIds(long taskDetailId, List<Long> branchIds) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TaskDetailKDK.class).
                add(Restrictions.eq("taskDetail.id", taskDetailId)).add(Restrictions.in("branch.id", branchIds));
        List<TaskDetailKDK> taskDetailKDKs = getHibernateTemplate().findByCriteria(criteria);
        for (TaskDetailKDK taskDetailKDK : taskDetailKDKs) {
            getHibernateTemplate().delete(taskDetailKDK);
        }
    }

    @Override
    public List<TaskDetailKDK> findBrandId(long brandId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TaskDetailKDK.class).
                add(Restrictions.eq("branch.id", brandId));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<TaskDetailKDK> findByTaskDetailId(long taskDetailId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TaskDetailKDK.class)
                .add(Restrictions.eq("taskDetail.id", taskDetailId));
        return getHibernateTemplate().findByCriteria(criteria);
    }
}
