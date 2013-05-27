package com.qlkh.server.util;

import org.springframework.util.StringUtils;

/**
 * The Class MoneyConverter.
 *
 * @author Nguyen Duc Dung
 * @since 5/27/13 6:42 PM
 */
public class MoneyConverter {

    private MoneyConverter() {

    }

    public static String transformNumber(String number) {
        if (number == null || number.trim().length() == 0) {
            return "";
        }
        int l = number.length();
        int t = l - 1;
        String[] dv = new String[]{"ngàn", "triệu", "tỷ", "nghìn tỷ", "triệu tỷ"};
        String[] tl = new String[]{"muơi", "trăm"};
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < l; i++) {
            buffer.append(number.charAt(i)).append(" ");
            int k = (t - 1) / 3;
            if (k >= 0 && t > 0) {
                if (t % 3 == 0) {
                    buffer.append(dv[k]).append(" ");
                } else {
                    buffer.append(tl[(t % 3) - 1]).append(" ");
                }
                t--;
            }
        }
        number = buffer.toString();
        // truong hop dc biet
        number = number.replaceAll("muơi 0", "muơi").replaceAll("0 muơi", "lẻ");

        // thay so
        number = number.replaceAll("1", "một")
                .replaceAll("2", "hai")
                .replaceAll("3", "ba")
                .replaceAll("4", "bốn")
                .replaceAll("5", "năm")
                .replaceAll("6", "sáu")
                .replaceAll("7", "bảy")
                .replaceAll("8", "tám")
                .replaceAll("9", "chín")
                .replaceAll("0", "không");

        // truong hop dc biet
        number = number.replaceAll("một muơi", "muời");
        number = number.replaceAll("muơi năm", "muơi lăm");
        number = number.replaceAll("muời năm", "muời lăm");
        number = number.replaceAll("muơi một", "muơi mốt");
        number = number.replaceAll(" không trăm lẻ triệu không trăm", "");
        number = number.replaceAll("lẻ triệu", "triệu");
        number = number.replaceAll("lẻ ngàn", "ngàn");
        number = number.replaceAll("triệu không trăm", "triệu");
        number = number.replaceAll("ngàn không trăm lẻ", "ngàn");
        number = number.replaceAll("triệu ngàn", "triệu");
        number = number.replaceAll("tỷ ngàn", "tỷ");

        if(number.length() > 14 && "không trăm lẻ ".equals(number.substring(number.length() - 14, number.length()))) {
            number = number.substring(0, number.length() - 14);
        }

        return StringUtils.capitalize(number + "đồng chẵn");
    }
}
