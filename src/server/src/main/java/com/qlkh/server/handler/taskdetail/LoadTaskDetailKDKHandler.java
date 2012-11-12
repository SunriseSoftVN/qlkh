/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.taskdetail;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailKDKAction;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailKDKResult;
import com.qlkh.core.client.model.Branch;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.TaskDetailKDK;
import com.qlkh.server.dao.BranchDao;
import com.qlkh.server.dao.TaskDetailKDKDao;
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
 * The Class LoadSubTaskDetailHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/2/12, 12:45 PM
 */
public class LoadTaskDetailKDKHandler extends AbstractHandler<LoadTaskDetailKDKAction, LoadTaskDetailKDKResult> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private BranchDao branchDao;

    @Autowired
    private TaskDetailKDKDao taskDetailKDKDao;

    @Override
    public Class<LoadTaskDetailKDKAction> getActionType() {
        return LoadTaskDetailKDKAction.class;
    }

    @Override
    public LoadTaskDetailKDKResult execute(LoadTaskDetailKDKAction KDKAction, ExecutionContext context) throws DispatchException {
        BasePagingLoadResult<TaskDetailKDK> result = getSubTaskDetails(KDKAction.getLoadConfig(),
                KDKAction.getTaskId(), KDKAction.getStationId());
        return new LoadTaskDetailKDKResult(result);
    }

    public BasePagingLoadResult<TaskDetailKDK> getSubTaskDetails(BasePagingLoadConfig loadConfig, long taskDetailId, long stationId) {
        List<TaskDetailKDK> taskDetailKDKs = new ArrayList<TaskDetailKDK>();
        Task task = generalDao.findById(Task.class, taskDetailId);
        if (task != null) {
            List<Branch> branches = branchDao.findByStationId(stationId);
            if (CollectionUtils.isNotEmpty(branches)) {
                for (Branch branch : branches) {
                    TaskDetailKDK taskDetailKDK = taskDetailKDKDao.
                            findByTaskIdAndBranchId(task.getId(), branch.getId(), DateTimeUtils.getCurrentYear());
                    if (taskDetailKDK == null) {
                        taskDetailKDK = new TaskDetailKDK();
                        taskDetailKDK.setTask(task);
                        taskDetailKDK.setYear(DateTimeUtils.getCurrentYear());
                        taskDetailKDK.setBranch(branch);
                        taskDetailKDK.setCreateBy(1l);
                        taskDetailKDK.setUpdateBy(1l);
                    }
                    taskDetailKDKs.add(taskDetailKDK);
                }
            }
        }
        return new BasePagingLoadResult<TaskDetailKDK>(taskDetailKDKs, loadConfig.getOffset(),
                taskDetailKDKs.size());
    }
}
