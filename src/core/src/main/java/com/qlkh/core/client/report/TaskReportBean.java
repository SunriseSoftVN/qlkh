/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.report;

import com.qlkh.core.client.model.Task;

import java.io.Serializable;

/**
 * The Class TaskReportBean.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/12, 7:32 PM
 */
public class TaskReportBean implements Serializable {

    private long id;
    private String name;
    private String code;
    private Integer quota;
    private Double defaultValue;
    private String unit;
    private int taskTypeCode;
    private String childTasks;
    private boolean dynamicQuota;

    private Integer quotaForPrinting;
    private Double defaultValueForPrinting;

    public TaskReportBean() {
    }

    public TaskReportBean(Task task) {
        id = task.getId();
        name = task.getName();
        code = task.getCode();
        defaultValue = task.getDefaultValue();
        unit = task.getUnit();
        quota = task.getQuota();
        taskTypeCode = task.getTaskTypeCode();
        childTasks = task.getChildTasks();
        dynamicQuota = task.isDynamicQuota();
        quotaForPrinting = task.getQuota();
        defaultValueForPrinting = task.getDefaultValue();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
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

    public boolean isDynamicQuota() {
        return dynamicQuota;
    }

    public void setDynamicQuota(boolean dynamicQuota) {
        this.dynamicQuota = dynamicQuota;
    }

    public Integer getQuotaForPrinting() {
        return quotaForPrinting;
    }

    public void setQuotaForPrinting(Integer quotaForPrinting) {
        this.quotaForPrinting = quotaForPrinting;
    }

    public Double getDefaultValueForPrinting() {
        return defaultValueForPrinting;
    }

    public void setDefaultValueForPrinting(Double defaultValueForPrinting) {
        this.defaultValueForPrinting = defaultValueForPrinting;
    }
}
