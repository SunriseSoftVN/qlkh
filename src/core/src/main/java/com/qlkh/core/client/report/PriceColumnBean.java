package com.qlkh.core.client.report;

import java.io.Serializable;

/**
 * The Class PriceColumnBean.
 *
 * @author Nguyen Duc Dung
 * @since 4/18/13 10:12 AM
 */
public class PriceColumnBean implements Serializable {

    private long id;
    private Double taskWeight;
    private Double weight;
    private Double price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Double getTaskWeight() {
        return taskWeight;
    }

    public void setTaskWeight(Double taskWeight) {
        this.taskWeight = taskWeight;
    }
}
