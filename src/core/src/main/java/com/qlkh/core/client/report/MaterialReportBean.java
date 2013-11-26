package com.qlkh.core.client.report;

import java.math.BigInteger;
import java.util.Date;

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
    private String personName;
    private String stationName;
    private String groupName;
    private String reason;
    private long reportCode;
    private Date exportDate;

    public long getReportCode() {
        return reportCode;
    }

    public void setReportCode(BigInteger reportCode) {
        this.reportCode = reportCode.longValue();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

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

    public Double getMoney() {
        if (price != null && weight != null) {
            return price * weight;
        }
        return null;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
