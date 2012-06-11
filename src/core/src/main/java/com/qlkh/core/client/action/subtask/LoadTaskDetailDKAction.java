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
public class LoadTaskDetailDKAction implements Action<LoadTaskDetailDKResult> {

    private BasePagingLoadConfig loadConfig;
    private long taskId;
    private long stationId;

    public LoadTaskDetailDKAction() {
    }

    public LoadTaskDetailDKAction(BasePagingLoadConfig loadConfig,
                                  long taskId, long stationId) {
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
