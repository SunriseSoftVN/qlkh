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

import com.qlvt.core.client.model.TaskDetail;

/**
 * The Class SubTaskDetailDto.
 *
 * @author Nguyen Duc Dung
 * @since 1/3/12, 8:44 PM
 */
public class SubTaskDetailDto extends AbstractDto {

    public TaskDetail getTaskDetail() {
        return get("taskDetail");
    }

    public void setTaskDetail(TaskDetail taskDetail) {
        set("taskDetail", taskDetail);
    }

    public Double getQ1() {
        return get("q1");
    }

    public void setQ1(Double q1) {
        set("q1", q1);
    }

    public Double getQ2() {
        return get("q2");
    }

    public void setQ2(Double q2) {
        set("q2", q2);
    }

    public Double getQ3() {
        return get("q3");
    }

    public void setQ3(Double q3) {
        set("q3", q3);
    }

    public Double getQ4() {
        return get("q4");
    }

    public void setQ4(Double q4) {
        set("q4", q4);
    }
}
