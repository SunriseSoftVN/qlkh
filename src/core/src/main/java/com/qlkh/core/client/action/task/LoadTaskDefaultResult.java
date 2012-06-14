/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.task;

import com.qlkh.core.client.model.TaskDefaultValue;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class LoadTaskDefaultResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/12, 12:58 AM
 */
public class LoadTaskDefaultResult implements Result {

    private TaskDefaultValue taskDefaultValue;

    public LoadTaskDefaultResult() {
    }

    public LoadTaskDefaultResult(TaskDefaultValue taskDefaultValue) {
        this.taskDefaultValue = taskDefaultValue;
    }

    public TaskDefaultValue getTaskDefaultValue() {
        return taskDefaultValue;
    }
}
