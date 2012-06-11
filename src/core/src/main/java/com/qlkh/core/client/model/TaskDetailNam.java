/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

/**
 * The Class SubTaskAnnualDetail.
 *
 * @author Nguyen Duc Dung
 * @since 1/3/12, 9:00 AM
 */
public class TaskDetailNam extends AbstractEntity {

    private Task task;
    private Branch branch;
    private Double lastYearValue;
    private Double increaseValue;
    private Double decreaseValue;
    private Double q1;
    private Double q2;
    private Double q3;
    private Double q4;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
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

    @SuppressWarnings("JpaAttributeMemberSignatureInspection")
    public Double getRealValue() {
        Double lastYearValue = getLastYearValue();
        Double increaseValue = getIncreaseValue();
        Double decreaseValue = getDecreaseValue();
        if (lastYearValue == null && increaseValue == null
                && decreaseValue == null) {
            return null;
        }
        if (increaseValue == null) {
            increaseValue = 0D;
        }
        if (decreaseValue == null) {
            decreaseValue = 0D;
        }
        if (lastYearValue == null) {
            lastYearValue = 0D;
        }
        return lastYearValue + increaseValue - decreaseValue;
    }
}
