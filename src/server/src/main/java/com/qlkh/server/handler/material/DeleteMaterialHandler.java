package com.qlkh.server.handler.material;

import com.qlkh.core.client.action.material.DeleteMaterialAction;
import com.qlkh.core.client.action.material.DeleteMaterialResult;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * The Class DeleteMaterialHanlder.
 *
 * @author Nguyen Duc Dung
 * @since 3/26/13 8:07 PM
 */
public class DeleteMaterialHandler extends AbstractHandler<DeleteMaterialAction, DeleteMaterialResult> {

    @Override
    public Class<DeleteMaterialAction> getActionType() {
        return DeleteMaterialAction.class;
    }

    @Override
    public DeleteMaterialResult execute(DeleteMaterialAction deleteMaterialAction, ExecutionContext executionContext) throws DispatchException {
        return null;
    }
}
