package com.qlkh.server.rest;

/**
 * The Class TaskExportBean.
 *
 * @author Nguyen Duc Dung
 * @since 4/20/14 10:35 PM
 */
public class TaskExportBean {

    private long id;
    private Double khoiLuong;
    private Double gio;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getKhoiLuong() {
        return khoiLuong;
    }

    public void setKhoiLuong(Double khoiLuong) {
        this.khoiLuong = khoiLuong;
    }

    public Double getGio() {
        return gio;
    }

    public void setGio(Double gio) {
        this.gio = gio;
    }
}
