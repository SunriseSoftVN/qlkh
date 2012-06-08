/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.gird;

import com.qlkh.core.client.action.grid.LoadGridDataAction;
import com.qlkh.core.client.action.grid.LoadGridDataResult;
import com.qlkh.server.criterion.ClientRestrictionsTranslator;
import com.qlkh.server.dao.GxtDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class LoadGridHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 6:41 AM
 */
public class LoadGridDataHandler extends AbstractHandler<LoadGridDataAction, LoadGridDataResult> {

    @Autowired
    private GxtDao gxtDao;

    @Override
    public Class<LoadGridDataAction> getActionType() {
        return LoadGridDataAction.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public LoadGridDataResult execute(LoadGridDataAction action, ExecutionContext context) throws DispatchException {
        return new LoadGridDataResult(gxtDao.getByBeanConfig(action.getEntityName(),
                action.getConfig(), ClientRestrictionsTranslator.getCriterions(action.getCriterion())));
    }
}
