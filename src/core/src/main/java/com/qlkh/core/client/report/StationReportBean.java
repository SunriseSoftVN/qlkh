/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.report;

import java.io.Serializable;

/**
 * The Class StationReportBean.
 *
 * @author Nguyen Duc Dung
 * @since 3/7/12, 2:10 PM
 */
public class StationReportBean implements Serializable {

    private long id;
    private String name;
    private Double value;
    private Double time;

    public StationReportBean() {
    }

    public StationReportBean(long id, String name) {
        this.name = name;
        this.id = id;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
