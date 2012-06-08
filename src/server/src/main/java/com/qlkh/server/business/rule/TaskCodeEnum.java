/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.business.rule;

/**
 * The Class TaskCodeEnum.
 *
 * @author Nguyen Duc Dung
 * @since 5/30/12, 10:38 AM
 */
public enum TaskCodeEnum {
    A("A"),
    B("B"),
    C("C"),
    C1("C.1"),
    C2("C.2"),
    I("I"),
    II("II"),
    III("III"),
    IA("1.A"),
    IB("1.B"),
    IIA("2.A"),
    IIB("2.B"),
    IIIA("3.A"),
    IIIB("3.B"),
    IVA("4.A"),
    IVB("4.B"),
    VA("5.A"),
    VB("5.B"),
    VIA("6.A"),
    VIB("6.B"),
    VIIA("7.A"),
    VIIB("7.B"),
    VIIIA("8.A"),
    VIIIB("8.B"),
    VIVA("9.A"),
    VIVB("9.B"),
    XA("10.A"),
    XB("10.B");

    private String code;

    TaskCodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
