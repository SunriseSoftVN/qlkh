package com.qlkh.core.client.model.view;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class TaskMaterial.
 *
 * @author Nguyen Duc Dung
 * @since 4/15/13 4:18 AM
 */
public class TaskMaterialDataView implements IsSerializable {
    private long taskId;
    private long materialId;
    private double quantity;
    private double price;
    private int year;
    private int quarter;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
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
}

