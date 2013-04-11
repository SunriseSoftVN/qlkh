package com.qlkh.core.client.action.material;

import com.qlkh.core.client.constant.QuarterEnum;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class CopyMaterialPriceAction.
 *
 * @author Nguyen Duc Dung
 * @since 4/11/13 2:52 AM
 */
public class CopyMaterialPriceAction implements Action<CopyMaterialPriceResult> {

    private QuarterEnum quarter;

    public CopyMaterialPriceAction() {
    }

    public CopyMaterialPriceAction(QuarterEnum quarter) {
        this.quarter = quarter;
    }

    public QuarterEnum getQuarter() {
        return quarter;
    }
}
