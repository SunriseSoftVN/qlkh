package com.qlkh.core.client.report;

import com.qlkh.core.client.model.view.TaskMaterialDataView;

import java.io.Serializable;

/**
 * The Class MaterialReportBean.
 *
 * @author Nguyen Duc Dung
 * @since 4/16/13 10:23 PM
 */
public class MaterialReportBean implements Serializable {

    private String name;
    private String stt;
    private String unit;
    private Double price;
    private Double quantity;

    private String[] range = new String[0];

    public MaterialReportBean() {
    }

    public MaterialReportBean(TaskMaterialDataView taskMaterialDataView) {
        this.name = taskMaterialDataView.getName();
        this.unit = taskMaterialDataView.getUnit();
        this.price = taskMaterialDataView.getPrice();
        this.quantity = taskMaterialDataView.getQuantity();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String[] getRange() {
        return range;
    }

    public void setRange(String[] range) {
        this.range = range;
    }

    public Double getQuantity() {
        return quantity;
    }
}
