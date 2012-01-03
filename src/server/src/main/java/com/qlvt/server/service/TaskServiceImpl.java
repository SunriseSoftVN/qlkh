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
import com.qlvt.core.client.dto.TaskDto;
import com.qlvt.core.client.model.Task;
import com.qlvt.server.dao.TaskDao;
import com.qlvt.server.service.core.AbstractService;
import org.dozer.DozerBeanMapperSingletonWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TaskServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 12/31/11, 2:39 PM
 */
@Singleton
public class TaskServiceImpl extends AbstractService implements TaskService {

    @Inject
    private TaskDao taskDao;

    @Override
    public BasePagingLoadResult<List<Task>> getTasksForGrid(BasePagingLoadConfig loadConfig) {
        return new BasePagingLoadResult(taskDao.getByBeanConfig(Task.class, loadConfig),
                loadConfig.getOffset(), taskDao.count(Task.class));
    }

    @Override
    public void updateTask(Task task) {
        taskDao.saveOrUpdate(task);
    }

    @Override
    public void updateTasks(List<Task> tasks) {
        taskDao.saveOrUpdate(tasks);
    }

    @Override
    public void deleteTask(long taskId) {
        taskDao.deleteById(Task.class, taskId);
    }

    @Override
    public void deleteTasks(List<Long> taskIds) {
        taskDao.deleteByIds(Task.class, taskIds);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskDao.getAll(Task.class);
    }

    @Override
    public List<TaskDto> getAllTaskDtos() {
        List<Task> tasks = taskDao.getAll(Task.class);
        List<TaskDto> taskDtos = new ArrayList<TaskDto>(tasks.size());
        for (Task task : tasks) {
            taskDtos.add(DozerBeanMapperSingletonWrapper.getInstance().map(task, TaskDto.class));
        }
        return taskDtos;
    }
}
