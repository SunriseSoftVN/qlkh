/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

/**
 * The Class TaskDefaultValue.
 *
 * @author Nguyen Duc Dung
 * @since 6/14/12, 8:59 PM
 */
public class TaskDefaultValue extends AbstractEntity {

    private double defaultValue;
    private Task task;

    public double getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(double defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
