package com.qlkh.server.handler.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.material.LoadMaterialLimitResult;
import com.qlkh.core.client.action.material.LoadMaterialPriceAction;
import com.qlkh.core.client.action.material.LoadMaterialPriceResult;
import com.qlkh.core.client.model.MaterialLimit;
import com.qlkh.core.client.model.MaterialPrice;
import com.qlkh.server.dao.MaterialLimitDao;
import com.qlkh.server.dao.MaterialPriceDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * The Class LoadLimitHandler.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 2:30 PM
 */
public class LoadMaterialPriceHandler extends AbstractHandler<LoadMaterialPriceAction, LoadMaterialPriceResult> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private MaterialPriceDao materialPriceDao;

    @Override
    public Class<LoadMaterialPriceAction> getActionType() {
        return LoadMaterialPriceAction.class;
    }

    @Override
    public LoadMaterialPriceResult execute(LoadMaterialPriceAction loadMaterialAction, ExecutionContext executionContext) throws DispatchException {
        List<MaterialPrice> materials = materialPriceDao.findByMaterialId(loadMaterialAction.getMaterialId());
        BasePagingLoadResult<MaterialPrice> result =
                new BasePagingLoadResult<MaterialPrice>(materials, loadMaterialAction.getLoadConfig().getOffset(), 1);
        return new LoadMaterialPriceResult(result);
    }
}
