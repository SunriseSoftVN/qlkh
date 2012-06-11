/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.TaskDetailNam;
import com.qlkh.server.dao.TaskDetailNamDao;
import com.qlkh.server.dao.core.AbstractDao;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The Class TaskDetailNamDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 6/11/12, 10:09 PM
 */
public class TaskDetailNamDaoImpl extends AbstractDao<TaskDetailNam> implements TaskDetailNamDao {
    @Override
    public TaskDetailNam findByTaskIdAndBranchId(long taskId, long branchId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TaskDetailNam.class).
                add(Restrictions.eq("task.id", taskId)).add(Restrictions.eq("branch.id", branchId));
        List<TaskDetailNam> taskDetailNams = getHibernateTemplate().findByCriteria(criteria);
        if (CollectionUtils.isNotEmpty(taskDetailNams)) {
            return taskDetailNams.get(0);
        }
        return null;
    }
}
