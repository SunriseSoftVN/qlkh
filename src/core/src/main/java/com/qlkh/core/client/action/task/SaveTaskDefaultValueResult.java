/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.task;

import com.qlkh.core.client.model.Task;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class SaveTaskDefaultValueResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/12, 10:28 PM
 */
public class SaveTaskDefaultValueResult implements Result {
    private Task task;

    public SaveTaskDefaultValueResult() {
    }

    public SaveTaskDefaultValueResult(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
