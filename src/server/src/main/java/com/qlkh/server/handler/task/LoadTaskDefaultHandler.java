/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.task;

import com.qlkh.core.client.action.task.LoadTaskDefaultAction;
import com.qlkh.core.client.action.task.LoadTaskDefaultResult;
import com.qlkh.core.client.model.TaskDefaultValue;
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
import static org.hamcrest.Matchers.lessThanOrEqualTo;

/**
 * The Class LoadTaskDefaultHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/12, 1:00 AM
 */
public class LoadTaskDefaultHandler extends AbstractHandler<LoadTaskDefaultAction, LoadTaskDefaultResult> {

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<LoadTaskDefaultAction> getActionType() {
        return LoadTaskDefaultAction.class;
    }

    @Override
    public LoadTaskDefaultResult execute(LoadTaskDefaultAction action, ExecutionContext context) throws DispatchException {
        List<TaskDefaultValue> taskDefaultValues = generalDao.findCriteria(TaskDefaultValue.class,
                Restrictions.eq("task.id", action.getTaskId()));
        List<TaskDefaultValue> select = select(taskDefaultValues,
                having(on(TaskDefaultValue.class).getCreatedDate(), lessThanOrEqualTo(new Date())));
        if (CollectionUtils.isNotEmpty(select)) {
            TaskDefaultValue taskDefaultValue = selectMax(select, on(TaskDefaultValue.class).getId());
            return new LoadTaskDefaultResult(taskDefaultValue);
        }
        return new LoadTaskDefaultResult();
    }
}
