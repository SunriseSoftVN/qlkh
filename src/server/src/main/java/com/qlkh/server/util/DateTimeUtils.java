/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.util;

import java.util.Calendar;

/**
 * The Class DateTimeUtils.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/12, 10:31 PM
 */
public final class DateTimeUtils {

    private static Calendar cal = Calendar.getInstance();

    private DateTimeUtils() {

    }

    public static int getCurrentYear() {
        return cal.get(Calendar.YEAR);
    }

    public static int getCurrentQuarter() {
        int month = cal.get(Calendar.MONTH);
        return month / 3 + 1;
    }

}
