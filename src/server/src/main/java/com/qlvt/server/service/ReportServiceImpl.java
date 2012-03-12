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

package com.qlvt.server.service;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.qlvt.client.client.service.ReportService;
import com.qlvt.core.client.constant.ReportFileTypeEnum;
import com.qlvt.core.client.model.*;
import com.qlvt.core.client.report.CompanySumReportBean;
import com.qlvt.core.client.report.StationReportBean;
import com.qlvt.core.system.SystemUtil;
import com.qlvt.server.dao.*;
import com.qlvt.server.service.core.AbstractService;
import com.qlvt.server.servlet.ReportServlet;
import com.qlvt.server.transaction.Transaction;
import com.qlvt.server.util.ReportExporter;
import com.qlvt.server.util.ServletUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class ReportServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 2/22/12, 12:45 PM
 */
@Transaction
@Singleton
public class ReportServiceImpl extends AbstractService implements ReportService {

    private static final String REPORT_SERVLET_URI = "/report?";
    private static final String REPORT_FILE_NAME = "kehoachtacnghiep";
    private static final Font DEFAULT_FONT = new Font(9, "Arial", "/fonts/Arial.ttf",
            Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);

    @Inject
    private TaskDao taskDao;

    @Inject
    private StationDao stationDao;

    @Inject
    private TaskDetailDao taskDetailDao;

    @Inject
    private SubTaskAnnualDetailDao subTaskAnnualDetailDao;

    @Inject
    private SubTaskDetailDao subTaskDetailDao;

    @Inject
    private BranchDao branchDao;

    @Override
    public String reportForCompany(ReportFileTypeEnum fileTypeEnum) {
        try {
            DynamicReport dynamicReport = buildReport();
            JasperReport jasperReport = DynamicJasperHelper.
                    generateJasperReport(dynamicReport, new ClassicLayoutManager(), null);

            JasperPrint jasperPrint = JasperFillManager.
                    fillReport(jasperReport, null, new JRBeanCollectionDataSource(buildReportData()));

            String filePath = ServletUtils.getInstance().
                    getRealPath(ReportServlet.REPORT_DIRECTORY, REPORT_FILE_NAME
                            + fileTypeEnum.getFileExt());

            if (fileTypeEnum == ReportFileTypeEnum.PDF) {
                ReportExporter.exportReport(jasperPrint, filePath);
            } else if (fileTypeEnum == ReportFileTypeEnum.EXCEL) {
                ReportExporter.exportReportXls(jasperPrint, filePath);
            }

            return new StringBuilder().append(ServletUtils.getInstance()
                    .getBaseUrl(getThreadLocalRequest()))
                    .append(SystemUtil.getConfiguration().serverServletRootPath())
                    .append(REPORT_SERVLET_URI)
                    .append(ReportServlet.REPORT_FILENAME)
                    .append("=")
                    .append(REPORT_FILE_NAME)
                    .append(fileTypeEnum.getFileExt()).toString();

        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DynamicReport buildReport() {
        FastReportBuilder fastReportBuilder = new FastReportBuilder();

        Style headerStyle = new Style();
        headerStyle.setFont(DEFAULT_FONT);
        headerStyle.getFont().setBold(true);
        headerStyle.setBorder(Border.THIN);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);

        Style detailStyle = new Style();
        detailStyle.setFont(DEFAULT_FONT);
        detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        detailStyle.setVerticalAlign(VerticalAlign.MIDDLE);

        Style nameStyle = new Style();
        nameStyle.setFont(DEFAULT_FONT);
        nameStyle.setVerticalAlign(VerticalAlign.MIDDLE);

        try {
            fastReportBuilder.addColumn("TT", "stt", Integer.class, 20, detailStyle)
                    .addColumn("Mã CV", "task.code", String.class, 20, detailStyle)
                    .addColumn("Nội dung công ", "task.name", String.class, 100, nameStyle)
                    .addColumn("Đơn vị", "task.unit", String.class, 20, detailStyle)
                    .addColumn("Định mức", "task.defaultValue", Double.class, 20, detailStyle)
                    .addColumn("Số lần", "task.quota", Integer.class, 20, detailStyle);
            List<CompanySumReportBean> companies = buildReportData();
            if (CollectionUtils.isNotEmpty(companies)) {
                Map<Integer, String> colSpans = new HashMap<Integer, String>();
                for (StationReportBean station : companies.get(0).getStations().values()) {
                    int index = fastReportBuilder.getColumns().size();
                    fastReportBuilder.addColumn("KL",
                            "stations." + station.getId() + ".value", Double.class, 30, detailStyle);
                    fastReportBuilder.addColumn("Giờ",
                            "stations." + station.getId() + ".time", Long.class, 30, detailStyle);
                    colSpans.put(index, station.getName());
                }
                for (Integer colIndex : colSpans.keySet()) {
                    fastReportBuilder.setColspan(colIndex, 2, colSpans.get(colIndex));
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        fastReportBuilder.setHeaderHeight(50);
        fastReportBuilder.setPageSizeAndOrientation(Page.Page_A4_Landscape());
        fastReportBuilder.setDefaultStyles(null, null, headerStyle, null);
        fastReportBuilder.setUseFullPageWidth(true);
        fastReportBuilder.setPrintBackgroundOnOddRows(true);

        return fastReportBuilder.build();
    }

    private List<CompanySumReportBean> buildReportData() {
        List<CompanySumReportBean> beans = new ArrayList<CompanySumReportBean>();
        List<Task> tasks = taskDao.getAll(Task.class);
        List<Station> stations = stationDao.getAll(Station.class);
        int index = 0;
        for (Task task : tasks) {
            CompanySumReportBean bean = new CompanySumReportBean();
            bean.setStt(++index);
            bean.setTask(task);
            for (Station station : stations) {
                StationReportBean stationReport = calculate(task, station);
                bean.getStations().put(String.valueOf(stationReport.getId()), stationReport);
            }
            beans.add(bean);
        }
        return beans;
    }

    private StationReportBean calculate(Task task, Station station) {
        StationReportBean stationReport = new StationReportBean();
        stationReport.setId(station.getId());
        stationReport.setName(station.getName());
        TaskDetail taskDetail = taskDetailDao.findCurrentByStationIdAndTaskId(station.getId(), task.getId());
        if (taskDetail != null) {
            //Calculate Weight
            if (taskDetail.getAnnual()) {
                calculateWeightForAnnualTask(stationReport, taskDetail);
            } else {
                calculateWeightForNormalTask(stationReport, taskDetail);
            }
            //Calculate time
            if (task.getDefaultValue() != null && stationReport.getValue() != null) {
                Double time = task.getDefaultValue() * task.getQuota() * stationReport.getValue();
                stationReport.setTime(time.longValue());
            }
        }
        return stationReport;
    }

    private void calculateWeightForNormalTask(StationReportBean stationReport, TaskDetail taskDetail) {
        List<SubTaskDetail> subTaskDetails = subTaskDetailDao.findByTaskDetailId(taskDetail.getId());
        if (CollectionUtils.isNotEmpty(subTaskDetails)) {
            Double weight = 0d;
            for (SubTaskDetail subTaskDetail : subTaskDetails) {
                if (subTaskDetail.getQ1() != null) {
                    weight += subTaskDetail.getQ1();
                }
            }
            stationReport.setValue(weight);
        }
    }

    private void calculateWeightForAnnualTask(StationReportBean stationReport, TaskDetail taskDetail) {
        List<SubTaskAnnualDetail> subTaskAnnualDetails = subTaskAnnualDetailDao
                .findByTaskDetailId(taskDetail.getId());
        if (CollectionUtils.isNotEmpty(subTaskAnnualDetails)) {
            Double weight = 0d;
            for (SubTaskAnnualDetail subTaskAnnualDetail : subTaskAnnualDetails) {
                if (subTaskAnnualDetail.getRealValue() != null) {
                    weight += subTaskAnnualDetail.getRealValue();
                } else {
                    Double increaseValue = subTaskAnnualDetail.getIncreaseValue();
                    if (increaseValue == null) {
                        increaseValue = 0d;
                    }
                    Double decreaseValue = subTaskAnnualDetail.getDecreaseValue();
                    if (decreaseValue == null) {
                        decreaseValue = 0d;
                    }
                    Double lastYearValue = subTaskAnnualDetail.getLastYearValue();
                    if (lastYearValue == null) {
                        lastYearValue = 0d;
                    }
                    Double result = lastYearValue + increaseValue - decreaseValue;
                    weight += result;
                }
            }
            stationReport.setValue(weight);
        }
    }

}
