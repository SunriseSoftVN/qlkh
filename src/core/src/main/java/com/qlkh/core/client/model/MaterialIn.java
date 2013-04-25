package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

/**
 * The Class MaterialIn.
 *
 * @author Nguyen Duc Dung
 * @since 4/26/13 1:27 AM
 */
public class MaterialIn extends AbstractEntity {

    private String code;
    private Double total;
    private Double weight;
    private int quarter;
    private int year;
    private MaterialGroup materialGroup;
    private MaterialPerson materialPerson;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public MaterialGroup getMaterialGroup() {
        return materialGroup;
    }

    public void setMaterialGroup(MaterialGroup materialGroup) {
        this.materialGroup = materialGroup;
    }

    public MaterialPerson getMaterialPerson() {
        return materialPerson;
    }

    public void setMaterialPerson(MaterialPerson materialPerson) {
        this.materialPerson = materialPerson;
    }
}
