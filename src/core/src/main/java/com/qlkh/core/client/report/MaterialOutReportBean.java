package com.qlkh.core.client.report;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MaterialOutReportBean.
 *
 * @author Nguyen Duc Dung
 * @since 5/25/13 10:37 PM
 */
public class MaterialOutReportBean {

    private String personName;
    private String reason;
    private String stationName;
    private List<MaterialReportBean> materials = new ArrayList<MaterialReportBean>();

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public List<MaterialReportBean> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialReportBean> materials) {
        this.materials = materials;
    }
}
