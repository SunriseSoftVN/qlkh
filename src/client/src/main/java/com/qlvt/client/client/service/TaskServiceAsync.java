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

package com.qlvt.client.client.service;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.core.client.dto.TaskDto;
import com.qlvt.core.client.model.Task;
import com.smvp4g.mvp.client.core.service.RemoteServiceAsync;

import java.util.List;

public interface TaskServiceAsync extends RemoteServiceAsync<TaskServiceAsync> {
    void getTasksForGrid(BasePagingLoadConfig loadConfig, AsyncCallback<BasePagingLoadResult<Task>> callback);
    void updateTask(Task task, AsyncCallback<Void> async);
    void updateTasks(List<Task> tasks, AsyncCallback<Void> async);
    void deleteTask(long taskId, AsyncCallback<Void> async);
    void deleteTasks(List<Long> taskIds, AsyncCallback<Void> async);
    void getAllTasks(AsyncCallback<List<Task>> async);
    void getAllTaskDtos(AsyncCallback<List<TaskDto>> async);
}
