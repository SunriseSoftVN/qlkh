package com.qlkh.server.handler.material;

import com.qlkh.core.client.action.material.DeleteMaterialAction;
import com.qlkh.core.client.action.material.DeleteMaterialResult;
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
 * The Class DeleteMaterialHanlder.
 *
 * @author Nguyen Duc Dung
 * @since 3/26/13 8:07 PM
 */
public class DeleteMaterialHandler extends AbstractHandler<DeleteMaterialAction, DeleteMaterialResult> {

    private static final String[] RELATE_ENTITY_NAMES = {MaterialLimit.class.getName()};

    private static final String[] RELATE_DELETE_ENTITY_NAMES = {MaterialLimit.class.getName()};

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<DeleteMaterialAction> getActionType() {
        return DeleteMaterialAction.class;
    }

    @Override
    public DeleteMaterialResult execute(DeleteMaterialAction action, ExecutionContext executionContext) throws DispatchException {
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
                    result = generalDao.findRelateEntityById(Material.class.getName(), id, relateEntityName);
                    if (CollectionUtils.isNotEmpty(result)) {
                        return new DeleteMaterialResult(false);
                    }
                }
            }
        }

        //Delete relate entities, before delete tasks.
        for (String relateEntityName : RELATE_DELETE_ENTITY_NAMES) {
            List<AbstractEntity> result;
            for (Long id : ids) {
                result = generalDao.findRelateEntityById(Material.class.getName(), id, relateEntityName);
                if (CollectionUtils.isNotEmpty(result)) {
                    List<Long> deleteIds = extract(result, on(AbstractEntity.class).getId());
                    generalDao.deleteByIds(relateEntityName, deleteIds);
                }
            }
        }

        //Finally delete Material.
        generalDao.deleteByIds(Material.class, ids);

        return new DeleteMaterialResult(true);
    }
}
