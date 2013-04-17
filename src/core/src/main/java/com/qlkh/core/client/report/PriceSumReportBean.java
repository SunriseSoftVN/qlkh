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

    private boolean isCalculated = false;

    public void calculate() {

        for (PriceSumReportBean child : childs) {
            if (!child.isCalculated) {
                child.calculate();
            }
        }

        for (StationReportBean station : stations.values()) {
            double parentWeight = 0d;
            double parentPrice = 0d;
            for (PriceSumReportBean child : childs) {
                if (child.getMaterial().getId() != null
                        && station.getValue() != null
                        && child.getMaterial().getQuantity() != null) {
                    double weight = station.getValue() * child.getMaterial().getQuantity();
                    double price = weight * child.getMaterial().getPrice();
                    StationReportBean childStation = new StationReportBean();
                    childStation.setId(station.getId());
                    childStation.setMaterialWeight(weight);
                    childStation.setMaterialPrice(price);
                    child.getStations().put(String.valueOf(station.getId()), childStation);
                    if (weight > 0) {
                        parentWeight += weight;
                        parentPrice += price;
                    }
                } else {
                    StationReportBean childStation = child.getStations().get(String.valueOf(station.getId()));
                    if (childStation != null && childStation.getMaterialPrice() != null && childStation.getMaterialWeight() != null) {
                        parentPrice += childStation.getMaterialPrice();
                        parentWeight += childStation.getMaterialWeight();
                    }
                }
            }

            if (parentWeight > 0) {
                station.setMaterialWeight(parentWeight);
                station.setMaterialPrice(parentPrice);
            }
        }

        //Remove duplicate material
        List<PriceSumReportBean> duplicateChilds = new ArrayList<>();
        for (int i = 0; i < childs.size() - 1; i++) {
            PriceSumReportBean child1 = childs.get(i);
            for (int j = i + 1; j < childs.size(); j++) {
                PriceSumReportBean child2 = childs.get(j);
                if (child1.getMaterial().getId() != null
                        && child1.getMaterial().getId().equals(child2.getMaterial().getId())) {
                    duplicateChilds.add(child2);

                    // Sum up
                    for (StationReportBean station1 : child1.getStations().values()) {
                        StationReportBean station2 = child2.getStations().get(String.valueOf(station1.getId()));
                        if (station2 != null && station2.getMaterialWeight() != null) {
                            if (station1.getMaterialWeight() != null) {
                                double weight = station1.getMaterialWeight() + station2.getMaterialWeight();
                                station1.setMaterialWeight(weight);
                            } else {
                                station1.setMaterialWeight(station2.getMaterialWeight());
                            }
                        }
                    }
                }
            }
        }

        childs.removeAll(duplicateChilds);

        isCalculated = true;
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
