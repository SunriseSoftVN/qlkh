/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.core;

import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class DeleteResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 2:03 PM
 */
public class DeleteResult implements Result {

    private boolean result;

    public DeleteResult() {
    }

    public DeleteResult(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }
}
