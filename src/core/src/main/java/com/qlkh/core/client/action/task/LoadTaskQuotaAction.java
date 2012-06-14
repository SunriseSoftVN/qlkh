/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.task;

import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadTaskQuotaAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/12, 3:12 AM
 */
public class LoadTaskQuotaAction implements Action<LoadTaskQuotaResult> {

    private long taskId;

    public LoadTaskQuotaAction() {
    }

    public LoadTaskQuotaAction(long taskId) {
        this.taskId = taskId;
    }

    public long getTaskId() {
        return taskId;
    }
}
