package com.qlkh.core.client.report;

import java.math.BigInteger;

/**
 * The Class MaterialReportBean.
 *
 * @author Nguyen Duc Dung
 * @since 5/25/13 10:39 PM
 */
public class MaterialReportBean {

    private Long materialId;
    private String name;
    private String code;
    private String unit;
    private Double require;
    private Double weight;
    private Double price;
    private Double total;

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(BigInteger materialId) {
        this.materialId = materialId.longValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getRequire() {
        return require;
    }

    public void setRequire(Double require) {
        this.require = require;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
