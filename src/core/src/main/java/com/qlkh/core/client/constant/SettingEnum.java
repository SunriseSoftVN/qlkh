package com.qlkh.core.client.constant;

public enum SettingEnum {

    YEAR("YEAR"),
    COMPANY_NAME("COMPANY_NAME"),
    HIEN_DS_TNND("HIEN_DS_TNND");

    private String name;

    SettingEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
