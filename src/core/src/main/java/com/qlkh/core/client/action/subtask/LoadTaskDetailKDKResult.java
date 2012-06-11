/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.subtask;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.grid.LoadGridDataResult;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class LoadSubTaskDetailResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/2/12, 12:43 PM
 */
public class LoadTaskDetailKDKResult extends LoadGridDataResult implements Result {

    public LoadTaskDetailKDKResult() {
    }

    public LoadTaskDetailKDKResult(BasePagingLoadResult result) {
        super(result);
    }
}
