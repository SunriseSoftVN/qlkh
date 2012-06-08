/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.core;

import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.server.dao.core.GeneralDao;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class SaveHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 1:55 PM
 */
public class SaveHandler extends AbstractHandler<SaveAction, SaveResult> {

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<SaveAction> getActionType() {
        return SaveAction.class;
    }

    @Override
    public SaveResult execute(SaveAction action, ExecutionContext context) throws DispatchException {
        if (action.getEntity() != null) {
            return new SaveResult(generalDao.saveOrUpdate(action.getEntity()));
        } else if (action.getEntities() != null) {
            return new SaveResult(generalDao.saveOrUpdate(action.getEntities()));
        }
        return null;
    }
}
