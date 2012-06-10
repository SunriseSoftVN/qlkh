/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.core

import com.qlkh.core.client.action.core.DeleteAction
import com.qlkh.core.client.action.core.DeleteResult
import com.qlkh.server.dao.core.GeneralDao
import net.customware.gwt.dispatch.server.ExecutionContext
import net.customware.gwt.dispatch.shared.DispatchException
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.factory.annotation.Autowired

import static com.qlkh.core.client.action.core.DeleteAction.DeleteActionType.BY_ENTITY
import static com.qlkh.core.client.action.core.DeleteAction.DeleteActionType.BY_ID
import static com.qlkh.core.client.action.core.DeleteAction.DeleteActionType.BY_IDS

/**
 * The Class DeleteHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/10/12, 9:20 AM
 *
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
        if (action.relateEntityNames != null) {
            action.relateEntityNames.each { relateEntityName ->
                List result;
                String entityName = null;
                List<Long> ids = new ArrayList<Long>();

                if (action.entityName != null) {
                    entityName = action.entityName;
                } else if (action.entity != null) {
                    entityName = action.entity.class.name;
                }

                if (action.id > 0) {
                    ids.add(action.id);
                } else if (CollectionUtils.isNotEmpty(action.ids)) {
                    ids = action.ids;
                }

                ids.each { id ->
                    result = generalDao.findRelateEntityById(entityName, id, relateEntityName);
                    if (CollectionUtils.isNotEmpty(result)) {
                        return new DeleteResult(false);
                    }
                }
            }
        }
        switch (action.actionType) {
            case BY_ENTITY:
                generalDao.delete(action.entity);
                break;
            case BY_ID:
                generalDao.deleteById(action.entityName, action.id);
                break;
            case BY_IDS:
                generalDao.deleteByIds(action.entityName, action.ids);
                break;
        }
        return new DeleteResult(true);
    }
}
