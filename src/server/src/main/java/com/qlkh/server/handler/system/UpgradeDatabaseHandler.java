/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.system;

import com.qlkh.core.client.action.system.UpgradeDatabaseAction;
import com.qlkh.core.client.action.system.UpgradeDatabaseResult;
import com.qlkh.core.client.model.Task;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
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
        List<Task> tasks = generalDao.getAll(Task.class);
        for (Task task : tasks) {
            task.setCode(task.getCode().trim());
        }
        generalDao.saveOrUpdate(tasks);
        return new UpgradeDatabaseResult();
    }
}
