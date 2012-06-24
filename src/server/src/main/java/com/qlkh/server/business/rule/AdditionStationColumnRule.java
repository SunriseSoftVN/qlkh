/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.business.rule;

import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.qlkh.core.client.model.Station;
import com.qlkh.core.client.report.StationReportBean;
import com.qlkh.core.client.report.SumReportBean;

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
        dsTNStation.setId(StationCodeEnum.TN_FOR_REPORT.getId());
        dsTNStation.setName("ĐSTN");
        dsNDStation.setId(StationCodeEnum.ND_FOR_REPORT.getId());
        dsNDStation.setName("ĐSNĐ");
    }

    public static void addStation(List<Station> stations) {
        stations.add(dsTNStation);
        stations.add(dsNDStation);
    }

    public static void addDataForDSTN(List<SumReportBean> beans, long stationId) {
        for (SumReportBean bean : beans) {
            Double totalValue = bean.getStations().get(String.
                    valueOf(stationId)).getValue();
            Double totalTime = bean.getStations().get(String.
                    valueOf(stationId)).getTime();

            Double dsNDValue = bean.getStations().get(String.valueOf(dsNDStation.getId())).getValue();
            Double dsNDTime = bean.getStations().get(String.valueOf(dsNDStation.getId())).getTime();

            if (totalValue != null && totalValue > 0d) {
                if (dsNDValue == null) {
                    dsNDValue = 0d;
                }
                double result = totalValue - dsNDValue;
                if (result > 0) {
                    bean.getStations().
                            get(String.valueOf(dsTNStation.getId())).setValue(result);
                }
            }

            if (totalTime != null && totalTime > 0d) {
                if (dsNDTime == null) {
                    dsNDTime = 0d;
                }
                double result = totalTime - dsNDTime;
                   if (result > 0) {
                       bean.getStations().
                               get(String.valueOf(dsTNStation.getId())).setTime(result);
                   }
            }
        }
    }

    public static void addDataForDSND(List<SumReportBean> beans) {
        for (SumReportBean bean : beans) {
            StationReportBean dsTNBean = new StationReportBean(dsTNStation.getId(), dsTNStation.getName());
            StationReportBean dsNDBean = new StationReportBean(dsNDStation.getId(), dsNDStation.getName());

            for (StationReportBean station : bean.getStations().values()) {
                if (station.getNdValue() != null) {
                    dsNDBean.setValue(station.getNdValue());
                    dsNDBean.setTime(station.getNdTime());
                }
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
