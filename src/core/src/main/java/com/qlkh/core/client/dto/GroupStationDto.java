package com.qlkh.core.client.dto;

import com.extjs.gxt.ui.client.data.BeanModelTag;

import java.io.Serializable;

/**
 * The Class GroupStationDto.
 *
 * @author Nguyen Duc Dung
 * @since 7/26/13 2:51 PM
 */
public class GroupStationDto implements Serializable, BeanModelTag {

    private long id;
    private String name;
    private boolean isStation = true;

    public boolean isStation() {
        return isStation;
    }

    public void setStation(boolean station) {
        isStation = station;
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
}
