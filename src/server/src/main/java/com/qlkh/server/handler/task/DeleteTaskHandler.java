/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.task;

import com.qlkh.core.client.action.task.DeleteTaskAction;
import com.qlkh.core.client.action.task.DeleteTaskResult;
import com.qlkh.core.client.model.*;
import com.qlkh.core.client.model.core.AbstractEntity;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

/**
 * The Class DeleteTaskHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/14/12, 10:53 AM
 */
public class DeleteTaskHandler extends AbstractHandler<DeleteTaskAction, DeleteTaskResult> {

    private static final String[] RELATE_ENTITY_NAMES = {TaskDetailDK.class.getName(),
            TaskDetailKDK.class.getName(), TaskDetailNam.class.getName()};

    private static final String[] RELATE_DELETE_ENTITY_NAMES = {TaskDetailDK.class.getName(),
            TaskDetailKDK.class.getName(), TaskDetailNam.class.getName(),
            TaskQuota.class.getName(), TaskDefaultValue.class.getName()};


    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<DeleteTaskAction> getActionType() {
        return DeleteTaskAction.class;
    }

    @Override
    public DeleteTaskResult execute(DeleteTaskAction action, ExecutionContext context) throws DispatchException {
        List<Long> ids = new ArrayList<Long>();
        if (action.getId() > 0) {
            ids.add(action.getId());
        } else if (CollectionUtils.isNotEmpty(action.getIds())) {
            ids = action.getIds();
        }

        //Check delete rule. whether it can be delete or not.
        if (!action.isForceDelete()) {
            //Skip check if user want to force delete.
            for (String relateEntityName : RELATE_ENTITY_NAMES) {
                List result;
                for (Long id : ids) {
                    result = generalDao.findRelateEntityById(Task.class.getName(), id, relateEntityName);
                    if (CollectionUtils.isNotEmpty(result)) {
                        return new DeleteTaskResult(false);
                    }
                }
            }
        }

        //Delete relate entities, before delete tasks.
        for (String relateEntityName : RELATE_DELETE_ENTITY_NAMES) {
            List<AbstractEntity> result;
            for (Long id : ids) {
                result = generalDao.findRelateEntityById(Task.class.getName(), id, relateEntityName);
                if (CollectionUtils.isNotEmpty(result)) {
                    List<Long> deleteIds = extract(result, on(AbstractEntity.class).getId());
                    generalDao.deleteByIds(relateEntityName, deleteIds);
                }
            }
        }

        //Finally delete tasks.
        generalDao.deleteByIds(Task.class, ids);
        return new DeleteTaskResult(true);
    }
}
