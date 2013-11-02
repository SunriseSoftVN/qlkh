package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.grid.LoadGridDataResult;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class LoadMaterialInResult.
 *
 * @author Nguyen Duc Dung
 * @since 11/2/13 11:59 AM
 */
public class LoadMaterialInResult extends LoadGridDataResult implements Result {

    public LoadMaterialInResult() {
    }

    public LoadMaterialInResult(BasePagingLoadResult result) {
        super(result);
    }
}
