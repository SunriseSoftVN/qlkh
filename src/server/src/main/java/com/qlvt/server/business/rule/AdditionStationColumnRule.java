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

import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.qlvt.core.client.constant.ReportTypeEnum;
import com.qlvt.core.client.model.Station;
import com.qlvt.core.client.model.view.SubAnnualTaskDetailDataView;
import com.qlvt.core.client.model.view.SubTaskDetailDataView;
import com.qlvt.core.client.model.view.TaskDetailDataView;
import com.qlvt.core.client.report.StationReportBean;
import com.qlvt.core.client.report.SumReportBean;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import static ch.lambdaj.Lambda.*;
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
                                      List<TaskDetailDataView> taskDetailViews,
                                      List<SubAnnualTaskDetailDataView> subAnnualTaskDetails,
                                      List<SubTaskDetailDataView> subTaskDetails) {
        for (SumReportBean bean : beans) {
            List<TaskDetailDataView> selectTaskDetail = select(taskDetailViews,
                    having(on(TaskDetailDataView.class).getTaskId(), equalTo(bean.getTask().getId()))
                            .and(having(on(TaskDetailDataView.class).getStationId(), equalTo(StationCodeEnum.CAUGIAT.getId())))
            );
            Long taskDetailId = null;
            Boolean annual = null;
            if (CollectionUtils.isNotEmpty(selectTaskDetail)) {
                taskDetailId = selectTaskDetail.get(0).getTaskDetailId();
                annual = selectTaskDetail.get(0).isAnnual();
            }

            Double value = 0d;
            if (taskDetailId != null) {
                if (annual) {
                    SubAnnualTaskDetailDataView subTaskAnnualDetail = selectUnique(subAnnualTaskDetails,
                            having(on(SubAnnualTaskDetailDataView.class).getTaskDetailId(), equalTo(taskDetailId))
                                    .and(having(on(SubAnnualTaskDetailDataView.class).getBranchId(), equalTo(BranchCodeEnum.ND.getId()))));
                    if (subTaskAnnualDetail != null) {
                        value = subTaskAnnualDetail.getRealValue();
                    }
                } else {
                    SubTaskDetailDataView subTaskDetail = selectUnique(subTaskDetails,
                            having(on(SubTaskDetailDataView.class).getTaskDetailId(), equalTo(taskDetailId))
                                    .and(having(on(SubTaskDetailDataView.class).getBranchId(), equalTo(BranchCodeEnum.ND.getId()))));

                    if (subTaskDetail != null) {
                        switch (reportTypeEnum) {
                            case Q1:
                                if (subTaskDetail.getQ1() != null) {
                                    value += subTaskDetail.getQ1();
                                }
                                break;
                            case Q2:
                                if (subTaskDetail.getQ2() != null) {
                                    value += subTaskDetail.getQ2();
                                }
                                break;
                            case Q3:
                                if (subTaskDetail.getQ3() != null) {
                                    value += subTaskDetail.getQ3();
                                }
                                break;
                            case Q4:
                                if (subTaskDetail.getQ4() != null) {
                                    value += subTaskDetail.getQ4();
                                }
                                break;
                            case CA_NAM:
                                if (subTaskDetail.getQ1() != null) {
                                    value += subTaskDetail.getQ1();
                                }
                                if (subTaskDetail.getQ2() != null) {
                                    value += subTaskDetail.getQ2();
                                }
                                if (subTaskDetail.getQ3() != null) {
                                    value += subTaskDetail.getQ3();
                                }
                                if (subTaskDetail.getQ4() != null) {
                                    value += subTaskDetail.getQ4();
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
