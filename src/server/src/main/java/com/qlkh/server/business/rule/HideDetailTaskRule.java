/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.business.rule;

import com.qlkh.core.client.report.StationReportBean;
import com.qlkh.core.client.report.SumReportBean;
import com.qlkh.core.client.report.TaskReportBean;
import org.apache.commons.lang.StringUtils;

import java.util.List;

import static com.qlkh.core.client.constant.TaskTypeEnum.*;

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
            if (isTask(bean.getTask())) {
                bean.getTask().setUnit(StringUtils.EMPTY);
                bean.getTask().setQuota(null);
                bean.getTask().setDefaultValue(null);
                for (StationReportBean station : bean.getStations().values()) {
                    station.setValue(null);
                }
            }
        }
    }

    private static boolean isTask(TaskReportBean task) {
        return DK.getCode() != task.getTaskTypeCode()
                && KDK.getCode() != task.getTaskTypeCode()
                && NAM.getCode() != task.getTaskTypeCode();
    }
}
