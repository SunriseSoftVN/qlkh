package com.qlkh.server.handler.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.material.LoadMaterialAction;
import com.qlkh.core.client.action.material.LoadMaterialResult;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.client.model.MaterialPrice;
import com.qlkh.server.dao.MaterialPriceDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.util.DateTimeUtils;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * The Class LoadMaterialHandler.
 *
 * @author Nguyen Duc Dung
 * @since 4/8/13 11:54 PM
 */
public class LoadMaterialHandler extends AbstractHandler<LoadMaterialAction, LoadMaterialResult> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private MaterialPriceDao materialPriceDao;

    @Override
    public Class<LoadMaterialAction> getActionType() {
        return LoadMaterialAction.class;
    }

    @Override
    public LoadMaterialResult execute(LoadMaterialAction loadMaterialAction, ExecutionContext executionContext) throws DispatchException {
        BasePagingLoadResult<Material> result = generalDao.getByBeanConfig(Material.class.getName(), loadMaterialAction.getConfig());

        int currentYear = DateTimeUtils.getCurrentYear();
        //TODO: This is gonna be too slow. @dungvn3000
        for (Material material : result.getData()) {
            MaterialPrice materialPrice = materialPriceDao.findByMaterialIdAndYear(material.getId(), currentYear);
            material.setCurrentPrice(materialPrice);
        }

        return new LoadMaterialResult(result);
    }
}
