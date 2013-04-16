package com.qlkh.core.client.report;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class PriceSumReportBean.
 *
 * @author Nguyen Duc Dung
 * @since 4/16/13 7:25 PM
 */
public class PriceSumReportBean implements Serializable {

    private Map<String, StationReportBean> stations = new HashMap<String, StationReportBean>();
    private MaterialReportBean material;

    public Map<String, StationReportBean> getStations() {
        return stations;
    }

    public void setStations(Map<String, StationReportBean> stations) {
        this.stations = stations;
    }

    public MaterialReportBean getMaterial() {
        return material;
    }

    public void setMaterial(MaterialReportBean material) {
        this.material = material;
    }
}
