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
import com.qlvt.core.client.model.SubTaskAnnualDetail;
import com.qlvt.core.client.model.SubTaskDetail;
import com.qlvt.core.client.model.TaskDetail;
import com.qlvt.core.client.report.CompanySumReportBean;
import com.qlvt.core.client.report.StationReportBean;
import com.qlvt.server.dao.SubTaskAnnualDetailDao;
import com.qlvt.server.dao.SubTaskDetailDao;
import com.qlvt.server.dao.TaskDetailDao;

import java.util.List;

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
        dsTNStation.setId(999998l);
        dsTNStation.setName("ĐSTN");
        dsNDStation.setId(999999l);
        dsNDStation.setName("ĐSNĐ");
    }

    public static void addStation(List<Station> stations) {
        stations.add(dsTNStation);
        stations.add(dsNDStation);
    }

    public static void addDataForDSTN(List<CompanySumReportBean> beans) {
        for (CompanySumReportBean bean : beans) {
            Double companyValue = bean.getStations().get(String.
                    valueOf(StationCodeEnum.COMPANY.getId())).getValue();
            Double companyTime = bean.getStations().get(String.
                    valueOf(StationCodeEnum.COMPANY.getId())).getTime();

            Double dsNDValue = bean.getStations().get(String.valueOf(dsNDStation.getId())).getValue();
            Double dsNDTime = bean.getStations().get(String.valueOf(dsNDStation.getId())).getTime();

            if (companyValue != null && companyValue > 0d) {
                if (dsNDValue != null && dsNDValue > 0d) {
                    bean.getStations().
                            get(String.valueOf(dsTNStation.getId())).setValue(companyValue - dsNDValue);
                }
            }

            if (companyTime != null && companyTime > 0d) {
                if (dsNDTime != null && dsNDTime > 0d) {
                    bean.getStations().
                            get(String.valueOf(dsTNStation.getId())).setTime(companyTime - dsNDTime);
                }
            }
        }
    }

    public static void addDataForDSND(List<CompanySumReportBean> beans, ReportTypeEnum reportTypeEnum, TaskDetailDao taskDetailDao,
                                      SubTaskAnnualDetailDao subTaskAnnualDetailDao, SubTaskDetailDao subTaskDetailDao) {
        for (CompanySumReportBean bean : beans) {
            TaskDetail taskDetail = taskDetailDao.
                    findCurrentByStationIdAndTaskId(StationCodeEnum.CAUGIAT.getId(), bean.getTask().getId());
            Double value = 0d;
            if (taskDetail != null) {
                if (taskDetail.getAnnual()) {
                    SubTaskAnnualDetail subTaskAnnualDetail = subTaskAnnualDetailDao.findByTaskDetaiIdAndBranchId(taskDetail.getId(),
                            BranchCodeEnum.ND.getId());
                    if (subTaskAnnualDetail != null) {
                        value = subTaskAnnualDetail.getRealValue();
                    }
                } else {
                    SubTaskDetail subTaskDetail = subTaskDetailDao.findByTaskDetaiIdAndBranchId(taskDetail.getId(),
                            BranchCodeEnum.ND.getId());
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
