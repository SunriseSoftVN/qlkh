/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.task;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.grid.LoadGridDataResult;

/**
 * The Class LoadUnusedTaskGridResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/12, 4:37 PM
 */
public class LoadUnusedTaskGridResult extends LoadGridDataResult {
    public LoadUnusedTaskGridResult() {
    }

    public LoadUnusedTaskGridResult(BasePagingLoadResult result) {
        super(result);
    }
}
