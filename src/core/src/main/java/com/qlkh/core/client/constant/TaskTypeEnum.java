/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.constant;

/**
 * The Class TaskTypeEnum.
 *
 * @author Nguyen Duc Dung
 * @since 2/13/12, 5:03 PM
 */
public enum TaskTypeEnum {

    DK(3),
    KDK(0),
    NAM(4),
    SUM(1),
    SUBSUM(2),
    DOTXUAT(5);

    private int code;

    TaskTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TaskTypeEnum valueOf(int taskType) {
        for (TaskTypeEnum type : values()) {
            if (type.getCode() == taskType) {
                return type;
            }
        }
        return null;
    }
}
