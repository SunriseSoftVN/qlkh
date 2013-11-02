package com.qlkh.server.handler.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.material.LoadMaterialInAction;
import com.qlkh.core.client.action.material.LoadMaterialInResult;
import com.qlkh.core.client.model.MaterialIn;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class LoadMaterialInHandler.
 *
 * @author Nguyen Duc Dung
 * @since 11/2/13 12:05 PM
 */
public class LoadMaterialInHandler extends AbstractHandler<LoadMaterialInAction, LoadMaterialInResult> {

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<LoadMaterialInAction> getActionType() {
        return LoadMaterialInAction.class;
    }

    @Override
    public LoadMaterialInResult execute(LoadMaterialInAction action, ExecutionContext context) throws DispatchException {
        BasePagingLoadResult<MaterialIn> result = generalDao.
                getByBeanConfig(MaterialIn.class.getName(), action.getLoadConfig());
        return new LoadMaterialInResult(result);
    }
}
