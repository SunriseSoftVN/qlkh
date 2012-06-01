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

import com.qlvt.core.client.action.taskdetail.DeleteTaskDetailAction;
import com.qlvt.core.client.action.taskdetail.DeleteTaskDetailResult;
import com.qlvt.core.client.model.Branch;
import com.qlvt.core.client.model.TaskDetail;
import com.qlvt.server.dao.BranchDao;
import com.qlvt.server.dao.SubTaskAnnualDetailDao;
import com.qlvt.server.dao.SubTaskDetailDao;
import com.qlvt.server.dao.core.GeneralDao;
import com.qlvt.server.service.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class DeleteTaskDetailHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 8:49 PM
 */
public class DeleteTaskDetailHandler extends AbstractHandler<DeleteTaskDetailAction, DeleteTaskDetailResult> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private BranchDao branchDao;

    @Autowired
    private SubTaskDetailDao subTaskDetailDao;

    @Autowired
    private SubTaskAnnualDetailDao subTaskAnnualDetailDao;

    @Override
    public Class<DeleteTaskDetailAction> getActionType() {
        return DeleteTaskDetailAction.class;
    }

    @Override
    public DeleteTaskDetailResult execute(DeleteTaskDetailAction action, ExecutionContext context) throws DispatchException {
        if (CollectionUtils.isNotEmpty(action.getIds())) {
            deleteTaskDetails(action.getIds());
        } else if (action.getId() > 0) {
            deleteTaskDetail(action.getId());
        }
        return null;
    }

    private void deleteTaskDetails(List<Long> taskDetailIds) {
        for (Long taskId : taskDetailIds) {
            if (taskId != null) {
                deleteTaskDetail(taskId);
            }
        }
    }

    private void deleteTaskDetail(long taskDetailId) {
        TaskDetail taskDetail = generalDao.findById(TaskDetail.class, taskDetailId);
        if (taskDetail != null) {
            List<Branch> branches = branchDao.findByStationId(taskDetail.getStation().getId());

            //Delete SubTask First
            List<Long> branchIds = new ArrayList<Long>(branches.size());
            for (Branch branch : branches) {
                branchIds.add(branch.getId());
            }
            subTaskDetailDao.deleteSubTaskByTaskDetaiIdAndBrandIds(taskDetail.getId(), branchIds);

            subTaskAnnualDetailDao.deleteSubAnnualTaskByTaskDetaiIdAndBrandIds(taskDetail.getId(), branchIds);
            //Delete TaskDetail
            generalDao.deleteById(TaskDetail.class, taskDetailId);
        }
    }
}
