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

import com.google.inject.Singleton;
import com.qlvt.core.client.constant.TaskTypeEnum;
import com.qlvt.core.client.model.Task;
import com.qlvt.server.dao.TaskDao;
import com.qlvt.server.dao.core.AbstractDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The Class TaskDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 12/31/11, 2:35 PM
 */
@Singleton
public class TaskDaoImpl extends AbstractDao<Task> implements TaskDao {
    @Override
    public List<Task> getAllNormalTask() {
        Criteria criteria = getCurrentSession().createCriteria(Task.class).
                add(Restrictions.eq("taskTypeCode", TaskTypeEnum.KDK.getTaskTypeCode()));
        return criteria.list();
    }

    @Override
    public List<Task> getAllAnnualTask() {
        Criteria criteria = getCurrentSession().createCriteria(Task.class).
                add(Restrictions.eq("taskTypeCode", TaskTypeEnum.DK.getTaskTypeCode()));
        return criteria.list();
    }

    @Override
    public List<Task> getAllOrderByCode() {
        Criteria criteria = getCurrentSession().createCriteria(Task.class)
                .addOrder(Order.asc("code"));
        return criteria.list();
    }
}
