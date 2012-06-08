/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.dto;

import com.qlkh.core.client.model.Branch;

/**
 * The Class SubTaskAnnualDetailDto.
 *
 * @author Nguyen Duc Dung
 * @since 1/4/12, 8:55 PM
 */
public class SubTaskAnnualDetailDto extends AbstractDto {

    /**
     * This variable declare for Serializable.
     */
    private TaskDetailDto taskDetailDto;

    public TaskDetailDto getTaskDetail() {
        return get("taskDetail");
    }

    public void setTaskDetail(TaskDetailDto taskDetail) {
        set("taskDetail", taskDetail);
    }

    public Branch getBranch() {
        return get("branch");
    }

    public void setBranch(Branch branch) {
        set("branch", branch);
    }

    public Integer getLastYear() {
        return get("lastYear");
    }

    public void setLastYear(Integer lastYear) {
        set("lastYear", lastYear);
    }

    public Integer getCurrentYear() {
        return get("currentYear");
    }

    public void setCurrentYear(Integer currentYear) {
        set("currentYear", currentYear);
    }

    public Double getLastYearValue() {
        return get("lastYearValue");
    }

    public void setLastYearValue(Double lastYearValue) {
        set("lastYearValue", lastYearValue);
    }

    public Double getIncreaseValue() {
        return get("increaseValue");
    }

    public void setIncreaseValue(Double increaseValue) {
        set("increaseValue", increaseValue);
    }

    public Double getDecreaseValue() {
        return get("decreaseValue");
    }

    public void setDecreaseValue(Double decreaseValue) {
        set("decreaseValue", decreaseValue);
    }

    public Double getRealValue() {
        return get("realValue");
    }

    public void setRealValue(Double realValue) {
        set("realValue", realValue);
    }
}
