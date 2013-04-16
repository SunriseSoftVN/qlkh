package com.qlkh.core.client.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class PriceSumReportBean.
 *
 * @author Nguyen Duc Dung
 * @since 4/16/13 7:25 PM
 */
public class PriceSumReportBean implements Serializable {

    private Map<String, StationReportBean> stations = new HashMap<>();
    private MaterialReportBean material;
    private List<PriceSumReportBean> childs = new ArrayList<>();

    public void calculate() {
        if (childs.isEmpty()) {
            for (StationReportBean station : stations.values()) {
                double weight = 0d;
                if (station.getValue() != null && material.getQuantity() != null) {
                    weight = station.getValue() * material.getQuantity();
                }
                station.setValue(weight);
            }
        } else {
            for (PriceSumReportBean child : childs) {
                child.calculate();
            }

            for (PriceSumReportBean child : childs) {
                for (StationReportBean station : child.getStations().values()) {
                    StationReportBean parentStation = stations.get(station.getId());
                    if (parentStation == null) {
                        stations.put(String.valueOf(station.getId()), station);
                    } else {
                        double weight = parentStation.getValue() + station.getValue();
                        parentStation.setValue(weight);
                    }
                }
            }

        }

    }

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

    public List<PriceSumReportBean> getChilds() {
        return childs;
    }
}
