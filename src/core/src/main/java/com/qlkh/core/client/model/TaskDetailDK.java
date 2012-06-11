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
public class TaskDetailDK extends AbstractEntity {

    private Task task;
    private Branch branch;
    private Double lastYearValue;
    private Double increaseValue;
    private Double decreaseValue;

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
