/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.TaskDetail;
import com.qlkh.server.dao.TaskDetailDao;
import com.qlkh.server.dao.core.AbstractDao;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.Calendar;
import java.util.List;

/**
 * The Class TaskDetailDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 1/1/12, 3:49 PM
 */
public class TaskDetailDaoImpl extends AbstractDao<TaskDetail> implements TaskDetailDao {
    @Override
    public List<TaskDetail> findByStationId(long stationId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TaskDetail.class)
                .add(Restrictions.eq("station.id", stationId));
        return getHibernateTemplate().findByCriteria(criteria);
    }


    @Override
    public TaskDetail findCurrentByStationIdAndTaskId(long stationId, long taskId) {
        Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
        DetachedCriteria criteria = DetachedCriteria.forClass(TaskDetail.class)
                .add(Restrictions.eq("station.id", stationId))
                .add(Restrictions.eq("task.id", taskId))
                .add(Restrictions.eq("year", currentYear));

        List<TaskDetail> taskDetails = getHibernateTemplate().findByCriteria(criteria);
        if (CollectionUtils.isNotEmpty(taskDetails)) {
            return taskDetails.get(0);
        }
        return null;
    }
}
