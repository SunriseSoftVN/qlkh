/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.business.rule;

/**
 * The Class StationCodeEnum.
 *
 * @author Nguyen Duc Dung
 * @since 5/30/12, 3:23 PM
 */
public enum StationCodeEnum {

    COMPANY(27l),
    CAUGIAT(40l),
    TN_FOR_REPORT(999998l),
    ND_FOR_REPORT(999999l);

    private long id;

    StationCodeEnum(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
