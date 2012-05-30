/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.qlvt.server.business.rule;

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
