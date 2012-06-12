/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.model.view;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigInteger;

/**
 * The Class SunAnnualTaskDetailView.
 *
 * @author Nguyen Duc Dung
 * @since 6/7/12, 9:56 AM
 */
public class TaskDetailDKDataView implements IsSerializable {

    private long taskId;
    private long subTaskId;
    private long branchId;
    private long stationId;
    private int taskTypeCode;
    private Double lastYearValue;
    private Double increaseValue;
    private Double decreaseValue;
    private int year;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(BigInteger taskId) {
        this.taskId = taskId.longValue();
    }

    public long getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(BigInteger subTaskId) {
        this.subTaskId = subTaskId.longValue();
    }

    public long getBranchId() {
        return branchId;
    }

    public void setBranchId(BigInteger branchId) {
        this.branchId = branchId.longValue();
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(BigInteger stationId) {
        this.stationId = stationId.longValue();
    }

    public Double getLastYearValue() {
        return lastYearValue;
    }

    public void setLastYearValue(Double lastYearValue) {
        this.lastYearValue = lastYearValue;
    }

    public Double getIncreaseValue() {
        return increaseValue;
    }

    public void setIncreaseValue(Double increaseValue) {
        this.increaseValue = increaseValue;
    }

    public Double getDecreaseValue() {
        return decreaseValue;
    }

    public void setDecreaseValue(Double decreaseValue) {
        this.decreaseValue = decreaseValue;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTaskTypeCode() {
        return taskTypeCode;
    }

    public void setTaskTypeCode(int taskTypeCode) {
        this.taskTypeCode = taskTypeCode;
    }

    public Double getRealValue() {
        if (lastYearValue == null && increaseValue == null
                && decreaseValue == null) {
            return null;
        }
        if (increaseValue == null) {
            increaseValue = 0d;
        }
        if (decreaseValue == null) {
            decreaseValue = 0d;
        }
        if (lastYearValue == null) {
            lastYearValue = 0d;
        }
        return lastYearValue + increaseValue - decreaseValue;
    }


}
