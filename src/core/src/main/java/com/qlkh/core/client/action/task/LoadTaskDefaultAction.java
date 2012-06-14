/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.task;

import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadTaskDefaultAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/12, 12:57 AM
 */
public class LoadTaskDefaultAction implements Action<LoadTaskDefaultResult> {

    private long taskId;

    public LoadTaskDefaultAction() {
    }

    public LoadTaskDefaultAction(long taskId) {
        this.taskId = taskId;
    }

    public long getTaskId() {
        return taskId;
    }
}
