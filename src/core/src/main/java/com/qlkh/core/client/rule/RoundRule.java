package com.qlkh.core.client.rule;

/**
 * The Class RoundRule.
 *
 * @author Nguyen Duc Dung
 * @since 7/15/13 1:27 PM
 */
public final class RoundRule {

    public static String[] R_UNIT = new String[]{
            "cái", "bộ", "viên", "cuộn", "thanh"
    };

    public static boolean shouldRoundUp(String unit) {
        boolean found = false;
        for (String v : R_UNIT) {
            if (unit != null && v.toLowerCase().equals(unit.toLowerCase())) {
                found = true;
                break;
            }
        }
        return found;
    }

}
