/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.model.view;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigInteger;

/**
 * The Class TaskDetailView.
 *
 * @author Nguyen Duc Dung
 * @since 6/7/12, 8:59 AM
 */
public class TaskDetailDataView implements IsSerializable {
    private long taskId;
    private long taskDetailId;
    private long stationId;
    private boolean annual;
    private int year;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(BigInteger taskId) {
        this.taskId = taskId.longValue();
    }

    public long getTaskDetailId() {
        return taskDetailId;
    }

    public void setTaskDetailId(BigInteger taskDetailId) {
        this.taskDetailId = taskDetailId.longValue();
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(BigInteger stationId) {
        this.stationId = stationId.longValue();
    }

    public boolean isAnnual() {
        return annual;
    }

    public void setAnnual(boolean annual) {
        this.annual = annual;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
