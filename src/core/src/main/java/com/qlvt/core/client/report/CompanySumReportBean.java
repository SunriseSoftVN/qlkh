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
import java.util.HashMap;
import java.util.Map;

/**
 * The Class CompanySumReportBean.
 *
 * @author Nguyen Duc Dung
 * @since 3/7/12, 2:09 PM
 */
public class CompanySumReportBean implements Serializable {

    private int stt;
    private Task task;
    private Map<String, StationReportBean> stations = new HashMap<String, StationReportBean>();

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Map<String, StationReportBean> getStations() {
        return stations;
    }

    public void setStations(Map<String, StationReportBean> stations) {
        this.stations = stations;
    }
}
