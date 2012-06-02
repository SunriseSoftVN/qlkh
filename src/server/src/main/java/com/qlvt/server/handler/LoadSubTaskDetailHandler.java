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

package com.qlvt.server.handler;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlvt.core.client.action.subtaskdetail.LoadSubTaskDetailAction;
import com.qlvt.core.client.action.subtaskdetail.LoadSubTaskDetailResult;
import com.qlvt.core.client.model.Branch;
import com.qlvt.core.client.model.SubTaskDetail;
import com.qlvt.core.client.model.TaskDetail;
import com.qlvt.server.dao.BranchDao;
import com.qlvt.server.dao.SubTaskDetailDao;
import com.qlvt.server.dao.core.GeneralDao;
import com.qlvt.server.handler.core.AbstractHandler;
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
        BasePagingLoadResult<SubTaskDetail> result = getSubTaskDetails(action.getLoadConfig(), action.getTaskDetailId());
        return new LoadSubTaskDetailResult(result);
    }

    public BasePagingLoadResult<SubTaskDetail> getSubTaskDetails(BasePagingLoadConfig loadConfig, long taskDetailId) {
        List<SubTaskDetail> subTaskDetails = new ArrayList<SubTaskDetail>();
        TaskDetail taskDetail = generalDao.findById(TaskDetail.class, taskDetailId);
        if (taskDetail != null) {
            List<Branch> branches = branchDao.findByStationId(taskDetail.getStation().getId());
            if (CollectionUtils.isNotEmpty(branches)) {
                for (Branch branch : branches) {
                    SubTaskDetail subTaskDetail = subTaskDetailDao.
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
}
