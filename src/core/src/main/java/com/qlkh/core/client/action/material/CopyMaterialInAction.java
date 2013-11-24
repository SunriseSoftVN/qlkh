package com.qlkh.core.client.action.material;

import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class CopyMaterialInAction.
 *
 * @author Nguyen Duc Dung
 * @since 4/30/13 1:18 AM
 */
public class CopyMaterialInAction implements Action<CopyMaterialInResult> {

    private int year;
    private int quarter;
    private long stationId;

    public CopyMaterialInAction() {
    }

    public CopyMaterialInAction(int year) {
        this.year = year;
    }

    public CopyMaterialInAction(int year, int quarter, long stationId) {
        this.year = year;
        this.quarter = quarter;
        this.stationId = stationId;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
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
