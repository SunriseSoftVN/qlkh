package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadMaterialWithTaskAction.
 *
 * @author Nguyen Duc Dung
 * @since 4/29/13 4:21 PM
 */
public class LoadMaterialWithTaskAction implements Action<LoadMaterialWithTaskResult> {

    private int quarter;
    private int year;
    private BasePagingLoadConfig loadConfig;

    public LoadMaterialWithTaskAction() {
    }

    public LoadMaterialWithTaskAction(BasePagingLoadConfig loadConfig, int quarter, int year) {
        this.quarter = quarter;
        this.year = year;
        this.loadConfig = loadConfig;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BasePagingLoadConfig getLoadConfig() {
        return loadConfig;
    }

    public void setLoadConfig(BasePagingLoadConfig loadConfig) {
        this.loadConfig = loadConfig;
    }
}
