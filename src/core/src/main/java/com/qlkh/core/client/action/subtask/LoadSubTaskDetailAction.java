/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.subtask;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadSubTaskDetailAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/2/12, 12:43 PM
 */
public class LoadSubTaskDetailAction implements Action<LoadSubTaskDetailResult> {

    private BasePagingLoadConfig loadConfig;
    private long taskId;
    private long stationId;

    public LoadSubTaskDetailAction() {
    }

    public LoadSubTaskDetailAction(BasePagingLoadConfig loadConfig, long taskId, long stationId) {
        this.loadConfig = loadConfig;
        this.taskId = taskId;
        this.stationId = stationId;
    }

    public BasePagingLoadConfig getLoadConfig() {
        return loadConfig;
    }

    public long getTaskId() {
        return taskId;
    }

    public long getStationId() {
        return stationId;
    }
}
