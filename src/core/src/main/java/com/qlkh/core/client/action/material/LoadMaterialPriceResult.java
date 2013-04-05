package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.grid.LoadGridDataResult;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class MaterialPriceResult.
 *
 * @author Nguyen Duc Dung
 * @since 4/5/13 11:25 AM
 */
public class LoadMaterialPriceResult extends LoadGridDataResult implements Result {

    public LoadMaterialPriceResult() {

    }

    public LoadMaterialPriceResult(BasePagingLoadResult result) {
        super(result);
    }
}
