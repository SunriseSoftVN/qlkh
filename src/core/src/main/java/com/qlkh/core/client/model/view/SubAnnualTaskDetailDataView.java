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
public class SubAnnualTaskDetailDataView implements IsSerializable {

    private long taskId;
    private long taskDetailId;
    private long stationId;
    private int year;
    private boolean annual;
    private long subTaskId;
    private long branchId;
    private Double lastYearValue;
    private Double increaseValue;
    private Double decreaseValue;
    private Double realValue;

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isAnnual() {
        return annual;
    }

    public void setAnnual(boolean annual) {
        this.annual = annual;
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

    public Double getRealValue() {
        return realValue;
    }

    public void setRealValue(Double realValue) {
        this.realValue = realValue;
    }
}
