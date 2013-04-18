package com.qlkh.server.business.rule;

import com.qlkh.core.client.report.PriceReportBean;

import java.util.List;

/**
 * The Class PriceReportRule.
 *
 * @author Nguyen Duc Dung
 * @since 4/16/13 10:59 PM
 */
public final class PriceReportRule {

    private PriceReportRule() {

    }

    public static void addDefault(List<PriceReportBean> prices) {
        create("A", "A", "TỔNG KẾ HOẠCH GIAO", prices, "A.I.*");
        create("A.I", "I", "ĐỊNH KỲ", prices, "SF[0-9].I");
        create("A.II", "II", "KHÔNG ĐỊNH KỲ", prices, "SF[0-9].II");

        create("SF1", "SF1", "HT ĐƯỜNG TRUYỀN TẢI", prices, "SF1.I.*");
        create("SF1.I", "I", "ĐỊNH KỲ (từ 1.3xx - 1.4xx)", prices, "1.3.*", "1.4.*");
        create("SF1.II", "II", "KHÔNG ĐỊNH KỲ (từ 1.5xx-1.6xx)", prices, "1.5.*", "1.6.*");

        create("SF2", "SF2", "TRẠM-TỔNG ĐÀI", prices, "SF2.I.*");
        create("SF2.I", "I", "ĐỊNH KỲ (từ 2.3xx - 2.4xx)", prices, "2.3.*", "2.4.*");
        create("SF2.II", "II", "KHÔNG ĐỊNH KỲ (từ 2.5xx - 2.6xx)", prices, "2.5.*", "2.6.*");

        create("SF3", "SF3", "TÍN HIỆU RA, VÀO GA", prices, "SF3.I.*");
        create("SF3.I", "I", "ĐỊNH KỲ (từ 3.3xx - 3.4xx)", prices, "3.3.*", "3.4.*");
        create("SF3.II", "II", "KHÔNG ĐỊNH KỲ (3.5xx - 3.6xx)", prices, "3.5.*", "3.6.*");

        create("SF4", "SF4", "THIẾT BỊ KHỐNG CHẾ", prices, "SF4.I.*");
        create("SF4.I", "I", "ĐỊNH KỲ (từ 4.3xx - 4.4xx)", prices, "4.3.*", "4.4.*");
        create("SF4.II", "II", "KHÔNG ĐỊNH KỲ (từ 4.5xx - 4.6xx)", prices, "4.5.*", "4.6.*");

        create("SF5", "SF5", "THIẾT BỊ ĐIỀU KHIỂN", prices, "SF5.I.*");
        create("SF5.I", "I", "ĐỊNH KỲ (từ 5.3xx - 5.4xx)", prices, "5.3.*", "5.4.*");
        create("SF5.II", "II", "KHÔNG ĐỊNH KỲ (từ 5.5xx - 5.6xx)", prices, "5.5.*", "5.6.*");

        create("SF6", "SF6", "HỆ THỐNG CÁP TÍN HIỆU", prices, "SF6.I.*");
        create("SF6.I", "I", "ĐỊNH KỲ (từ 6.3xx - 6.4xx)", prices, "6.3.*", "6.4.*");
        create("SF6.II", "II", "KHÔNG ĐỊNH KỲ (từ 6.5xx - 6.6xx)", prices, "6.5.*", "6.6.*");

        create("SF7", "SF7", "HỆ THỐNG NGUỒN ĐIỆN", prices, "SF7.I.*");
        create("SF7.I", "I", "ĐỊNH KỲ (từ 7.3xx - 7.4xx)", prices, "7.3.*", "7.4.*");
        create("SF7.II", "II", "KHÔNG ĐỊNH KỲ (từ 7.5xx - 7.6xx)", prices, "7.5.*", "7.6.*");
    }

    public static void create(String code, String tt, String name, List<PriceReportBean> prices, String... range) {
        PriceReportBean priceReportBean = new PriceReportBean(tt, code, name, range);
        prices.add(priceReportBean);
    }

}
