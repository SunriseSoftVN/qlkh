/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

/**
 * The Class StationLock.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/12, 9:18 PM
 */
public class StationLock extends AbstractEntity {

    private Station station;
    private int code;

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
