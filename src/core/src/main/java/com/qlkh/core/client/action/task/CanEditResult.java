/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.task;

import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class CanEditResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/12/12, 1:39 PM
 */
public class CanEditResult implements Result {
    private boolean editable;

    public CanEditResult() {
    }

    public CanEditResult(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }
}
