package com.qlkh.server.handler.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.material.LoadMaterialWithoutPriceAction;
import com.qlkh.core.client.action.material.LoadMaterialWithoutPriceResult;
import com.qlkh.core.client.model.Material;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class LoadLimitHandler.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 2:30 PM
 */
public class LoadMaterialWithoutPriceHandler extends AbstractHandler<LoadMaterialWithoutPriceAction, LoadMaterialWithoutPriceResult> {

    @Autowired
    private SqlQueryDao sqlQueryDao;

    @Override
    public Class<LoadMaterialWithoutPriceAction> getActionType() {
        return LoadMaterialWithoutPriceAction.class;
    }

    @Override
    public LoadMaterialWithoutPriceResult execute(LoadMaterialWithoutPriceAction action, ExecutionContext executionContext) throws DispatchException {
        BasePagingLoadResult<Material> result = sqlQueryDao.getMaterials(action.getYear(),
                action.getQuarter(), action.getLoadConfig());
        return new LoadMaterialWithoutPriceResult(result);
    }
}
