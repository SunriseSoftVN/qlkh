/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.TaskDetailKDK;
import com.qlkh.server.dao.TaskDetailKDKDao;
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
public class TaskDetailKDKDaoImpl extends AbstractDao<TaskDetailKDK> implements TaskDetailKDKDao {

    @Override
    public TaskDetailKDK findByTaskIdAndBranchId(long taskId, long branchId, int year) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TaskDetailKDK.class).
                add(Restrictions.eq("task.id", taskId)).
                add(Restrictions.eq("branch.id", branchId)).
                add(Restrictions.eq("year", year));
        List<TaskDetailKDK> taskDetailKDKs = getHibernateTemplate().findByCriteria(criteria);
        if (CollectionUtils.isNotEmpty(taskDetailKDKs)) {
            return taskDetailKDKs.get(0);
        }
        return null;
    }
}
