package com.qlkh.server.handler.material;

import com.qlkh.core.client.action.material.MaterialInGetNextCodeAction;
import com.qlkh.core.client.action.material.MaterialInGetNextCodeResult;
import com.qlkh.server.dao.MaterialInDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class MaterialInGetNextCodeHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/4/13 4:37 PM
 */
public class MaterialInGetNextCodeHandler extends AbstractHandler<MaterialInGetNextCodeAction, MaterialInGetNextCodeResult> {

    @Autowired
    private MaterialInDao materialInDao;

    @Override
    public Class<MaterialInGetNextCodeAction> getActionType() {
        return MaterialInGetNextCodeAction.class;
    }

    @Override
    public MaterialInGetNextCodeResult execute(MaterialInGetNextCodeAction action, ExecutionContext context) throws DispatchException {
        return new MaterialInGetNextCodeResult(materialInDao.getNextCode());
    }
}
