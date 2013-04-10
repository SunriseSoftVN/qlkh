package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

/**
 * The Class MaterialPrice.
 *
 * @author Nguyen Duc Dung
 * @since 4/5/13 11:04 AM
 */
public class MaterialPrice extends AbstractEntity {

    private Material material;
    private int year;
    private int quarter;
    private double price;

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
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

    public double getPrice() {
        return price;
    }
}
