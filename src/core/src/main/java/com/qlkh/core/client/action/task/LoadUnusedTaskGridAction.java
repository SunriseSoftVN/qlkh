/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.task;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.qlkh.core.client.action.grid.ActionHasLoadConfig;
import com.qlkh.core.client.constant.TaskTypeEnum;

/**
 * The Class LoadUnusedTaskGridAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/12, 4:37 PM
 */
public class LoadUnusedTaskGridAction implements ActionHasLoadConfig<LoadUnusedTaskGridResult> {

    private BasePagingLoadConfig loadConfig;
    private long stationId;
    private TaskTypeEnum typeEnum;

    public LoadUnusedTaskGridAction() {
    }

    public LoadUnusedTaskGridAction(long stationId, TaskTypeEnum typeEnum) {
        this.stationId = stationId;
        this.typeEnum = typeEnum;
    }

    @Override
    public BasePagingLoadConfig getConfig() {
        return loadConfig;
    }

    @Override
    public void setConfig(BasePagingLoadConfig config) {
        this.loadConfig = config;
    }

    public TaskTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public long getStationId() {
        return stationId;
    }
}
