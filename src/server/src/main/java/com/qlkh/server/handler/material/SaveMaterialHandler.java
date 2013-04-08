package com.qlkh.server.handler.material;

import com.qlkh.core.client.action.material.SaveMaterialAction;
import com.qlkh.core.client.action.material.SaveMaterialResult;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.client.model.MaterialPrice;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class SaveMaterialHandler.
 *
 * @author Nguyen Duc Dung
 * @since 4/9/13 1:08 AM
 */
public class SaveMaterialHandler extends AbstractHandler<SaveMaterialAction, SaveMaterialResult> {

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<SaveMaterialAction> getActionType() {
        return SaveMaterialAction.class;
    }

    @Override
    public SaveMaterialResult execute(SaveMaterialAction saveMaterialAction, ExecutionContext executionContext) throws DispatchException {
        Material material = generalDao.saveOrUpdate(saveMaterialAction.getMaterial());
        MaterialPrice price = material.getCurrentPrice();
        if (price != null) {
            generalDao.saveOrUpdate(price);
        }
        return new SaveMaterialResult(material);
    }
}
