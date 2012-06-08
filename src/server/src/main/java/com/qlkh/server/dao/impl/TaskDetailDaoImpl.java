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
