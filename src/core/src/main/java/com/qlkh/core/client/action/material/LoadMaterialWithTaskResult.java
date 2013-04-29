package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.grid.LoadGridDataResult;

/**
 * The Class LoadMaterialWithTaskResult.
 *
 * @author Nguyen Duc Dung
 * @since 4/29/13 4:22 PM
 */
public class LoadMaterialWithTaskResult extends LoadGridDataResult {

    public LoadMaterialWithTaskResult() {
    }

    public LoadMaterialWithTaskResult(BasePagingLoadResult result) {
        super(result);
    }
}
