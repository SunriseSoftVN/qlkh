/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.task;

import com.qlkh.core.client.action.task.LoadUnusedTaskGridAction;
import com.qlkh.core.client.action.task.LoadUnusedTaskGridResult;
import com.qlkh.server.dao.TaskDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class LoadUnusedTaskGridHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/12, 4:53 PM
 */
public class LoadUnusedTaskGridHandler extends AbstractHandler<LoadUnusedTaskGridAction, LoadUnusedTaskGridResult> {

    @Autowired
    private TaskDao taskDao;

    @Override
    public Class<LoadUnusedTaskGridAction> getActionType() {
        return LoadUnusedTaskGridAction.class;
    }

    @Override
    public LoadUnusedTaskGridResult execute(LoadUnusedTaskGridAction action, ExecutionContext context) throws DispatchException {
        return new LoadUnusedTaskGridResult(taskDao.getUnusedTask(action.getStationId(),
                action.getTypeEnum(), action.getConfig()));
    }
}
