/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.core;

import com.qlkh.core.client.action.core.LoadAction;
import com.qlkh.core.client.action.core.LoadResult;
import com.qlkh.server.criterion.ClientRestrictionsTranslator;
import com.qlkh.server.dao.core.GeneralDao;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class LoadHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 11:41 AM
 */
public class LoadHandler extends AbstractHandler<LoadAction, LoadResult> {

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<LoadAction> getActionType() {
        return LoadAction.class;
    }

    @Override
    public LoadResult execute(LoadAction action, ExecutionContext context) throws DispatchException {
        if (action.getLoadType() == LoadAction.LoadActionType.ALL) {
            return new LoadResult(generalDao.getAll(action.getEntityName()));
        } else if (action.getLoadType() == LoadAction.LoadActionType.BY_ID) {
            return new LoadResult(generalDao.findById(action.getEntityName(), action.getId()));
        } else if (action.getLoadType() == LoadAction.LoadActionType.BY_CRITERIA) {
            return new LoadResult(generalDao.findCriteria(action.getEntityName(),
                    ClientRestrictionsTranslator.getCriterions(action.getCriterion())));
        }
        return null;
    }
}
