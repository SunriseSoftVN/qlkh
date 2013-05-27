package com.qlkh.server.util;

import org.junit.Test;

/**
 * The Class TestMoneyConverter.
 *
 * @author Nguyen Duc Dung
 * @since 5/27/13 6:50 PM
 */
public class TestMoneyConverter {

    @Test
    public void testMoneyConverter() {
        Long[] data = new Long[]{
                9000034000l, 91100034001l, 95152134111l, 901152134111l,
                10000000000l, 11000000000l, 11100000000l,
                1l, 10l, 11l, 15l, 101l, 155l, 1001l, 21l, 21125l, 100001l, 1000000l,
                10000000l,
                12345678l,
                11000000l,
                123456789l,
                1234567890l,
                100000000l,
                120000000l,
                1000000000l,
                1200000000l,
        };
        for (Long money : data) {
            String result = MoneyConverter.transformNumber(money.toString());
            System.out.println(money + " = " + result);
        }
    }

}
