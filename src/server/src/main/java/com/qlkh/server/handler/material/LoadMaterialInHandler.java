package com.qlkh.server.handler.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.material.LoadMaterialInAction;
import com.qlkh.core.client.action.material.LoadMaterialInResult;
import com.qlkh.core.client.model.MaterialIn;
import com.qlkh.core.client.model.MaterialPrice;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.core.IsEqual.equalTo;

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
        BasePagingLoadResult<MaterialIn> result = null;
        if (action.getGroupId() != null) {
            result = generalDao.getByBeanConfig(
                    MaterialIn.class.getName(), action.getLoadConfig(),
                    Restrictions.eq("quarter", action.getQuarter()),
                    Restrictions.eq("year", action.getYear()),
                    Restrictions.eq("group.id", action.getGroupId()));
        } else if (action.getStationId() != null) {
            result = generalDao.getByBeanConfig(
                    MaterialIn.class.getName(), action.getLoadConfig(),
                    Restrictions.eq("quarter", action.getQuarter()),
                    Restrictions.eq("year", action.getYear()),
                    Restrictions.eq("station.id", action.getStationId()));
        } else {
            result = generalDao.getByBeanConfig(
                    MaterialIn.class.getName(), action.getLoadConfig(),
                    Restrictions.eq("quarter", action.getQuarter()),
                    Restrictions.eq("year", action.getYear()));
        }


        if(result.getTotalLength() > 0) {
            List<Long> materialIds = extract(result.getData(), on(MaterialIn.class).getMaterial().getId());

            List<MaterialPrice> prices = generalDao.findCriteria(MaterialPrice.class,
                    Restrictions.in("material.id", materialIds),
                    Restrictions.eq("quarter", action.getQuarter()),
                    Restrictions.eq("year", action.getYear())
            );


            for (MaterialIn materialIn : result.getData()) {
                if (materialIn.getMaterial() != null) {
                    MaterialPrice price = selectUnique(prices,
                            having(on(MaterialPrice.class).getMaterial().getId(),
                                    equalTo(materialIn.getMaterial().getId())));
                    if (price != null) {
                        materialIn.getMaterial().setCurrentPrice(price);
                    }
                }
            }
        }

        return new LoadMaterialInResult(result);
    }
}
