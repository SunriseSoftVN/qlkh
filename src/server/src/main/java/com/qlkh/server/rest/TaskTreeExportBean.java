package com.qlkh.server.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dungvn3000 on 5/27/2014.
 */
public class TaskTreeExportBean {

    private long id;
    private String code;
    private String name;
    private String donVi;
    private Double dinhMuc;
    private Integer soLan;
    private boolean isHidden;

    private List<TaskTreeExportBean> children = new ArrayList<TaskTreeExportBean>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<TaskTreeExportBean> getChildren() {
        return children;
    }

    public String getDonVi() {
        return donVi;
    }

    public Double getDinhMuc() {
        return dinhMuc;
    }

    public void setDinhMuc(Double dinhMuc) {
        this.dinhMuc = dinhMuc;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSoLan() {
        return soLan;
    }

    public void setSoLan(Integer soLan) {
        this.soLan = soLan;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }
}
