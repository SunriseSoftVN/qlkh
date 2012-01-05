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
import com.qlvt.server.dao.BranchDao;
import com.qlvt.server.dao.SubTaskAnnualDetailDao;
import com.qlvt.server.dao.SubTaskDetailDao;
import com.qlvt.server.dao.TaskDetailDao;
import com.qlvt.server.service.core.AbstractService;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Class TaskDetailServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 1/1/12, 3:53 PM
 */
@Singleton
public class TaskDetailServiceImpl extends AbstractService implements TaskDetailService {

    @Inject
    private TaskDetailDao taskDetailDao;

    @Inject
    private SubTaskDetailDao subTaskDetailDao;

    @Inject
    private SubTaskAnnualDetailDao subTaskAnnualDetailDao;

    @Inject
    private BranchDao branchDao;

    @Override
    public BasePagingLoadResult<TaskDetailDto> getTaskDetailsForGrid(BasePagingLoadConfig loadConfig, long stationId) {
        List<Branch> branches = branchDao.getBranchsByStationId(stationId);
        List<TaskDetail> taskDetails = taskDetailDao.getByBeanConfig(TaskDetail.class, loadConfig, Restrictions.eq("station.id", stationId));
        List<TaskDetailDto> taskDetailDtos = new ArrayList<TaskDetailDto>(taskDetails.size());
        for (TaskDetail taskDetail : taskDetails) {
            TaskDetailDto taskDetailDto = DozerBeanMapperSingletonWrapper.
                    getInstance().map(taskDetail, TaskDetailDto.class);
            for (Branch branch : branches) {
                SubTaskDetail subTaskDetail = subTaskDetailDao.
                        getSubTaskByTaskDetaiIdAndBranchId(taskDetail.getId(), branch.getId());
                if (subTaskDetail == null) {
                    subTaskDetail = new SubTaskDetail();
                    subTaskDetail.setTaskDetail(taskDetail);
                    subTaskDetail.setBranch(branch);
                    subTaskDetail.setCreateBy(1l);
                    subTaskDetail.setUpdateBy(1l);
                    subTaskDetail.setCreatedDate(new Date());
                    subTaskDetail.setUpdatedDate(new Date());
                }
                taskDetailDto.set(branch.getName(), DozerBeanMapperSingletonWrapper.getInstance().
                        map(subTaskDetail, SubTaskDetailDto.class));
            }
            taskDetailDtos.add(taskDetailDto);
        }
        return new BasePagingLoadResult<TaskDetailDto>(taskDetailDtos, loadConfig.getOffset(),
                taskDetailDao.count(TaskDetail.class));
    }

    @Override
    public BasePagingLoadResult<TaskDetailDto> getTaskAnnualDetailsForGrid(BasePagingLoadConfig loadConfig, long stationId) {
        List<Branch> branches = branchDao.getBranchsByStationId(stationId);
        List<TaskDetail> taskDetails = taskDetailDao.getByBeanConfig(TaskDetail.class, loadConfig, Restrictions.eq("station.id", stationId));
        List<TaskDetailDto> taskDetailDtos = new ArrayList<TaskDetailDto>(taskDetails.size());
        for (TaskDetail taskDetail : taskDetails) {
            TaskDetailDto taskDetailDto = DozerBeanMapperSingletonWrapper.
                    getInstance().map(taskDetail, TaskDetailDto.class);
            for (Branch branch : branches) {
                SubTaskAnnualDetail subTaskAnnualDetail = subTaskAnnualDetailDao.
                        getSubAnnualTaskByTaskDetaiIdAndBranchId(taskDetail.getId(), branch.getId());
                if (subTaskAnnualDetail == null) {
                    subTaskAnnualDetail = new SubTaskAnnualDetail();
                    subTaskAnnualDetail.setTaskDetail(taskDetail);
                    subTaskAnnualDetail.setBranch(branch);
                    subTaskAnnualDetail.setCreateBy(1l);
                    subTaskAnnualDetail.setUpdateBy(1l);
                    subTaskAnnualDetail.setCreatedDate(new Date());
                    subTaskAnnualDetail.setUpdatedDate(new Date());
                }
                taskDetailDto.set(branch.getName(), DozerBeanMapperSingletonWrapper.getInstance().
                        map(subTaskAnnualDetail, SubTaskAnnualDetailDto.class));
            }
            taskDetailDtos.add(taskDetailDto);
        }
        return new BasePagingLoadResult<TaskDetailDto>(taskDetailDtos, loadConfig.getOffset(),
                taskDetailDao.count(TaskDetail.class));
    }

    @Override
    public void deleteTaskDetail(long taskDetailId) {
        TaskDetail taskDetail = taskDetailDao.findById(TaskDetail.class, taskDetailId);
        if (taskDetail != null) {
            List<Branch> branches = branchDao.getBranchsByStationId(taskDetail.getStation().getId());

            //Delete SubTask First
            List<Long> branchIds = new ArrayList<Long>(branches.size());
            for (Branch branch : branches) {
                branchIds.add(branch.getId());
            }
            subTaskDetailDao.deleteSubTaskByTaskDetaiIdAndBrandIds(taskDetail.getId(), branchIds);
            //Delete TaskDetail
            taskDetailDao.deleteById(TaskDetail.class, taskDetailId);
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
    public void updateTaskDetail(TaskDetail taskDetail) {
        taskDetailDao.saveOrUpdate(taskDetail);
    }

    @Override
    public void updateTaskDetails(List<TaskDetail> taskDetails) {
        taskDetailDao.saveOrUpdate(taskDetails);
    }

    @Override
    public void updateTaskDetailDtos(List<TaskDetailDto> taskDetailDtos) {
        List<TaskDetail> taskDetails = new ArrayList<TaskDetail>(taskDetailDtos.size());
        List<SubTaskDetail> subTaskDetails = new ArrayList<SubTaskDetail>(taskDetails.size());
        for (TaskDetailDto taskDetailDto : taskDetailDtos) {
            TaskDetail taskDetail = DozerBeanMapperSingletonWrapper.getInstance().map(taskDetailDto, TaskDetail.class);
            taskDetails.add(taskDetail);
            List<Branch> branches = branchDao.getBranchsByStationId(taskDetailDto.getStation().getId());
            for (Branch branch : branches) {
                SubTaskDetailDto subTaskDetailDto = taskDetailDto.get(branch.getName());
                if (subTaskDetailDto != null) {
                    SubTaskDetail subTaskDetail = DozerBeanMapperSingletonWrapper.getInstance().
                            map(subTaskDetailDto, SubTaskDetail.class);
                    subTaskDetail.setTaskDetail(taskDetail);
                    subTaskDetails.add(subTaskDetail);
                }
            }
        }
        updateTaskDetails(taskDetails);
        subTaskDetailDao.saveOrUpdate(subTaskDetails);
    }
}
