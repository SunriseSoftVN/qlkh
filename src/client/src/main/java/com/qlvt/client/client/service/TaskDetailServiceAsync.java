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
import com.qlvt.core.client.dto.TaskDetailDto;
import com.qlvt.core.client.model.SubTaskAnnualDetail;
import com.qlvt.core.client.model.SubTaskDetail;
import com.qlvt.core.client.model.TaskDetail;
import com.smvp4g.mvp.client.core.service.RemoteServiceAsync;

import java.util.List;

public interface TaskDetailServiceAsync extends RemoteServiceAsync<TaskDetailServiceAsync> {
    void getTaskDetailsForGrid(BasePagingLoadConfig loadConfig, long staionId, AsyncCallback<BasePagingLoadResult<TaskDetail>> async);

    void deleteTaskDetail(long taskDetailId, AsyncCallback<Void> async);

    void deleteTaskDetails(List<Long> taskDetailIds, AsyncCallback<Void> async);

    void updateTaskDetail(TaskDetail taskDetail, AsyncCallback<TaskDetail> async);

    void updateTaskDetails(List<TaskDetail> taskDetails, AsyncCallback<Void> async);

    void updateTaskDetailDtos(List<TaskDetailDto> taskDetails, AsyncCallback<Void> async);

    void getTaskAnnualDetailsForGrid(BasePagingLoadConfig loadConfig, long stationId, AsyncCallback<BasePagingLoadResult<TaskDetail>> async);

    void getSubTaskAnnualDetails(BasePagingLoadConfig loadConfig, long taskDetailId, AsyncCallback<BasePagingLoadResult<SubTaskAnnualDetail>> async);

    void updateSubTaskAnnualDetails(List<SubTaskAnnualDetail> subTaskAnnualDetails, AsyncCallback<Void> async);

    void getSubTaskDetails(BasePagingLoadConfig loadConfig, long taskDetailId, AsyncCallback<BasePagingLoadResult<SubTaskDetail>> async);

    void updateSubTaskDetails(List<SubTaskDetail> subTaskDetails, AsyncCallback<Void> async);
}
