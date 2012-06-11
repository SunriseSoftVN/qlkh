/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.taskdetail;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadTaskDetailNamAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/11/12, 10:02 PM
 */
public class LoadTaskDetailNamAction implements Action<LoadTaskDetailNamResult> {

    private BasePagingLoadConfig loadConfig;
    private long taskId;
    private long stationId;

    public LoadTaskDetailNamAction() {
    }

    public LoadTaskDetailNamAction(BasePagingLoadConfig loadConfig, long taskId, long stationId) {
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
