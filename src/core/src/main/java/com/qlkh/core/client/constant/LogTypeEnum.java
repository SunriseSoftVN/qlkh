/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.constant;

/**
 * The Class LogTypeEnum.
 *
 * @author Nguyen Duc Dung
 * @since 6/3/12, 11:13 AM
 */
public enum LogTypeEnum {
    ERROR(0),
    DEBUG(1),
    INFO(2);

    private int code;

    LogTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
