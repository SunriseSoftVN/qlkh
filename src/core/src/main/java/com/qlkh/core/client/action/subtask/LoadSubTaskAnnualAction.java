/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.subtask;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadSubTaskAnnualAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 9:08 PM
 */
public class LoadSubTaskAnnualAction implements Action<LoadSubTaskAnnualResult> {

    private BasePagingLoadConfig loadConfig;
    private long taskDetailId;

    public LoadSubTaskAnnualAction() {
    }

    public LoadSubTaskAnnualAction(BasePagingLoadConfig loadConfig, long taskDetailId) {
        this.loadConfig = loadConfig;
        this.taskDetailId = taskDetailId;
    }

    public BasePagingLoadConfig getLoadConfig() {
        return loadConfig;
    }

    public long getTaskDetailId() {
        return taskDetailId;
    }
}
