/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.subtask;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.subtask.LoadSubTaskDetailAction;
import com.qlkh.core.client.action.subtask.LoadSubTaskDetailResult;
import com.qlkh.core.client.model.TaskDetailKDK;
import com.qlkh.server.dao.BranchDao;
import com.qlkh.server.dao.SubTaskDetailDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class LoadSubTaskDetailHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/2/12, 12:45 PM
 */
public class LoadSubTaskDetailHandler extends AbstractHandler<LoadSubTaskDetailAction, LoadSubTaskDetailResult> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private BranchDao branchDao;

    @Autowired
    private SubTaskDetailDao subTaskDetailDao;

    @Override
    public Class<LoadSubTaskDetailAction> getActionType() {
        return LoadSubTaskDetailAction.class;
    }

    @Override
    public LoadSubTaskDetailResult execute(LoadSubTaskDetailAction action, ExecutionContext context) throws DispatchException {
        BasePagingLoadResult<TaskDetailKDK> result = getSubTaskDetails(action.getLoadConfig(), action.getTaskDetailId());
        return new LoadSubTaskDetailResult(result);
    }

    public BasePagingLoadResult<TaskDetailKDK> getSubTaskDetails(BasePagingLoadConfig loadConfig, long taskDetailId) {
        List<TaskDetailKDK> taskDetailKDKs = new ArrayList<TaskDetailKDK>();
//        TaskDetail taskDetail = generalDao.findById(TaskDetail.class, taskDetailId);
//        if (taskDetail != null) {
//            List<Branch> branches = branchDao.findByStationId(taskDetail.getStation().getId());
//            if (CollectionUtils.isNotEmpty(branches)) {
//                for (Branch branch : branches) {
//                    TaskDetailKDK taskDetailKDK = subTaskDetailDao.
//                            findByTaskDetaiIdAndBranchId(taskDetail.getId(), branch.getId());
//                    if (taskDetailKDK == null) {
//                        taskDetailKDK = new TaskDetailKDK();
//                        taskDetailKDK.setTaskDetail(taskDetail);
//                        taskDetailKDK.setBranch(branch);
//                        taskDetailKDK.setCreateBy(1l);
//                        taskDetailKDK.setUpdateBy(1l);
//                    }
//                    taskDetailKDKs.add(taskDetailKDK);
//                }
//            }
//        }
        return new BasePagingLoadResult<TaskDetailKDK>(taskDetailKDKs, loadConfig.getOffset(),
                taskDetailKDKs.size());
    }
}
