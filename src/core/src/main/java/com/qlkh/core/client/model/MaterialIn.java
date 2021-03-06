package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

import java.util.Date;

/**
 * The Class MaterialIn.
 *
 * @author Nguyen Duc Dung
 * @since 4/26/13 1:27 AM
 */
public class MaterialIn extends AbstractEntity {

    private Integer code;
    private Double total;
    private Double weight;
    private int quarter;
    private int year;
    private MaterialGroup materialGroup;
    private MaterialPerson materialPerson;
    private Material material;
    private Station station;
    private Group group;
    private Date exportDate;

    //Only for display on grid.
    @SuppressWarnings("JpaAttributeMemberSignatureInspection")
    public Double getRemain() {
        if (total != null && weight != null) {
            return total - weight;
        }
        return null;
    }

    //Only for display on grid.
    @SuppressWarnings("JpaAttributeMemberSignatureInspection")
    public Double getTotalPrice() {
        if (material != null
                && material.getCurrentPrice() != null && weight != null) {
            return material.getCurrentPrice().getPrice() * weight;
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
