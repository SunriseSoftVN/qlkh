/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.TaskDetailDK;
import com.qlkh.server.dao.TaskDetailDKDao;
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
public class TaskDetailDKDaoImpl extends AbstractDao<TaskDetailDK> implements TaskDetailDKDao {

    @Override
    public TaskDetailDK findByTaskIdAndBranchId(long taskId, long branchId, int year) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TaskDetailDK.class).
                add(Restrictions.eq("task.id", taskId)).
                add(Restrictions.eq("branch.id", branchId)).
                add(Restrictions.eq("year", year));
        List<TaskDetailDK> taskDetailDKs = getHibernateTemplate().findByCriteria(criteria);
        if (CollectionUtils.isNotEmpty(taskDetailDKs)) {
            return taskDetailDKs.get(0);
        }
        return null;
    }
}
