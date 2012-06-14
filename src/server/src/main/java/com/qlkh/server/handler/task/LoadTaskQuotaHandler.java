/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.task;

import com.qlkh.core.client.action.task.LoadTaskQuotaAction;
import com.qlkh.core.client.action.task.LoadTaskQuotaResult;
import com.qlkh.core.client.model.TaskQuota;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

/**
 * The Class LoadTaskQuotaHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/12, 3:13 AM
 */
public class LoadTaskQuotaHandler extends AbstractHandler<LoadTaskQuotaAction, LoadTaskQuotaResult> {

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<LoadTaskQuotaAction> getActionType() {
        return LoadTaskQuotaAction.class;
    }

    @Override
    public LoadTaskQuotaResult execute(LoadTaskQuotaAction action, ExecutionContext context) throws DispatchException {
        List<TaskQuota> taskQuotas = generalDao.findCriteria(TaskQuota.class,
                Restrictions.eq("task.id", action.getTaskId()));
        List<TaskQuota> select = select(taskQuotas,
                having(on(TaskQuota.class).getTask().getId(), equalTo(action.getTaskId()))
                        .and(having(on(TaskQuota.class).getYear(), lessThanOrEqualTo(1900 + new Date().getYear()))));
        if (CollectionUtils.isNotEmpty(select)) {
            TaskQuota taskQuota = selectMax(select, on(TaskQuota.class).getYear());
            return new LoadTaskQuotaResult(taskQuota);
        }
        return new LoadTaskQuotaResult();
    }
}
