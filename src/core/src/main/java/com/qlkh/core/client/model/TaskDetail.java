/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

/**
 * The Class TaskDetail.
 *
 * @author Nguyen Duc Dung
 * @since 1/1/12, 3:28 PM
 */
public class TaskDetail extends AbstractEntity {

    private Task task;
    private int year;
    private Station station;
    private Boolean annual;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Boolean getAnnual() {
        return annual;
    }

    public void setAnnual(Boolean annual) {
        this.annual = annual;
    }
}
