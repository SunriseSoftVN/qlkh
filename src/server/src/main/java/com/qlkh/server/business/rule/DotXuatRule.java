/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.business.rule;

import com.qlkh.core.client.report.SumReportBean;

import static com.qlkh.core.client.constant.TaskTypeEnum.DOTXUAT;
import static com.qlkh.server.business.rule.TaskCodeEnum.*;

/**
 * The Class DotXuatRule.
 *
 * @author Nguyen Duc Dung
 * @since 7/14/12, 4:30 AM
 */
public final class DotXuatRule {

    //DotXuat = (I+II+C1) * 1,5% * KL
    public static boolean isParent(SumReportBean parentBean, SumReportBean childBean) {
        if (parentBean != null && parentBean.getTask().getTaskTypeCode() == DOTXUAT.getCode()) {
            if (childBean.getTask().getCode().equals(I.getCode())
                    || childBean.getTask().getCode().equals(II.getCode())
                    || childBean.getTask().getCode().equals(C1.getCode())) {
                return true;
            }
        }
        return false;
    }

}
