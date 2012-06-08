/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.constant;

/**
 * The Class LockTypeEnum.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/12, 9:28 PM
 */
public enum StationLockTypeEnum {

    DK(0),
    KDK_Q1(1),
    KDK_Q2(2),
    KDK_Q3(3),
    KDK_Q4(4);

    private int code;

    StationLockTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
