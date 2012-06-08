/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.business.rule;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class AlwaysShowTaskRule.
 *
 * @author Nguyen Duc Dung
 * @since 5/30/12, 12:36 PM
 */
public final class AlwaysShowTaskRule {

    public static boolean isTask(String code) {
        for (Rule rule : getRule()) {
            if(code.equals(rule.getCode())) {
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
