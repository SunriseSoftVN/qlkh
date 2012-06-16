/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.system;

import com.qlkh.core.client.action.system.UpgradeDatabaseAction;
import com.qlkh.core.client.action.system.UpgradeDatabaseResult;
import com.qlkh.core.client.constant.TaskTypeEnum;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.TaskQuota;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.util.DateTimeUtils;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * The Class UpgradeDatabaseHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/16/12, 7:25 PM
 */
public class UpgradeDatabaseHandler extends AbstractHandler<UpgradeDatabaseAction, UpgradeDatabaseResult> {

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<UpgradeDatabaseAction> getActionType() {
        return UpgradeDatabaseAction.class;
    }

    @Override
    public UpgradeDatabaseResult execute(UpgradeDatabaseAction action, ExecutionContext context) throws DispatchException {
        List<Task> tasks = generalDao.findCriteria(Task.class, Restrictions.eq("taskTypeCode", TaskTypeEnum.DK.getCode()));
        for (Task task : tasks) {
            List<TaskQuota> taskQuotas = generalDao.
                    findCriteria(TaskQuota.class, Restrictions.eq("task.id", task.getId()),
                            Restrictions.eq("year", DateTimeUtils.getCurrentYear()));
            if (CollectionUtils.isEmpty(taskQuotas)) {
                TaskQuota taskQuota = new TaskQuota();
                taskQuota.setTask(task);
                taskQuota.setQ1(task.getQuota());
                taskQuota.setQ2(task.getQuota());
                taskQuota.setQ3(task.getQuota());
                taskQuota.setQ4(task.getQuota());
                taskQuota.setYear(DateTimeUtils.getCurrentYear());
                taskQuota.setCreateBy(1l);
                taskQuota.setUpdateBy(1l);
                generalDao.saveOrUpdate(taskQuota);
            }
        }
        return new UpgradeDatabaseResult();
    }
}
