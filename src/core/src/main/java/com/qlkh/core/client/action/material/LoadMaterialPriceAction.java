package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.qlkh.core.client.constant.QuarterEnum;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class MaterialPriceAction.
 *
 * @author Nguyen Duc Dung
 * @since 4/5/13 11:26 AM
 */
public class LoadMaterialPriceAction implements Action<LoadMaterialPriceResult> {

    private BasePagingLoadConfig loadConfig;
    private QuarterEnum quarter;
    private int year;

    public LoadMaterialPriceAction() {
    }

    public LoadMaterialPriceAction(BasePagingLoadConfig loadConfig, QuarterEnum quarter, int year) {
        assert quarter != null;
        this.loadConfig = loadConfig;
        this.quarter = quarter;
        this.year = year;
    }

    public BasePagingLoadConfig getLoadConfig() {
        return loadConfig;
    }

    public void setLoadConfig(BasePagingLoadConfig loadConfig) {
        this.loadConfig = loadConfig;
    }

    public QuarterEnum getQuarter() {
        return quarter;
    }

    public void setQuarter(QuarterEnum quarter) {
        this.quarter = quarter;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
