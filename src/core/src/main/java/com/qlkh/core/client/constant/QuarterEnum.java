package com.qlkh.core.client.constant;

/**
 * The Class QuarterEnum.
 *
 * @author Nguyen Duc Dung
 * @since 4/10/13 8:45 AM
 */
public enum QuarterEnum {

    Q1("Q1", 1),
    Q2("Q2", 2),
    Q3("Q3", 3),
    Q4("Q4", 4);

    private String value;
    private int code;

    QuarterEnum(String value, int code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }

    public static QuarterEnum valueOf(int code) {
        for (QuarterEnum quarter : values()) {
            if (quarter.getCode() == code) {
                return quarter;
            }
        }
        return null;
    }
}
