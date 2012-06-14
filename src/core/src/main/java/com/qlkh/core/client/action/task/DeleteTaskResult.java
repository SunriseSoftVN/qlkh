/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.task;

import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class DeleteTaskResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/14/12, 10:52 AM
 */
public class DeleteTaskResult implements Result {
    private boolean deleted;

    public DeleteTaskResult() {
    }

    public DeleteTaskResult(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
