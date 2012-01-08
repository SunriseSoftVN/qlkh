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

package com.qlvt.core.client.dto;

import com.qlvt.core.client.model.Station;

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
