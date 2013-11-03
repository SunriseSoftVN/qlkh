package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadMaterialInAction.
 *
 * @author Nguyen Duc Dung
 * @since 11/2/13 11:59 AM
 */
public class LoadMaterialInAction implements Action<LoadMaterialInResult> {

    private BasePagingLoadConfig loadConfig;
    private Long stationId;
    private Long groupId;
    private int year;
    private int quarter;


    public LoadMaterialInAction() {
    }


    public BasePagingLoadConfig getLoadConfig() {
        return loadConfig;
    }

    public void setLoadConfig(BasePagingLoadConfig loadConfig) {
        this.loadConfig = loadConfig;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }
}
