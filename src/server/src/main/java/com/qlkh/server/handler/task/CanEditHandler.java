/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.task;

import com.qlkh.core.client.action.task.CanEditAction;
import com.qlkh.core.client.action.task.CanEditResult;
import com.qlkh.core.client.model.Task;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * The Class CanEditHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/12/12, 1:40 PM
 */
public class CanEditHandler extends AbstractHandler<CanEditAction, CanEditResult> {

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<CanEditAction> getActionType() {
        return CanEditAction.class;
    }

    @Override
    public CanEditResult execute(CanEditAction action, ExecutionContext context) throws DispatchException {
        if (action.getRelateEntityNames() != null) {
            for (String relateEntityName : action.getRelateEntityNames()) {
                List result = generalDao.
                        findRelateEntityById(Task.class.getName(), action.getId(), relateEntityName);
                if (CollectionUtils.isNotEmpty(result)) {
                    return new CanEditResult(false);
                }
            }
        }
        return new CanEditResult(true);
    }
}
