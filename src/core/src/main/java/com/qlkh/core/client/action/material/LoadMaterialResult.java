package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.grid.LoadGridDataResult;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class LoadMaterialResult.
 *
 * @author Nguyen Duc Dung
 * @since 4/8/13 4:40 PM
 */
public class LoadMaterialResult extends LoadGridDataResult implements Result {

    public LoadMaterialResult() {
    }

    public LoadMaterialResult(BasePagingLoadResult result) {
        super(result);
    }
}
