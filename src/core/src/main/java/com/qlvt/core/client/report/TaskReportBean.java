/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.qlvt.core.client.report;

import com.qlvt.core.client.model.Task;

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
    private Double defaultValue;
    private String unit;
    private Integer quota;
    private int taskTypeCode;
    private String childTasks;

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
}
