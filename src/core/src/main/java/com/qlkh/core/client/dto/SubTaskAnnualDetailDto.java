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
