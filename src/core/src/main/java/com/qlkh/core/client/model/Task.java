/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

/**
 * The Class Task.
 *
 * @author Nguyen Duc Dung
 * @since 12/31/11, 2:28 PM
 */
public class Task extends AbstractEntity {

    private String name;
    private String code;
    private Double defaultValue;
    private String unit;
    private int quota;
    private int taskTypeCode;
    private String childTasks;
    private boolean dynamicDefaultValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Double defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public int getTaskTypeCode() {
        return taskTypeCode;
    }

    public void setTaskTypeCode(int taskTypeCode) {
        this.taskTypeCode = taskTypeCode;
    }

    public String getChildTasks() {
        return childTasks;
    }

    public void setChildTasks(String childTasks) {
        this.childTasks = childTasks;
    }

    public boolean isDynamicDefaultValue() {
        return dynamicDefaultValue;
    }

    public void setDynamicDefaultValue(boolean dynamicDefaultValue) {
        this.dynamicDefaultValue = dynamicDefaultValue;
    }
}
