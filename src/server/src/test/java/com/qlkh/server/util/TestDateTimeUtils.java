/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * The Class TestDateTimeUtils.
 *
 * @author Nguyen Duc Dung
 * @since 6/18/12, 2:53 PM
 */
public class TestDateTimeUtils {

    @Test
    public void testGetDateForQuarter() {
        long day = DateTimeUtils.getDateForQuarter(1, 2012);
        Assert.assertEquals(day, 91);

        day = DateTimeUtils.getDateForQuarter(2, 2012);
        Assert.assertEquals(day, 91);

        day = DateTimeUtils.getDateForQuarter(3, 2012);
        Assert.assertEquals(day, 92);

        day = DateTimeUtils.getDateForQuarter(4, 2012);
        Assert.assertEquals(day, 92);
    }

}
