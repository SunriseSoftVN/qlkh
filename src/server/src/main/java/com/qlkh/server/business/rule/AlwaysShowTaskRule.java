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
