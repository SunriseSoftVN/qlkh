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

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlvt.core.client.constant.TaskTypeEnum;
import com.qlvt.core.client.model.Task;
import com.qlvt.core.client.model.TaskDetail;
import com.qlvt.server.dao.TaskDao;
import com.qlvt.server.dao.core.AbstractDao;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

/**
 * The Class TaskDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 12/31/11, 2:35 PM
 */
public class TaskDaoImpl extends AbstractDao<Task> implements TaskDao {

    @Override
    public BasePagingLoadResult<Task> getUnusedTask(long stationId, TaskTypeEnum typeEnum, BasePagingLoadConfig config) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TaskDetail.class);
        criteria.add(Restrictions.eq("station.id", stationId));
        criteria.add(Restrictions.eq("annual", typeEnum == TaskTypeEnum.DK));
        criteria.add(Restrictions.eq("year", 1900 + new Date().getYear()));
        List<TaskDetail> taskDetails = getHibernateTemplate().findByCriteria(criteria);
        List<Long> usedTaskIds = extract(taskDetails, on(TaskDetail.class).getTask().getId());
        BasePagingLoadResult<Task> taskResult;

        if (CollectionUtils.isNotEmpty(usedTaskIds)) {
            taskResult = getByBeanConfig(Task.class.getName(), config,
                    Restrictions.eq("taskTypeCode", typeEnum.getCode()), Restrictions.not(Restrictions.in("id", usedTaskIds)));
        } else {
            taskResult = getByBeanConfig(Task.class.getName(), config,
                    Restrictions.eq("taskTypeCode", typeEnum.getCode()));
        }

        return taskResult;
    }
}
