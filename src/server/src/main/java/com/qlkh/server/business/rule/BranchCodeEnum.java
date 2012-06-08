/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.business.rule;

/**
 * The Class BranchCodeEnum.
 *
 * @author Nguyen Duc Dung
 * @since 5/30/12, 3:50 PM
 */
public enum BranchCodeEnum {

    ND(25l);

    private long id;

    BranchCodeEnum(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
