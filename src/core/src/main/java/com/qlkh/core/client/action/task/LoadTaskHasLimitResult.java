package com.qlkh.core.client.action.task;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.grid.LoadGridDataResult;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class LoadTaskHasLimitResult.
 *
 * @author Nguyen Duc Dung
 * @since 3/21/13 11:00 AM
 */
public class LoadTaskHasLimitResult extends LoadGridDataResult implements Result {

    public LoadTaskHasLimitResult() {

    }

    public LoadTaskHasLimitResult(BasePagingLoadResult result) {
        super(result);
    }
}
