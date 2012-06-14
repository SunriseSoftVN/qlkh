/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.task;

import com.qlkh.core.client.model.TaskQuota;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class LoadTaskQuotaResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/12, 3:12 AM
 */
public class LoadTaskQuotaResult implements Result {

    private TaskQuota taskQuota;

    public LoadTaskQuotaResult() {
    }

    public LoadTaskQuotaResult(TaskQuota taskQuota) {
        this.taskQuota = taskQuota;
    }

    public TaskQuota getTaskQuota() {
        return taskQuota;
    }
}
