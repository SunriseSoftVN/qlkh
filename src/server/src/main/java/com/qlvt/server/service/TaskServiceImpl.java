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

package com.qlvt.server.service;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.qlvt.client.client.service.TaskService;
import com.qlvt.core.client.exception.CodeExistException;
import com.qlvt.core.client.exception.ExceptionHandler;
import com.qlvt.core.client.model.Task;
import com.qlvt.server.guice.DaoProvider;
import com.qlvt.server.service.core.AbstractService;
import com.qlvt.server.transaction.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

/**
 * The Class TaskServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 12/31/11, 2:39 PM
 */
@Transaction
@Singleton
public class TaskServiceImpl extends AbstractService implements TaskService {

    @Inject
    private DaoProvider provider;

    @Override
    public BasePagingLoadResult<Task> getTasksForGrid(BasePagingLoadConfig loadConfig) {
        return provider.getTaskDao().getByBeanConfig(Task.class, loadConfig);
    }

    @ExceptionHandler(expectException = ConstraintViolationException.class,
            wrapperException = CodeExistException.class)
    @Override
    public Task updateTask(Task task) throws CodeExistException {
        return provider.getTaskDao().saveOrUpdate(task);
    }

    @Override
    public void updateTasks(List<Task> tasks) throws CodeExistException {
        try {
            provider.getTaskDao().saveOrUpdate(tasks);
        } catch (ConstraintViolationException ex) {
            throw new CodeExistException();
        }
    }

    @Override
    public void deleteTask(long taskId) {
        provider.getTaskDao().deleteById(Task.class, taskId);
    }

    @Override
    public void deleteTasks(List<Long> taskIds) {
        provider.getTaskDao().deleteByIds(Task.class, taskIds);
    }

    @Override
    public List<Task> getAllTasks() {
        return provider.getTaskDao().getAll(Task.class);
    }

    @Override
    public List<Task> getAllNormalTasks() {
        return provider.getTaskDao().getAllNormalTask();
    }

    @Override
    public List<Task> getAllAnnualTasks() {
        return provider.getTaskDao().getAllAnnualTask();
    }
}
