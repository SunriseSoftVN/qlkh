package com.qlkh.server.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dungvn3000 on 5/27/2014.
 */
public class TaskTreeExportBean {

    private long id;
    private String name;
    private String donVi;
    private Double dinhMuc;
    private Integer soLan;
    private int taskTypeCode;
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

    public int getTaskTypeCode() {
        return taskTypeCode;
    }

    public void setTaskTypeCode(int taskTypeCode) {
        this.taskTypeCode = taskTypeCode;
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
}
