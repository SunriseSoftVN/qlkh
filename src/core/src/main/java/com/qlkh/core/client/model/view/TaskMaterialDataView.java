package com.qlkh.core.client.model.view;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigInteger;

/**
 * The Class TaskMaterial.
 *
 * @author Nguyen Duc Dung
 * @since 4/15/13 4:18 AM
 */
public class TaskMaterialDataView implements IsSerializable {
    private long taskId;
    private long materialId;
    private String code;
    private String name;
    private String unit;
    private double quantity;
    private double price;
    private int year;
    private int quarter;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(BigInteger taskId) {
        this.taskId = taskId.longValue();
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(BigInteger materialId) {
        this.materialId = materialId.longValue();
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

