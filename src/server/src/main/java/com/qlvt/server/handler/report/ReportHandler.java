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

package com.qlvt.server.handler.report;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.*;
import com.qlvt.client.client.utils.TaskCodeUtils;
import com.qlvt.core.client.action.report.ReportAction;
import com.qlvt.core.client.action.report.ReportResult;
import com.qlvt.core.client.constant.ReportFileTypeEnum;
import com.qlvt.core.client.constant.ReportTypeEnum;
import com.qlvt.core.client.constant.TaskTypeEnum;
import com.qlvt.core.client.model.Station;
import com.qlvt.core.client.model.Task;
import com.qlvt.core.client.model.view.SubAnnualTaskDetailDataView;
import com.qlvt.core.client.model.view.SubTaskDetailDataView;
import com.qlvt.core.client.model.view.TaskDetailDataView;
import com.qlvt.core.client.report.StationReportBean;
import com.qlvt.core.client.report.SumReportBean;
import com.qlvt.core.system.SystemUtil;
import com.qlvt.server.business.rule.*;
import com.qlvt.server.dao.SqlQueryDao;
import com.qlvt.server.dao.SubTaskAnnualDetailDao;
import com.qlvt.server.dao.SubTaskDetailDao;
import com.qlvt.server.dao.TaskDetailDao;
import com.qlvt.server.dao.core.GeneralDao;
import com.qlvt.server.handler.core.AbstractHandler;
import com.qlvt.server.servlet.ReportServlet;
import com.qlvt.server.util.ReportExporter;
import com.qlvt.server.util.ServletUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.util.*;

import static ch.lambdaj.Lambda.*;
import static com.qlvt.core.client.constant.TaskTypeEnum.SUBSUM;
import static com.qlvt.core.client.constant.TaskTypeEnum.SUM;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * The Class ReportHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 6:52 PM
 */
public class ReportHandler extends AbstractHandler<ReportAction, ReportResult> {

    private static final String REPORT_SERVLET_URI = "/report?";
    private static final String REPORT_FILE_NAME = "kehoachtacnghiep";
    private static final Font DEFAULT_FONT = new Font(8, "Arial", "/fonts/Arial.ttf",
            Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
    private static final Font TITLE_FONT = new Font(14, "Arial", "/fonts/Arial.ttf",
            Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private TaskDetailDao taskDetailDao;

    @Autowired
    private SubTaskDetailDao subTaskDetailDao;

    @Autowired
    private SubTaskAnnualDetailDao subTaskAnnualDetailDao;

    @Autowired
    private SqlQueryDao sqlQueryDao;

    @Override
    public Class<ReportAction> getActionType() {
        return ReportAction.class;
    }

    @Override
    public ReportResult execute(ReportAction action, ExecutionContext context) throws DispatchException {
        return new ReportResult(reportForCompany(action.getReportTypeEnum(), action.getFileTypeEnum(), action.getStationId()));
    }

    private String reportForCompany(ReportTypeEnum reportTypeEnum, ReportFileTypeEnum fileTypeEnum, long stationId) throws ActionException {
        try {
            DynamicReport dynamicReport = buildReport(reportTypeEnum, fileTypeEnum, stationId);
            JasperReport jasperReport = DynamicJasperHelper.
                    generateJasperReport(dynamicReport, new ClassicLayoutManager(), null);

            JasperPrint jasperPrint = JasperFillManager.
                    fillReport(jasperReport, null, new JRBeanCollectionDataSource(buildReportData(reportTypeEnum, stationId)));

            String filePath = ServletUtils.getInstance().
                    getRealPath(ReportServlet.REPORT_DIRECTORY, REPORT_FILE_NAME
                            + fileTypeEnum.getFileExt());

            if (fileTypeEnum == ReportFileTypeEnum.PDF) {
                ReportExporter.exportReport(jasperPrint, filePath);
            } else if (fileTypeEnum == ReportFileTypeEnum.EXCEL) {
                ReportExporter.exportReportXls(jasperPrint, filePath);
            }

            return new StringBuilder().append(SystemUtil.getServerBaseUrl())
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

    private DynamicReport buildReport(ReportTypeEnum reportTypeEnum, ReportFileTypeEnum reportFileTypeEnum, long stationId) {
        FastReportBuilder fastReportBuilder = new FastReportBuilder();

        Style titleStyle = new Style();
        TITLE_FONT.setBold(true);
        titleStyle.setFont(TITLE_FONT);
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        titleStyle.setVerticalAlign(VerticalAlign.MIDDLE);

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
        detailStyle.setBorderLeft(Border.THIN);
        detailStyle.setBorderRight(Border.THIN);
        detailStyle.setBorderBottom(Border.THIN);

        Style numberStyle = new Style();
        numberStyle.setFont(DEFAULT_FONT);
        numberStyle.setBorder(Border.THIN);
        numberStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
        numberStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        numberStyle.setBorderLeft(Border.THIN);
        numberStyle.setBorderRight(Border.THIN);
        numberStyle.setBorderBottom(Border.THIN);

        Style nameStyle = new Style();
        nameStyle.setFont(DEFAULT_FONT);
        nameStyle.setBorder(Border.THIN);
        nameStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        nameStyle.setBorderLeft(Border.THIN);
        nameStyle.setBorderRight(Border.THIN);
        nameStyle.setBorderBottom(Border.THIN);

        try {
            fastReportBuilder.addColumn("Mã CV", "task.code", String.class, 15, detailStyle)
                    .addColumn("Nội dung công việc", "task.name", String.class, 80, nameStyle)
                    .addColumn("Đơn vị", "task.unit", String.class, 15, detailStyle)
                    .addColumn("Định mức", "task.defaultValue", Double.class, 15, detailStyle)
                    .addColumn("Số lần", "task.quota", Integer.class, 10, detailStyle);

            List<Station> stations = new ArrayList<Station>();
            if (stationId == StationCodeEnum.COMPANY.getId()) {
                stations = generalDao.getAll(Station.class);
                //Add two more columns. Business rule. TODO remove @dungvn3000
                AdditionStationColumnRule.addStation(stations);
            } else {
                Station station = generalDao.findById(Station.class, stationId);
                stations.add(station);
                fastReportBuilder.setTitle("KẾ HOẠCH TÁC NGHIỆP KHỐI LƯỢNG SCTXĐK-KCHT TTTH ĐS "
                        + reportTypeEnum.getValue() + " NĂM 2012 "
                        + station.getName().toUpperCase());
            }

            if (CollectionUtils.isNotEmpty(stations)) {
                Map<Integer, String> colSpans = new HashMap<Integer, String>();
                for (Station station : stations) {
                    int index = fastReportBuilder.getColumns().size();
                    //Format number style, remove omit unnecessary '.0'
                    fastReportBuilder.addColumn("KL",
                            "stations." + station.getId() + ".value", Double.class, 19, false, "###,###.#", numberStyle);
                    fastReportBuilder.addColumn("Giờ",
                            "stations." + station.getId() + ".time", Double.class, 24, false, "###,###", numberStyle);
                    colSpans.put(index, station.getName());
                }

                if (stationId == StationCodeEnum.COMPANY.getId()) {
                    for (Integer colIndex : colSpans.keySet()) {
                        fastReportBuilder.setColspan(colIndex, 2, colSpans.get(colIndex));
                    }
                    //set style two last columns. Business rule. TODO remove @dungvn3000
                    AdditionStationColumnRule.setStyle(fastReportBuilder.getColumns());
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        fastReportBuilder.setHeaderHeight(50);
        if (stationId == StationCodeEnum.COMPANY.getId()) {
            fastReportBuilder.setPageSizeAndOrientation(Page.Page_A4_Landscape());
        } else {
            fastReportBuilder.setPageSizeAndOrientation(Page.Page_A4_Portrait());
        }

        fastReportBuilder.setDefaultStyles(titleStyle, null, headerStyle, null);
        fastReportBuilder.setUseFullPageWidth(true);
        fastReportBuilder.setLeftMargin(10);

        if (reportFileTypeEnum == ReportFileTypeEnum.PDF) {
            fastReportBuilder.setPrintBackgroundOnOddRows(true);
        } else {
            fastReportBuilder.setIgnorePagination(true);
        }

        return fastReportBuilder.build();
    }

    private List<SumReportBean> buildReportData(ReportTypeEnum reportTypeEnum, long stationId) throws ActionException {
        Station currentStation = generalDao.findById(Station.class, stationId);
        if (currentStation == null) {
            throw new ActionException(StringUtils.
                    substitute("Station id {0} is not exist", stationId));
        }
        List<Station> stations = new ArrayList<Station>();
        if (currentStation.isCompany()) {
            stations = generalDao.getAll(Station.class);
        } else {
            stations.add(currentStation);
        }

        //Load all view from database for calculation
        List<Long> stationIds = extract(stations, on(Station.class).getId());
        int currentYear = 1900 + new Date().getYear();
        List<SubTaskDetailDataView> subTaskDetails = sqlQueryDao.
                getSubTaskDetailDataViews(stationIds, currentYear);
        List<SubAnnualTaskDetailDataView> subAnnualTaskDetails = sqlQueryDao
                .getSubAnnualTaskDetailDataViews(stationIds, currentYear);
        List<TaskDetailDataView> taskDetailViews = sqlQueryDao.getTaskDetailViews(currentYear);


        List<SumReportBean> beans = new ArrayList<SumReportBean>();
        List<SumReportBean> parentBeans = new ArrayList<SumReportBean>();
        List<Task> tasks = generalDao.getAll(Task.class, Order.asc("code"));
        for (Task task : tasks) {
            SumReportBean bean = new SumReportBean();
            bean.setTask(task);
            for (Station station : stations) {
                StationReportBean stationReport = calculateForStation(task, station, reportTypeEnum,
                        taskDetailViews, subTaskDetails, subAnnualTaskDetails);
                bean.getStations().put(String.valueOf(stationReport.getId()), stationReport);
            }
            beans.add(bean);
            if (task.getTaskTypeCode() == SUBSUM.getCode()
                    || task.getTaskTypeCode() == SUM.getCode()) {
                parentBeans.add(bean);
            }
        }

        //Add two more columns. Business rule.
        if (stationId == StationCodeEnum.COMPANY.getId()) {
            AdditionStationColumnRule.addDataForDSND(beans, reportTypeEnum, taskDetailViews,
                    subAnnualTaskDetails, subTaskDetails);
        }

        //Build tree
        buildTree(beans, parentBeans);
        //Calculate for Sum or SubSum Task.
        forEach(beans).calculate();

        //Remove empty task, exclude sum task
        List<SumReportBean> removeBeans = new ArrayList<SumReportBean>();
        for (SumReportBean bean : beans) {
            boolean found = true;
            for (StationReportBean station : bean.getStations().values()) {
                if (station.getValue() != null) {
                    found = false;
                }
            }
            if (found && !AlwaysShowTaskRule.isTask(bean.getTask().getCode())) {
                removeBeans.add(bean);
            }
        }
        beans.removeAll(removeBeans);

        //Calculate for company
        if (stationId == StationCodeEnum.COMPANY.getId()) {
            calculateForCompany(beans);
        }

        //Sort by Alphabetical.
        Collections.sort(beans);

        //Business Order.
        ReportOrderRule.sort(beans);

        //Hide unnecessary detail
        HideDetailTaskRule.hide(beans);

        //Calculate for DSTN
        if (stationId == StationCodeEnum.COMPANY.getId()) {
            AdditionStationColumnRule.addDataForDSTN(beans);
        }

        return beans;
    }


    private void buildTree(List<SumReportBean> beans,
                           List<SumReportBean> parentBeans) {
        for (SumReportBean childBean : beans) {
            for (SumReportBean parentBean : parentBeans) {
                if (isParent(parentBean, childBean)) {
                    parentBean.getChildBeans().add(childBean);
                }
            }
        }
    }

    private boolean isParent(SumReportBean parentBean, SumReportBean childBean) {
        if (!childBean.getTask().getCode().equals(parentBean.getTask().getCode())) {
            switch (TaskTypeEnum.valueOf(parentBean.getTask().getTaskTypeCode())) {
                case SUBSUM:
                    if (parentBean.getTask().getCode().length() > 3
                            && childBean.getTask().getCode().length() > 3) {
                        String parentPrefix = parentBean.getTask().getCode().substring(0, 3);
                        String childPrefix = childBean.getTask().getCode().substring(0, 3);
                        if (parentPrefix.equals(childPrefix)) {
                            return true;
                        }
                    }
                    break;
                case SUM:
                    List<String> childTaskCodes = TaskCodeUtils
                            .getChildTaskCodes(parentBean.getTask().getChildTasks());
                    if (CollectionUtils.isNotEmpty(childTaskCodes)
                            && childTaskCodes.contains(childBean.getTask().getCode())) {
                        return true;
                    }
            }
        }
        return false;
    }


    private StationReportBean calculateForStation(Task task, Station station, ReportTypeEnum reportTypeEnum,
                                                  List<TaskDetailDataView> taskDetails, List<SubTaskDetailDataView> subTaskDetails,
                                                  List<SubAnnualTaskDetailDataView> subAnnualTaskDetails) {
        StationReportBean stationReport = new StationReportBean();
        stationReport.setId(station.getId());
        stationReport.setName(station.getName());
        //We don't calculate for company because it is sum of another station.
        if (!station.isCompany() && (task.getTaskTypeCode() == TaskTypeEnum.KDK.getCode()
                || task.getTaskTypeCode() == TaskTypeEnum.DK.getCode())) {

            List<TaskDetailDataView> select = select(taskDetails,
                    having(on(TaskDetailDataView.class).getTaskId(), equalTo(task.getId()))
                            .and(having(on(TaskDetailDataView.class).getStationId(), equalTo(station.getId())))
            );
            Long taskDetailId = null;
            Boolean annual = null;
            if (CollectionUtils.isNotEmpty(select)) {
                taskDetailId = select.get(0).getTaskDetailId();
                annual = select.get(0).isAnnual();
            }

            if (taskDetailId != null) {
                //Calculate Weight
                if (annual) {
                    calculateWeightForAnnualTask(stationReport, taskDetailId, subAnnualTaskDetails);
                } else {
                    calculateWeightForNormalTask(stationReport, taskDetailId, reportTypeEnum, subTaskDetails);
                }
                //Calculate time
                if (task.getDefaultValue() != null && stationReport.getValue() != null) {
                    Double time = task.getDefaultValue() * task.getQuota() * stationReport.getValue();
                    stationReport.setTime(time);
                }
            }
        }
        return stationReport;
    }

    private void calculateWeightForNormalTask(StationReportBean stationReport, long taskDetailId,
                                              ReportTypeEnum reportTypeEnum, List<SubTaskDetailDataView> subTaskDetails) {
        List<SubTaskDetailDataView> select = select(subTaskDetails,
                having(on(SubTaskDetailDataView.class).getTaskDetailId(), equalTo(taskDetailId)));
        if (CollectionUtils.isNotEmpty(select)) {
            Double weight = 0d;
            for (SubTaskDetailDataView subTaskDetail : select) {
                switch (reportTypeEnum) {
                    case Q1:
                        if (subTaskDetail.getQ1() != null) {
                            weight += subTaskDetail.getQ1();
                        }
                        break;
                    case Q2:
                        if (subTaskDetail.getQ2() != null) {
                            weight += subTaskDetail.getQ2();
                        }
                        break;
                    case Q3:
                        if (subTaskDetail.getQ3() != null) {
                            weight += subTaskDetail.getQ3();
                        }
                        break;
                    case Q4:
                        if (subTaskDetail.getQ4() != null) {
                            weight += subTaskDetail.getQ4();
                        }
                        break;
                    case CA_NAM:
                        if (subTaskDetail.getQ1() != null) {
                            weight += subTaskDetail.getQ1();
                        }
                        if (subTaskDetail.getQ2() != null) {
                            weight += subTaskDetail.getQ2();
                        }
                        if (subTaskDetail.getQ3() != null) {
                            weight += subTaskDetail.getQ3();
                        }
                        if (subTaskDetail.getQ4() != null) {
                            weight += subTaskDetail.getQ4();
                        }
                        break;
                }
            }
            if (weight > 0) {
                stationReport.setValue(weight);
            }
        }
    }

    private void calculateWeightForAnnualTask(StationReportBean stationReport, long taskDetailId,
                                              List<SubAnnualTaskDetailDataView> subAnnualTaskDetails) {
        List<SubAnnualTaskDetailDataView> select = select(subAnnualTaskDetails,
                having(on(SubAnnualTaskDetailDataView.class).getTaskDetailId(), equalTo(taskDetailId)));
        if (CollectionUtils.isNotEmpty(select)) {
            Double weight = 0d;
            for (SubAnnualTaskDetailDataView subTaskAnnualDetail : select) {
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
            if (weight > 0) {
                stationReport.setValue(weight);
            }
        }
    }

    private void calculateForCompany(List<SumReportBean> beans) {
        for (SumReportBean bean : beans) {
            double sumTime = 0d;
            double sumValue = 0d;
            for (StationReportBean station : bean.getStations().values()) {
                //We don't using Nghia Dam branch because we already calculated it.
                if (station.getId() != StationCodeEnum.ND_FOR_REPORT.getId()) {
                    Double time = station.getTime();
                    Double value = station.getValue();
                    if (time == null) {
                        time = 0D;
                    }
                    if (value == null) {
                        value = 0D;
                    }
                    sumTime += time;
                    sumValue += value;
                }
            }
            if (sumValue > 0) {
                bean.getStations().get(String.valueOf(StationCodeEnum.COMPANY.getId())).setValue(sumValue);
            }
            if (sumTime > 0) {
                bean.getStations().get(String.valueOf(StationCodeEnum.COMPANY.getId())).setTime(sumTime);
            }
        }
    }
}
