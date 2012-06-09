/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

import javax.persistence.Transient;

/**
 * The Class SubTaskAnnualDetail.
 *
 * @author Nguyen Duc Dung
 * @since 1/3/12, 9:00 AM
 */
public class SubTaskAnnualDetail extends AbstractEntity {

    private TaskDetail taskDetail;
    private Branch branch;
    private Double lastYearValue;
    private Double increaseValue;
    private Double decreaseValue;

    public TaskDetail getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(TaskDetail taskDetail) {
        this.taskDetail = taskDetail;
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

    @Transient
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
