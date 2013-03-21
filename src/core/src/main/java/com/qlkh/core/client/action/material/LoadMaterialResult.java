package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.grid.LoadGridDataResult;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class LoadLimitJobResult.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 2:26 PM
 */
public class LoadMaterialResult extends LoadGridDataResult implements Result {

    public LoadMaterialResult() {

    }

    public LoadMaterialResult(BasePagingLoadResult result) {
        super(result);
    }
}
