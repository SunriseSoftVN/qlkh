/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.constant.TaskTypeEnum;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.TaskDetail;
import com.qlkh.server.dao.TaskDao;
import com.qlkh.server.dao.core.AbstractDao;
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
