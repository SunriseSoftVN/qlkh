/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.report;

import com.qlkh.core.client.model.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class SumReportBean.
 *
 * @author Nguyen Duc Dung
 * @since 3/7/12, 2:09 PM
 */
public class TaskSumReportBean implements Serializable, Comparable<TaskSumReportBean> {

    private TaskReportBean task;
    private boolean calculated;

    private Map<String, StationReportBean> stations = new HashMap<String, StationReportBean>();
    private List<TaskSumReportBean> childBeans = new ArrayList<TaskSumReportBean>();

    public void calculate() {
        if (!isCalculated()) {
            for (TaskSumReportBean bean : childBeans) {
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
                for (TaskSumReportBean bean : childBeans) {
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
    public int compareTo(TaskSumReportBean bean) {
        if (task.getCode().length() > bean.getTask().getCode().length()) {
            return 1;
        } else {
            return -1;
        }
    }

    public TaskReportBean getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = new TaskReportBean(task);
    }

    public Map<String, StationReportBean> getStations() {
        return stations;
    }

    public List<TaskSumReportBean> getChildBeans() {
        return childBeans;
    }

    public boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }
}
