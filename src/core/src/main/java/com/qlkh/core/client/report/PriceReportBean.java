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
public class PriceReportBean implements Serializable {

    private Map<String, PriceColumnBean> columns = new HashMap<String, PriceColumnBean>();
    private List<PriceReportBean> children = new ArrayList<PriceReportBean>();
    private String[] regex = new String[0];

    private String stt;
    private String code;
    private String name;
    private String unit;
    private Double price;
    private double quantity;
    private long materialId;
    private long taskId;

    public PriceReportBean() {
    }

    public PriceReportBean(String stt, String code, String name, String... regex) {
        this.regex = regex;
        this.stt = stt;
        this.code = code;
        this.name = name;
    }

    public void calculate() {
        for (PriceReportBean child : children) {
            child.calculate();
        }

        if (children.isEmpty()) {
            for (PriceColumnBean column : columns.values()) {
                if (column.getTaskWeight() != null && price != null) {
                    double weight = column.getTaskWeight() * quantity;
                    double price = weight * this.price;
                    column.setWeight(weight);
                    column.setPrice(price);
                }
            }
        } else {

        }
    }

    public Map<String, PriceColumnBean> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, PriceColumnBean> columns) {
        this.columns = columns;
    }

    public List<PriceReportBean> getChildren() {
        return children;
    }

    public void setChildren(List<PriceReportBean> children) {
        this.children = children;
    }

    public String[] getRegex() {
        return regex;
    }

    public void setRegex(String[] regex) {
        this.regex = regex;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
}
