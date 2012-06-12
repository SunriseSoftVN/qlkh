/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.business.rule;

import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.qlkh.core.client.constant.ReportTypeEnum;
import com.qlkh.core.client.model.Station;
import com.qlkh.core.client.model.view.TaskDetailDKDataView;
import com.qlkh.core.client.model.view.TaskDetailKDKDataView;
import com.qlkh.core.client.report.StationReportBean;
import com.qlkh.core.client.report.SumReportBean;

import java.util.List;

import static ch.lambdaj.Lambda.*;
import static com.qlkh.core.client.constant.TaskTypeEnum.DK;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * The Class AdditionStationColumnRule.
 *
 * @author Nguyen Duc Dung
 * @since 5/30/12, 1:58 PM
 */
public final class AdditionStationColumnRule {

    private static Station dsTNStation = new Station();
    private static Station dsNDStation = new Station();

    static {
        dsTNStation.setId(StationCodeEnum.TN_FOR_REPORT.getId());
        dsTNStation.setName("ĐSTN");
        dsNDStation.setId(StationCodeEnum.ND_FOR_REPORT.getId());
        dsNDStation.setName("ĐSNĐ");
    }

    public static void addStation(List<Station> stations) {
        stations.add(dsTNStation);
        stations.add(dsNDStation);
    }

    public static void addDataForDSTN(List<SumReportBean> beans) {
        for (SumReportBean bean : beans) {
            Double companyValue = bean.getStations().get(String.
                    valueOf(StationCodeEnum.COMPANY.getId())).getValue();
            Double companyTime = bean.getStations().get(String.
                    valueOf(StationCodeEnum.COMPANY.getId())).getTime();

            Double dsNDValue = bean.getStations().get(String.valueOf(dsNDStation.getId())).getValue();
            Double dsNDTime = bean.getStations().get(String.valueOf(dsNDStation.getId())).getTime();

            if (companyValue != null && companyValue > 0d) {
                if (dsNDValue == null) {
                    dsNDValue = 0d;
                }
                double result = companyValue - dsNDValue;
                if (result > 0) {
                    bean.getStations().
                            get(String.valueOf(dsTNStation.getId())).setValue(result);
                }
            }

            if (companyTime != null && companyTime > 0d) {
                if (dsNDTime == null) {
                    dsNDTime = 0d;
                }
                double result = companyTime - dsNDTime;
                   if (result > 0) {
                       bean.getStations().
                               get(String.valueOf(dsTNStation.getId())).setTime(result);
                   }
            }
        }
    }

    public static void addDataForDSND(List<SumReportBean> beans, ReportTypeEnum reportTypeEnum,
                                      List<TaskDetailDKDataView> taskDetailDKs,
                                      List<TaskDetailKDKDataView> taskDetailKDKs) {
        for (SumReportBean bean : beans) {

            Long taskId = bean.getTask().getId();

            Double value = 0d;
            if (taskId != null) {
                if (DK.getCode() == bean.getTask().getTaskTypeCode()) {
                    TaskDetailDKDataView detailDK = selectUnique(taskDetailDKs,
                            having(on(TaskDetailDKDataView.class).getTaskId(), equalTo(taskId))
                                    .and(having(on(TaskDetailDKDataView.class).getBranchId(), equalTo(BranchCodeEnum.ND.getId()))));
                    if (detailDK != null) {
                        value = detailDK.getRealValue();
                    }
                } else {
                    TaskDetailKDKDataView taskDetailKDK = selectUnique(taskDetailKDKs,
                            having(on(TaskDetailKDKDataView.class).getTaskId(), equalTo(taskId))
                                    .and(having(on(TaskDetailKDKDataView.class).getBranchId(), equalTo(BranchCodeEnum.ND.getId()))));

                    if (taskDetailKDK != null) {
                        switch (reportTypeEnum) {
                            case Q1:
                                if (taskDetailKDK.getQ1() != null) {
                                    value += taskDetailKDK.getQ1();
                                }
                                break;
                            case Q2:
                                if (taskDetailKDK.getQ2() != null) {
                                    value += taskDetailKDK.getQ2();
                                }
                                break;
                            case Q3:
                                if (taskDetailKDK.getQ3() != null) {
                                    value += taskDetailKDK.getQ3();
                                }
                                break;
                            case Q4:
                                if (taskDetailKDK.getQ4() != null) {
                                    value += taskDetailKDK.getQ4();
                                }
                                break;
                            case CA_NAM:
                                if (taskDetailKDK.getQ1() != null) {
                                    value += taskDetailKDK.getQ1();
                                }
                                if (taskDetailKDK.getQ2() != null) {
                                    value += taskDetailKDK.getQ2();
                                }
                                if (taskDetailKDK.getQ3() != null) {
                                    value += taskDetailKDK.getQ3();
                                }
                                if (taskDetailKDK.getQ4() != null) {
                                    value += taskDetailKDK.getQ4();
                                }
                                break;
                        }
                    }
                }
            }

            StationReportBean dsTNBean = new StationReportBean(dsTNStation.getId(), dsTNStation.getName());
            StationReportBean dsNDBean = new StationReportBean(dsNDStation.getId(), dsNDStation.getName());

            //Calculate time
            if (value != null && value > 0d) {
                Double time = bean.getTask().getDefaultValue() * bean.getTask().getQuota() * value;
                dsNDBean.setValue(value);
                dsNDBean.setTime(time);
            }

            bean.getStations().put(String.valueOf(dsTNBean.getId()), dsTNBean);
            bean.getStations().put(String.valueOf(dsNDBean.getId()), dsNDBean);
        }
    }

    public static void setStyle(List<AbstractColumn> columns) {
        int index = columns.size() - 1;
        columns.get(index - 1).setWidth(15);
        columns.get(index).setWidth(16);
    }

}
