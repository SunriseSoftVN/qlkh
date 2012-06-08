/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.subtask;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.grid.LoadGridDataResult;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class LoadSubTaskAnnualDetailResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 9:08 PM
 */
public class LoadSubTaskAnnualResult extends LoadGridDataResult implements Result {

    public LoadSubTaskAnnualResult() {
    }

    public LoadSubTaskAnnualResult(BasePagingLoadResult result) {
        super(result);
    }
}
