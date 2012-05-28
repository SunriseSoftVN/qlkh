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
import com.qlvt.client.client.service.TaskDetailService;
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
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TaskDetailServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 1/1/12, 3:53 PM
 */
@Transaction
@Singleton
public class TaskDetailServiceImpl extends AbstractService implements TaskDetailService {

    @Inject
    private DaoProvider provider;

    @Override
    public BasePagingLoadResult<TaskDetail> getTaskDetailsForGrid(BasePagingLoadConfig loadConfig, long stationId) {
        return provider.getTaskDetailDao().getByBeanConfig(TaskDetail.class, loadConfig,
                Restrictions.eq("station.id", stationId), Restrictions.eq("annual", false));
    }

    @Override
    public BasePagingLoadResult<TaskDetail> getTaskAnnualDetailsForGrid(BasePagingLoadConfig loadConfig, long stationId) {
        return provider.getTaskDetailDao().getByBeanConfig(TaskDetail.class, loadConfig,
                Restrictions.eq("station.id", stationId), Restrictions.eq("annual", true));
    }

    @Override
    public BasePagingLoadResult<SubTaskAnnualDetail> getSubTaskAnnualDetails(BasePagingLoadConfig loadConfig,
                                                                             long taskDetailId) {
        List<SubTaskAnnualDetail> subTaskAnnualDetails = new ArrayList<SubTaskAnnualDetail>();
        TaskDetail taskDetail = provider.getTaskDetailDao().findById(TaskDetail.class, taskDetailId);
        if (taskDetail != null) {
            List<Branch> branches = provider.getBranchDao().findByStationId(taskDetail.getStation().getId());
            if (CollectionUtils.isNotEmpty(branches)) {
                for (Branch branch : branches) {
                    SubTaskAnnualDetail subTaskAnnualDetail = provider.getSubTaskAnnualDetailDao().
                            findByTaskDetaiIdAndBranchId(taskDetail.getId(), branch.getId());
                    if (subTaskAnnualDetail == null) {
                        subTaskAnnualDetail = new SubTaskAnnualDetail();
                        subTaskAnnualDetail.setTaskDetail(taskDetail);
                        subTaskAnnualDetail.setBranch(branch);
                        subTaskAnnualDetail.setCreateBy(1l);
                        subTaskAnnualDetail.setUpdateBy(1l);
                    }
                    subTaskAnnualDetails.add(subTaskAnnualDetail);
                }
            }
        }
        return new BasePagingLoadResult<SubTaskAnnualDetail>(subTaskAnnualDetails, loadConfig.getOffset(),
                subTaskAnnualDetails.size());
    }

    @Override
    public BasePagingLoadResult<SubTaskDetail> getSubTaskDetails(BasePagingLoadConfig loadConfig, long taskDetailId) {
        List<SubTaskDetail> subTaskDetails = new ArrayList<SubTaskDetail>();
        TaskDetail taskDetail = provider.getTaskDetailDao().findById(TaskDetail.class, taskDetailId);
        if (taskDetail != null) {
            List<Branch> branches = provider.getBranchDao().findByStationId(taskDetail.getStation().getId());
            if (CollectionUtils.isNotEmpty(branches)) {
                for (Branch branch : branches) {
                    SubTaskDetail subTaskDetail = provider.getSubTaskDetailDao().
                            findByTaskDetaiIdAndBranchId(taskDetail.getId(), branch.getId());
                    if (subTaskDetail == null) {
                        subTaskDetail = new SubTaskDetail();
                        subTaskDetail.setTaskDetail(taskDetail);
                        subTaskDetail.setBranch(branch);
                        subTaskDetail.setCreateBy(1l);
                        subTaskDetail.setUpdateBy(1l);
                    }
                    subTaskDetails.add(subTaskDetail);
                }
            }
        }
        return new BasePagingLoadResult<SubTaskDetail>(subTaskDetails, loadConfig.getOffset(),
                subTaskDetails.size());
    }

    @Override
    public void deleteTaskDetail(long taskDetailId) {
        TaskDetail taskDetail = provider.getTaskDetailDao().findById(TaskDetail.class, taskDetailId);
        if (taskDetail != null) {
            List<Branch> branches = provider.getBranchDao().findByStationId(taskDetail.getStation().getId());

            //Delete SubTask First
            List<Long> branchIds = new ArrayList<Long>(branches.size());
            for (Branch branch : branches) {
                branchIds.add(branch.getId());
            }
            provider.getSubTaskDetailDao().deleteSubTaskByTaskDetaiIdAndBrandIds(taskDetail.getId(), branchIds);

            provider.getSubTaskAnnualDetailDao().deleteSubAnnualTaskByTaskDetaiIdAndBrandIds(taskDetail.getId(), branchIds);
            //Delete TaskDetail
            provider.getTaskDetailDao().deleteById(TaskDetail.class, taskDetailId);
        }
    }

    @Override
    public void deleteTaskDetails(List<Long> taskDetailIds) {
        for (Long taskId : taskDetailIds) {
            if (taskId != null) {
                deleteTaskDetail(taskId);
            }
        }
    }

    @Override
    public TaskDetail updateTaskDetail(TaskDetail taskDetail) {
        return provider.getTaskDetailDao().saveOrUpdate(taskDetail);
    }

    @Override
    public void updateTaskDetails(List<TaskDetail> taskDetails) {
        provider.getTaskDetailDao().saveOrUpdate(taskDetails);
    }

    @Override
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


    @Override
    public void updateSubTaskAnnualDetails(List<SubTaskAnnualDetail> subTaskAnnualDetails) {
        provider.getSubTaskAnnualDetailDao().saveOrUpdate(subTaskAnnualDetails);
    }

    @Override
    public void updateSubTaskDetails(List<SubTaskDetail> subTaskDetails) {
        provider.getSubTaskDetailDao().saveOrUpdate(subTaskDetails);
    }
}
