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
    Q1("QUÝ I", 1),
    Q2("QUÝ II", 2),
    Q3("QUÝ III", 3),
    Q4("QUÝ IV", 4),
    CA_NAM("", 0);

    private String name;
    private int value;

    ReportTypeEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
