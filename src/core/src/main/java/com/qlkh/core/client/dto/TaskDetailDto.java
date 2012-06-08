/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.dto;

import com.qlkh.core.client.model.Station;

/**
 * The Class TaskDetailDto.
 *
 * @author Nguyen Duc Dung
 * @since 1/3/12, 9:19 AM
 */
public class TaskDetailDto extends AbstractDto {

    /**
     * This variable declare for Serializable.
     */
    private TaskDto taskDto;

    /**
     * This variable declare for Serializable.
     */
    private SubTaskDetailDto subTaskDetailDto;

    /**
     * This variable declare for Serializable.
     */
    private SubTaskAnnualDetailDto subTaskAnnualDetailDto;

    /**
     * This variable declare for Serializable.
     */
    private Station station;

    public TaskDto getTask() {
        return get("task");
    }

    public void setTask(TaskDto task) {
        set("task", task);
    }

    public Integer getYear() {
        return get("year");
    }

    public void setYear(Integer year) {
        set("year", year);
    }

    public Station getStation() {
        return get("station");
    }

    public void setStation(Station station) {
        set("station", station);
    }

    public Boolean getAnnual() {
        return get("annual");
    }

    public void setAnnual(Boolean annual) {
        set("annual", annual);
    }
}
