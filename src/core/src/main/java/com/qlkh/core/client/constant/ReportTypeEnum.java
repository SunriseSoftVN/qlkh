/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.constant;

/**
 * The Class ReportTypeEnum.
 *
 * @author Nguyen Duc Dung
 * @since 3/12/12, 1:50 PM
 */
public enum ReportTypeEnum {
    Q1("QUÝ I"),
    Q2("QUÝ II"),
    Q3("QUÝ III"),
    Q4("QUÝ IV"),
    CA_NAM("");

    private String value;

    ReportTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
