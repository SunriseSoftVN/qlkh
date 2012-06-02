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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.qlvt.core.client.dto.SubTaskAnnualDetailDto;
import com.qlvt.core.client.dto.SubTaskDetailDto;
import com.qlvt.core.client.dto.TaskDetailDto;
import com.qlvt.core.client.model.Branch;
import com.qlvt.core.client.model.SubTaskAnnualDetail;
import com.qlvt.core.client.model.SubTaskDetail;
import com.qlvt.core.client.model.TaskDetail;
import com.qlvt.server.guice.DaoProvider;
import com.qlvt.server.service.core.AbstractService;
import com.qlvt.server.transaction.Transaction;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapperSingletonWrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Class TaskDetailServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 1/1/12, 3:53 PM
 */
@Transaction
@Singleton
public class TaskDetailServiceImpl extends AbstractService {

    @Inject
    private DaoProvider provider;


    public TaskDetail updateTaskDetail(TaskDetail taskDetail) {
        //Set year on server because client time might be wrong.
        taskDetail.setYear(1900 + new Date().getYear());
        return provider.getTaskDetailDao().saveOrUpdate(taskDetail);
    }

    public void updateTaskDetails(List<TaskDetail> taskDetails) {
        provider.getTaskDetailDao().saveOrUpdate(taskDetails);
    }

    public void updateTaskDetailDtos(List<TaskDetailDto> taskDetailDtos) {
        List<TaskDetail> taskDetails = new ArrayList<TaskDetail>(taskDetailDtos.size());
        List<SubTaskDetail> subTaskDetails = new ArrayList<SubTaskDetail>(taskDetails.size());
        List<SubTaskAnnualDetail> subTaskAnnualDetails = new ArrayList<SubTaskAnnualDetail>(taskDetails.size());
        for (TaskDetailDto taskDetailDto : taskDetailDtos) {
            TaskDetail taskDetail = DozerBeanMapperSingletonWrapper.getInstance().map(taskDetailDto, TaskDetail.class);
            taskDetails.add(taskDetail);
            List<Branch> branches = provider.getBranchDao().findByStationId(taskDetailDto.getStation().getId());
            for (Branch branch : branches) {
                Object dto = taskDetailDto.get(branch.getName());
                if (dto instanceof SubTaskDetailDto) {
                    SubTaskDetailDto subTaskDetailDto = (SubTaskDetailDto) dto;
                    SubTaskDetail subTaskDetail = DozerBeanMapperSingletonWrapper.getInstance().
                            map(subTaskDetailDto, SubTaskDetail.class);
                    subTaskDetail.setTaskDetail(taskDetail);
                    subTaskDetails.add(subTaskDetail);
                } else if (dto instanceof SubTaskAnnualDetailDto) {
                    SubTaskAnnualDetailDto subTaskAnnualDetailDto = (SubTaskAnnualDetailDto) dto;
                    SubTaskAnnualDetail subTaskAnnualDetail = DozerBeanMapperSingletonWrapper.getInstance()
                            .map(subTaskAnnualDetailDto, SubTaskAnnualDetail.class);
                    subTaskAnnualDetail.setTaskDetail(taskDetail);
                    subTaskAnnualDetails.add(subTaskAnnualDetail);
                }
            }
        }
        updateTaskDetails(taskDetails);
        if (CollectionUtils.isNotEmpty(subTaskDetails)) {
            provider.getSubTaskDetailDao().saveOrUpdate(subTaskDetails);
        }
        if (CollectionUtils.isNotEmpty(subTaskAnnualDetails)) {
            provider.getSubTaskAnnualDetailDao().saveOrUpdate(subTaskAnnualDetails);
        }
    }
}
