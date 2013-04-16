package com.qlkh.server.business.rule;

import com.qlkh.core.client.report.MaterialReportBean;
import com.qlkh.core.client.report.PriceSumReportBean;

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

    public static void addDefault(List<PriceSumReportBean> priceSumReportBeans) {
        create("A", "TỔNG KẾ HOẠCH GIAO", priceSumReportBeans);
        create("I", "ĐỊNH KỲ", priceSumReportBeans);
        create("II", "KHÔNG ĐỊNH KỲ", priceSumReportBeans);

        create("SF1", "HT ĐƯỜNG TRUYỀN TẢI", priceSumReportBeans);
        create("I", "ĐỊNH KỲ (từ 1.3xx - 1.4xx)", priceSumReportBeans, "1.3.*", "1.4.*");
        create("II", "KHÔNG ĐỊNH KỲ (từ 1.5xx-1.6xx)", priceSumReportBeans, "1.5.*", "1.6.*");

        create("SF2", "TRẠM-TỔNG ĐÀI", priceSumReportBeans);
        create("I", "ĐỊNH KỲ (từ 2.3xx - 2.4xx)", priceSumReportBeans, "2.3.*", "2.4.*");
        create("II", "KHÔNG ĐỊNH KỲ (từ 2.5xx - 2.6xx)", priceSumReportBeans, "2.5.*", "2.6.*");

        create("SF3", "TÍN HIỆU RA, VÀO GA", priceSumReportBeans);
        create("I", "ĐỊNH KỲ (từ 3.3xx - 3.4xx)", priceSumReportBeans, "3.3.*", "3.4.*");
        create("II", "KHÔNG ĐỊNH KỲ (3.5xx - 3.6xx)", priceSumReportBeans, "3.5.*", "3.6.*");

        create("SF4", "THIẾT BỊ KHỐNG CHẾ", priceSumReportBeans);
        create("I", "ĐỊNH KỲ (từ 4.3xx - 4.4xx)", priceSumReportBeans, "4.3.*", "4.4.*");
        create("II", "KHÔNG ĐỊNH KỲ (từ 4.5xx - 4.6xx)", priceSumReportBeans, "4.5.*", "4.6.*");

        create("SF5", "THIẾT BỊ ĐIỀU KHIỂN", priceSumReportBeans);
        create("I", "ĐỊNH KỲ (từ 5.3xx - 5.4xx)", priceSumReportBeans, "5.3.*", "5.4.*");
        create("II", "KHÔNG ĐỊNH KỲ (từ 5.5xx - 5.6xx)", priceSumReportBeans, "5.5.*", "5.6.*");

        create("SF6", "HỆ THỐNG CÁP TÍN HIỆU", priceSumReportBeans);
        create("I", "ĐỊNH KỲ (từ 6.3xx - 6.4xx)", priceSumReportBeans, "6.3.*", "6.4.*");
        create("II", "KHÔNG ĐỊNH KỲ (từ 6.5xx - 6.6xx)", priceSumReportBeans, "6.5.*", "6.6.*");

        create("SF7", "HỆ THỐNG NGUỒN ĐIỆN", priceSumReportBeans);
        create("I", "ĐỊNH KỲ (từ 7.3xx - 7.4xx)", priceSumReportBeans, "7.3.*", "7.4.*");
        create("II", "KHÔNG ĐỊNH KỲ (từ 7.5xx - 7.6xx)", priceSumReportBeans, "7.5.*", "7.6.*");
    }

    public static void create(String tt, String name, List<PriceSumReportBean> priceSumReportBeans, String... range) {
        MaterialReportBean materialReportBean = new MaterialReportBean();
        materialReportBean.setStt(tt);
        materialReportBean.setName(name);
        materialReportBean.setRange(range);
        PriceSumReportBean sumReportBean = new PriceSumReportBean();
        sumReportBean.setMaterial(materialReportBean);
        priceSumReportBeans.add(sumReportBean);
    }

}
