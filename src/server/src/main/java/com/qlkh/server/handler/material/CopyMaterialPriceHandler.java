package com.qlkh.server.handler.material;

import com.qlkh.core.client.action.material.CopyMaterialPriceAction;
import com.qlkh.core.client.action.material.CopyMaterialPriceResult;
import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.server.dao.MaterialPriceDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.util.DateTimeUtils;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class CopyMaterialPriceHandler.
 *
 * @author Nguyen Duc Dung
 * @since 4/11/13 2:53 AM
 */
public class CopyMaterialPriceHandler extends AbstractHandler<CopyMaterialPriceAction, CopyMaterialPriceResult> {

    @Autowired
    private MaterialPriceDao materialPriceDao;

    @Override
    public Class<CopyMaterialPriceAction> getActionType() {
        return CopyMaterialPriceAction.class;
    }

    @Override
    public CopyMaterialPriceResult execute(CopyMaterialPriceAction action, ExecutionContext executionContext) throws DispatchException {
        int fromQuarter = 0;
        int formYear = 0;
        int toYear = DateTimeUtils.getCurrentYear();
        if (action.getQuarter().getCode() > 1) {
            fromQuarter = action.getQuarter().getCode() - 1;
            formYear = toYear;
        } else {
            fromQuarter = 4;
            formYear = toYear - 1;
        }

        materialPriceDao.copyData(QuarterEnum.valueOf(fromQuarter), action.getQuarter(), formYear, toYear);

        return new CopyMaterialPriceResult();
    }
}
