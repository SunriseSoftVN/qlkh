/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.core;

import com.qlkh.core.client.action.core.DeleteAction;
import com.qlkh.core.client.action.core.DeleteResult;
import com.qlkh.server.dao.core.GeneralDao;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class DeleteHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 2:05 PM
 */
public class DeleteHandler extends AbstractHandler<DeleteAction, DeleteResult> {

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<DeleteAction> getActionType() {
        return DeleteAction.class;
    }

    @Override
    public DeleteResult execute(DeleteAction action, ExecutionContext context) throws DispatchException {
        if (action.getRelateEntityNames() != null) {
            for (String relateEntityName : action.getRelateEntityNames()) {
                List result;
                String entityName = null;
                List<Long> ids = new ArrayList<Long>();

                if (action.getEntityName() != null) {
                    entityName = action.getEntityName();
                } else if (action.getEntity() != null) {
                    entityName = action.getEntity().getClass().getName();
                }

                if (action.getId() > 0) {
                    ids.add(action.getId());
                } else if (CollectionUtils.isNotEmpty(action.getIds())) {
                    ids = action.getIds();
                }

                for (Long id : ids) {
                    result = generalDao.findRelateEntityById(entityName, id, relateEntityName);
                    if (CollectionUtils.isNotEmpty(result)) {
                        return new DeleteResult(false);
                    }
                }
            }
        }
        if (action.getActionType() == DeleteAction.DeleteActionType.BY_ENTITY) {
            generalDao.delete(action.getEntity());
        } else if (action.getActionType() == DeleteAction.DeleteActionType.BY_ID) {
            generalDao.deleteById(action.getEntityName(), action.getId());
        } else if (action.getActionType() == DeleteAction.DeleteActionType.BY_IDS) {
            generalDao.deleteByIds(action.getEntityName(), action.getIds());
        }
        return new DeleteResult(true);
    }
}
