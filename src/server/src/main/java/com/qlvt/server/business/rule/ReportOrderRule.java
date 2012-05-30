/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.qlvt.server.business.rule;

import com.qlvt.core.client.report.CompanySumReportBean;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ReportOrderRule.
 *
 * @author Nguyen Duc Dung
 * @since 5/30/12, 10:35 AM
 */
public final class ReportOrderRule {

    private ReportOrderRule() {
    }

    public static List<Rule> rule() {
        //Rule nay danh cho nhung sap xep duoi, vd C.1, C.2 nam duoi C
        List<Rule> rules = new ArrayList<Rule>();

        rules.add(new Rule(TaskCodeEnum.C1.getCode(), TaskCodeEnum.C.getCode(), false));
        rules.add(new Rule(TaskCodeEnum.C2.getCode(), TaskCodeEnum.C1.getCode(), false));
        rules.add(new Rule(TaskCodeEnum.I.getCode(), TaskCodeEnum.C2.getCode(), false));
        rules.add(new Rule(TaskCodeEnum.II.getCode(), TaskCodeEnum.I.getCode(), false));
        rules.add(new Rule(TaskCodeEnum.III.getCode(), TaskCodeEnum.II.getCode(), false));

        rules.add(new Rule(TaskCodeEnum.IA.getCode(), "1.000", false));
        rules.add(new Rule(TaskCodeEnum.IIA.getCode(), "2.000", false));
        rules.add(new Rule(TaskCodeEnum.IIIA.getCode(), "3.000", false));
        rules.add(new Rule(TaskCodeEnum.IVA.getCode(), "4.000", false));
        rules.add(new Rule(TaskCodeEnum.VA.getCode(), "5.000", false));
        rules.add(new Rule(TaskCodeEnum.VIA.getCode(), "6.000", false));
        rules.add(new Rule(TaskCodeEnum.VIIA.getCode(), "7.000", false));
        rules.add(new Rule(TaskCodeEnum.VIIIA.getCode(), "8.000", false));
        rules.add(new Rule(TaskCodeEnum.VIVA.getCode(), "9.000", false));
        rules.add(new Rule(TaskCodeEnum.XA.getCode(), "10.000", false));

        rules.add(new Rule(TaskCodeEnum.IB.getCode(), "1.400", true));
        rules.add(new Rule(TaskCodeEnum.IIB.getCode(), "2.400", true));
        rules.add(new Rule(TaskCodeEnum.IIIB.getCode(), "3.400", true));
        rules.add(new Rule(TaskCodeEnum.IVB.getCode(), "4.400", true));
        rules.add(new Rule(TaskCodeEnum.VB.getCode(), "5.400", true));
        rules.add(new Rule(TaskCodeEnum.VIB.getCode(), "6.400", true));
        rules.add(new Rule(TaskCodeEnum.VIIB.getCode(), "7.400", true));
        rules.add(new Rule(TaskCodeEnum.VIIIB.getCode(), "8.400", true));
        rules.add(new Rule(TaskCodeEnum.VIVB.getCode(), "9.400", true));
        rules.add(new Rule(TaskCodeEnum.XB.getCode(), "10.400", true));

        return rules;
    }

    public static void sort(List<CompanySumReportBean> beans) {
        if (CollectionsUtils.isEmpty(beans)) {
            return;
        }
        List<CompanySumReportBean> sortBeans = new ArrayList<CompanySumReportBean>(beans.size());
        sortBeans.addAll(beans);

        for (Rule rule : rule()) {
            for (CompanySumReportBean bean1 : sortBeans) {
                if (bean1.getTask().getCode().trim().toLowerCase()
                        .equals(rule.getCode1().trim().toLowerCase())) {
                    for (CompanySumReportBean bean2 : sortBeans) {
                        if (bean2.getTask().getCode().trim().toLowerCase()
                                .equals(rule.getCode2().trim().toLowerCase())) {
                            //Move bean1 to correct position
                            beans.remove(bean1);
                            int index = beans.indexOf(bean2);
                            if (rule.isHigher()) {
                                beans.add(index, bean1);
                            } else {
                                beans.add(index + 1, bean1);
                            }
                            break;
                        }
                    }
                }
            }
        }

    }

    private static class Rule {
        String code1;
        String code2;
        boolean higher;

        private Rule(String code1, String code2, boolean higher) {
            this.code1 = code1;
            this.code2 = code2;
            this.higher = higher;
        }

        public String getCode1() {
            return code1;
        }

        public String getCode2() {
            return code2;
        }

        public boolean isHigher() {
            return higher;
        }
    }
}
