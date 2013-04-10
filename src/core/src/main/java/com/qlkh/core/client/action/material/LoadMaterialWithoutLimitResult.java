package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.grid.LoadGridDataResult;

/**
 * The Class LoadMaterialWithoutLimitResult.
 *
 * @author Nguyen Duc Dung
 * @since 4/11/13 12:26 AM
 */
public class LoadMaterialWithoutLimitResult extends LoadGridDataResult {

    public LoadMaterialWithoutLimitResult() {
    }

    public LoadMaterialWithoutLimitResult(BasePagingLoadResult result) {
        super(result);
    }
}
