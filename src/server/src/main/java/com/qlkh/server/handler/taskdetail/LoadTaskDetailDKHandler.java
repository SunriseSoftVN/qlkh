/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.taskdetail;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailDKAction;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailDKResult;
import com.qlkh.core.client.model.Branch;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.TaskDetailDK;
import com.qlkh.server.dao.BranchDao;
import com.qlkh.server.dao.TaskDetailDKDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.util.DateTimeUtils;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class LoadSubTaskAnnualHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 9:10 PM
 */
public class LoadTaskDetailDKHandler extends AbstractHandler<LoadTaskDetailDKAction, LoadTaskDetailDKResult> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private BranchDao branchDao;

    @Autowired
    private TaskDetailDKDao taskDetailDKDao;

    @Override
    public Class<LoadTaskDetailDKAction> getActionType() {
        return LoadTaskDetailDKAction.class;
    }

    @Override
    public LoadTaskDetailDKResult execute(LoadTaskDetailDKAction action, ExecutionContext context) throws DispatchException {
        return new LoadTaskDetailDKResult(getSubTaskAnnualDetails(action.getLoadConfig(), action.getTaskId(), action.getStationId()));
    }

    public BasePagingLoadResult<TaskDetailDK> getSubTaskAnnualDetails(BasePagingLoadConfig loadConfig,
                                                                      long taskId, long stationId) {
        List<TaskDetailDK> taskDetailDKs = new ArrayList<TaskDetailDK>();
        Task task = generalDao.findById(Task.class, taskId);
        if (task != null) {
            List<Branch> branches = branchDao.findByStationId(stationId);
            if (CollectionUtils.isNotEmpty(branches)) {
                for (Branch branch : branches) {
                    TaskDetailDK taskDetailDK = taskDetailDKDao.
                            findByTaskIdAndBranchId(taskId, branch.getId());
                    if (taskDetailDK == null) {
                        taskDetailDK = new TaskDetailDK();
                        taskDetailDK.setYear(DateTimeUtils.getCurrentYear());
                        taskDetailDK.setTask(task);
                        taskDetailDK.setBranch(branch);
                        taskDetailDK.setCreateBy(1l);
                        taskDetailDK.setUpdateBy(1l);
                    }
                    taskDetailDKs.add(taskDetailDK);
                }
            }
        }
        return new BasePagingLoadResult<TaskDetailDK>(taskDetailDKs, loadConfig.getOffset(),
                taskDetailDKs.size());
    }
}
