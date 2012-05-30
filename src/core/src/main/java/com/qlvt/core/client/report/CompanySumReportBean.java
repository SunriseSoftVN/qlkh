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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class CompanySumReportBean.
 *
 * @author Nguyen Duc Dung
 * @since 3/7/12, 2:09 PM
 */
public class CompanySumReportBean implements Serializable, Comparable<CompanySumReportBean> {

    private Task task;
    private boolean calculated;
    private Map<String, StationReportBean> stations = new HashMap<String, StationReportBean>();
    private List<CompanySumReportBean> childBeans = new ArrayList<CompanySumReportBean>();

    public void calculate() {
        if (!isCalculated()) {
            for (CompanySumReportBean bean : childBeans) {
                bean.calculate();
            }

            for (StationReportBean station : stations.values()) {
                Double time = station.getTime();
                Double value = station.getValue();
                if (time == null) {
                    time = 0D;
                }
                if (value == null) {
                    value = 0D;
                }
                for (CompanySumReportBean bean : childBeans) {
                    Double childTime = bean.getStations().get(String.valueOf(station.getId())).getTime();
                    Double childValue = bean.getStations().get(String.valueOf(station.getId())).getValue();
                    if (childTime != null) {
                        time += childTime;
                    }
                    if (childValue != null) {
                        value += childValue;
                    }
                }
                if (value > 0) {
                    station.setValue(value);
                }
                if (time > 0) {
                    station.setTime(time);
                }
            }

            //Mark this bean is calculated. Avoid duplicate value.
            setCalculated(true);
        }
    }

    @Override
    public int compareTo(CompanySumReportBean bean) {
        if (task.getCode().length() > bean.getTask().getCode().length()) {
            return 1;
        } else {
            return -1;
        }
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

    public List<CompanySumReportBean> getChildBeans() {
        return childBeans;
    }

    public boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }
}
