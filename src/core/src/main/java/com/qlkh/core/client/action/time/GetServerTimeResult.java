/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.time;

import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class GerServerTimeResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/2/12, 4:00 PM
 */
public class GetServerTimeResult implements Result {
    private int year;
    private int quarter;

    public GetServerTimeResult() {
    }

    public GetServerTimeResult(int year, int quarter) {
        this.year = year;
        this.quarter = quarter;
    }

    public int getYear() {
        return year;
    }

    public int getQuarter() {
        return quarter;
    }
}
