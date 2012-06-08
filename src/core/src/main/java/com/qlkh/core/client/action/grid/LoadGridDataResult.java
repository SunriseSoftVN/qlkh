/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.grid;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.model.core.AbstractEntity;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class LoadGridDataResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 6:38 AM
 */
public class LoadGridDataResult implements Result {

    private BasePagingLoadResult result;

    public LoadGridDataResult() {
    }

    public LoadGridDataResult(BasePagingLoadResult result) {
        this.result = result;
    }

    public <E extends AbstractEntity> BasePagingLoadResult getResult() {
        return result;
    }
}
