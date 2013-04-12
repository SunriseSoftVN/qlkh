/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.business.rule;

import com.qlkh.client.client.utils.TaskCodeUtils;
import com.qlkh.core.client.action.report.TaskReportAction;
import com.qlkh.core.client.report.StationReportBean;
import com.qlkh.core.client.report.SumReportBean;
import com.qlkh.core.client.report.TaskReportBean;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.qlkh.core.client.constant.ReportFormEnum.MAU_1;
import static com.qlkh.core.client.constant.TaskTypeEnum.*;

/**
 * The Class HideWeightTaskRule.
 *
 * @author Nguyen Duc Dung
 * @since 5/30/12, 12:57 PM
 */
public final class HideDetailTaskRule {

    public static void hide(List<SumReportBean> beans, TaskReportAction action) {
        List<SumReportBean> removeBeans = new ArrayList<SumReportBean>();
        for (SumReportBean bean : beans) {
            //Hide default value and quota when it is 0.
            if (bean.getTask().getDefaultValueForPrinting() != null &&
                    bean.getTask().getDefaultValueForPrinting() == 0) {
                bean.getTask().setDefaultValueForPrinting(null);
            }
            if (bean.getTask().getQuotaForPrinting() != null
                    && bean.getTask().getQuotaForPrinting() == 0) {
                bean.getTask().setQuotaForPrinting(null);
            }

            if (isTask(bean.getTask())) {
                bean.getTask().setUnit(StringUtils.EMPTY);
                bean.getTask().setQuotaForPrinting(null);
                bean.getTask().setDefaultValueForPrinting(null);
                for (StationReportBean station : bean.getStations().values()) {
                    station.setValue(null);
                }
            }
            if (action.getReportFormEnum() == MAU_1 && isRemoveTask(bean.getTask())) {
                removeBeans.add(bean);
            }

            //Hide 0 value
            for (StationReportBean station : bean.getStations().values()) {
                if (station.getTime() != null && station.getTime() == 0) {
                    station.setTime(null);
                }

                if (station.getValue() != null && station.getValue() == 0) {
                    station.setValue(null);
                }
            }
        }
        beans.removeAll(removeBeans);
    }

    private static boolean isTask(TaskReportBean task) {
        return task.getCode().equals(TaskCodeEnum.III.getCode())
                || DK.getCode() != task.getTaskTypeCode()
                && KDK.getCode() != task.getTaskTypeCode()
                && NAM.getCode() != task.getTaskTypeCode();
    }

    private static boolean isRemoveTask(TaskReportBean task) {
        //Remove for *.10*, *.20*, *.30*
        Integer subCode = TaskCodeUtils.getTaskPrefix(task.getCode());
        return subCode != null && subCode > 100 && subCode < 400
                && subCode != 200 && subCode != 300;
    }
}
