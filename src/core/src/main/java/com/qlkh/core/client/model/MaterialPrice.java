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
    private double q1;
    private double q2;
    private double q3;
    private double q4;

    public double getQ1() {
        return q1;
    }

    public void setQ1(double q1) {
        this.q1 = q1;
    }

    public double getQ2() {
        return q2;
    }

    public void setQ2(double q2) {
        this.q2 = q2;
    }

    public double getQ3() {
        return q3;
    }

    public void setQ3(double q3) {
        this.q3 = q3;
    }

    public double getQ4() {
        return q4;
    }

    public void setQ4(double q4) {
        this.q4 = q4;
    }

    public Material getMaterial() {
        return material;
    }
}
