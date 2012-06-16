/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.task;

import com.qlkh.core.client.model.Task;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class SaveTaskDefaultValueAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/12, 10:27 PM
 */
public class SaveTaskDefaultValueAction implements Action<SaveTaskDefaultValueResult> {

    private Task task;
    private double defaultValue;

    public SaveTaskDefaultValueAction() {
    }

    public SaveTaskDefaultValueAction(Task task, double defaultValue) {
        this.task = task;
        this.defaultValue = defaultValue;
    }

    public Task getTask() {
        return task;
    }

    public double getDefaultValue() {
        return defaultValue;
    }
}
