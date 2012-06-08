/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.subtask;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.subtask.LoadSubTaskAnnualAction;
import com.qlkh.core.client.action.subtask.LoadSubTaskAnnualResult;
import com.qlkh.core.client.model.Branch;
import com.qlkh.core.client.model.SubTaskAnnualDetail;
import com.qlkh.core.client.model.TaskDetail;
import com.qlkh.server.dao.BranchDao;
import com.qlkh.server.dao.SubTaskAnnualDetailDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
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
public class LoadSubTaskAnnualHandler extends AbstractHandler<LoadSubTaskAnnualAction, LoadSubTaskAnnualResult> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private BranchDao branchDao;

    @Autowired
    private SubTaskAnnualDetailDao subTaskAnnualDetailDao;

    @Override
    public Class<LoadSubTaskAnnualAction> getActionType() {
        return LoadSubTaskAnnualAction.class;
    }

    @Override
    public LoadSubTaskAnnualResult execute(LoadSubTaskAnnualAction action, ExecutionContext context) throws DispatchException {
        return new LoadSubTaskAnnualResult(getSubTaskAnnualDetails(action.getLoadConfig(), action.getTaskDetailId()));
    }

    public BasePagingLoadResult<SubTaskAnnualDetail> getSubTaskAnnualDetails(BasePagingLoadConfig loadConfig,
                                                                             long taskDetailId) {
        List<SubTaskAnnualDetail> subTaskAnnualDetails = new ArrayList<SubTaskAnnualDetail>();
        TaskDetail taskDetail = generalDao.findById(TaskDetail.class, taskDetailId);
        if (taskDetail != null) {
            List<Branch> branches = branchDao.findByStationId(taskDetail.getStation().getId());
            if (CollectionUtils.isNotEmpty(branches)) {
                for (Branch branch : branches) {
                    SubTaskAnnualDetail subTaskAnnualDetail = subTaskAnnualDetailDao.
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
}
