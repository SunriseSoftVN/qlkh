/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.model.view;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigInteger;

/**
 * The Class SubTaskDetailView.
 *
 * @author Nguyen Duc Dung
 * @since 6/6/12, 7:57 PM
 */
public class SubTaskDetailDataView implements IsSerializable {

    private long taskId;
    private long taskDetailId;
    private long stationId;
    private long subTaskId;
    private long branchId;
    private int year;
    private boolean annual;
    private Double q1;
    private Double q2;
    private Double q3;
    private Double q4;

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

    public Double getQ1() {
        return q1;
    }

    public void setQ1(Double q1) {
        this.q1 = q1;
    }

    public Double getQ2() {
        return q2;
    }

    public void setQ2(Double q2) {
        this.q2 = q2;
    }

    public Double getQ3() {
        return q3;
    }

    public void setQ3(Double q3) {
        this.q3 = q3;
    }

    public Double getQ4() {
        return q4;
    }

    public void setQ4(Double q4) {
        this.q4 = q4;
    }
}
