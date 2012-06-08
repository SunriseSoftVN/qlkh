/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.business.rule;

import com.qlkh.core.client.report.StationReportBean;
import com.qlkh.core.client.report.SumReportBean;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class HideWeightTaskRule.
 *
 * @author Nguyen Duc Dung
 * @since 5/30/12, 12:57 PM
 */
public final class HideDetailTaskRule {

    public static void hide(List<SumReportBean> beans) {
        for (SumReportBean bean : beans) {
            //Hide default value and quota when it is 0.
            if (bean.getTask().getDefaultValue() == 0) {
                bean.getTask().setDefaultValue(null);
            }
            if (bean.getTask().getQuota() == 0) {
                bean.getTask().setQuota(null);
            }
            if (isTask(bean.getTask().getCode())) {
                bean.getTask().setUnit(StringUtils.EMPTY);
                bean.getTask().setQuota(null);
                bean.getTask().setDefaultValue(null);
                for (StationReportBean station : bean.getStations().values()) {
                    station.setValue(null);
                }
            }
        }
    }

    private static boolean isTask(String code) {
        for (Rule rule : getRule()) {
            if (code.equals(rule.getCode())) {
                return true;
            }
        }
        return false;
    }

    private static List<Rule> getRule() {
        List<Rule> rules = new ArrayList<Rule>();
        rules.add(new Rule(TaskCodeEnum.A.getCode()));
        rules.add(new Rule(TaskCodeEnum.B.getCode()));
        rules.add(new Rule(TaskCodeEnum.C.getCode()));
        rules.add(new Rule(TaskCodeEnum.C1.getCode()));
        rules.add(new Rule(TaskCodeEnum.C2.getCode()));
        rules.add(new Rule(TaskCodeEnum.I.getCode()));
        rules.add(new Rule(TaskCodeEnum.II.getCode()));
        rules.add(new Rule(TaskCodeEnum.III.getCode()));
        rules.add(new Rule(TaskCodeEnum.IA.getCode()));
        rules.add(new Rule(TaskCodeEnum.IB.getCode()));
        rules.add(new Rule("1.000"));
        rules.add(new Rule("1.100"));
        rules.add(new Rule("1.200"));
        rules.add(new Rule("1.300"));
        rules.add(new Rule("1.400"));
        return rules;
    }

    private static class Rule {
        String code;

        private Rule(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
