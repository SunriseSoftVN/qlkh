/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.taskdetail;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailNamAction;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailNamResult;
import com.qlkh.core.client.model.Branch;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.TaskDetailNam;
import com.qlkh.server.dao.BranchDao;
import com.qlkh.server.dao.TaskDetailNamDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Class LoadTaskDetailHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/11/12, 10:04 PM
 */
public class LoadTaskDetailNamHandler extends AbstractHandler<LoadTaskDetailNamAction, LoadTaskDetailNamResult> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private BranchDao branchDao;

    @Autowired
    private TaskDetailNamDao taskDetailNamDao;

    @Override
    public Class<LoadTaskDetailNamAction> getActionType() {
        return LoadTaskDetailNamAction.class;
    }

    @Override
    public LoadTaskDetailNamResult execute(LoadTaskDetailNamAction action, ExecutionContext context) throws DispatchException {
        return new LoadTaskDetailNamResult(getSubTaskAnnualDetails(action.getLoadConfig(), action.getTaskId(), action.getStationId()));
    }

    public BasePagingLoadResult<TaskDetailNam> getSubTaskAnnualDetails(BasePagingLoadConfig loadConfig,
                                                                      long taskId, long stationId) {
        List<TaskDetailNam> taskDetailNams = new ArrayList<TaskDetailNam>();
        Task task = generalDao.findById(Task.class, taskId);
        if (task != null) {
            List<Branch> branches = branchDao.findByStationId(stationId);
            if (CollectionUtils.isNotEmpty(branches)) {
                for (Branch branch : branches) {
                    TaskDetailNam taskDetailNam = taskDetailNamDao.
                            findByTaskIdAndBranchId(taskId, branch.getId());
                    if (taskDetailNam == null) {
                        taskDetailNam = new TaskDetailNam();
                        taskDetailNam.setYear(1900 + new Date().getYear());
                        taskDetailNam.setTask(task);
                        taskDetailNam.setBranch(branch);
                        taskDetailNam.setCreateBy(1l);
                        taskDetailNam.setUpdateBy(1l);
                    }
                    taskDetailNams.add(taskDetailNam);
                }
            }
        }
        return new BasePagingLoadResult<TaskDetailNam>(taskDetailNams, loadConfig.getOffset(),
                taskDetailNams.size());
    }

}
