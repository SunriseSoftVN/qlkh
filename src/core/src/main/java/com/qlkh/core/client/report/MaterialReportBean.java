package com.qlkh.core.client.report;

import java.io.Serializable;

/**
 * The Class MaterialReportBean.
 *
 * @author Nguyen Duc Dung
 * @since 4/16/13 10:23 PM
 */
public class MaterialReportBean implements Serializable {

    private String name;
    private String stt;
    private String unit;
    private String price;
    private String start;
    private String end;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
