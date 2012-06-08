/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.taskdetail;

import com.qlkh.core.client.action.taskdetail.DeleteTaskDetailAction;
import com.qlkh.core.client.action.taskdetail.DeleteTaskDetailResult;
import com.qlkh.core.client.model.Branch;
import com.qlkh.core.client.model.TaskDetail;
import com.qlkh.server.dao.BranchDao;
import com.qlkh.server.dao.SubTaskAnnualDetailDao;
import com.qlkh.server.dao.SubTaskDetailDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
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
