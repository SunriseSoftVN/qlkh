/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.taskdetail;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.grid.LoadGridDataResult;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class LoadTaskDetailNamResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/11/12, 10:02 PM
 */
public class LoadTaskDetailNamResult extends LoadGridDataResult implements Result {

    public LoadTaskDetailNamResult() {
    }

    public LoadTaskDetailNamResult(BasePagingLoadResult result) {
        super(result);
    }
}
