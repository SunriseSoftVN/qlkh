/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.report;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.*;
import com.google.common.base.Preconditions;
import com.qlkh.client.client.utils.NumberUtils;
import com.qlkh.client.client.utils.TaskCodeUtils;
import com.qlkh.core.client.action.report.ReportAction;
import com.qlkh.core.client.action.report.ReportResult;
import com.qlkh.core.client.constant.ReportFileTypeEnum;
import com.qlkh.core.client.constant.ReportTypeEnum;
import com.qlkh.core.client.constant.TaskTypeEnum;
import com.qlkh.core.client.model.*;
import com.qlkh.core.client.model.view.TaskDetailDKDataView;
import com.qlkh.core.client.model.view.TaskDetailKDKDataView;
import com.qlkh.core.client.model.view.TaskDetailNamDataView;
import com.qlkh.core.client.report.StationReportBean;
import com.qlkh.core.client.report.SumReportBean;
import com.qlkh.core.client.report.TaskReportBean;
import com.qlkh.core.configuration.ConfigurationServerUtil;
import com.qlkh.server.business.rule.*;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.servlet.ReportServlet;
import com.qlkh.server.util.ReportExporter;
import com.qlkh.server.util.ServletUtils;
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
import static com.qlkh.client.client.utils.NumberUtils.convertNullToDouble;
import static com.qlkh.core.client.constant.ReportTypeEnum.*;
import static com.qlkh.core.client.constant.TaskTypeEnum.SUBSUM;
import static com.qlkh.core.client.constant.TaskTypeEnum.SUM;
import static org.hamcrest.Matchers.*;
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
    private SqlQueryDao sqlQueryDao;

    @Override
    public Class<ReportAction> getActionType() {
        return ReportAction.class;
    }

    @Override
    public ReportResult execute(ReportAction action, ExecutionContext context) throws DispatchException {
        return new ReportResult(reportForCompany(action));
    }

    private String reportForCompany(ReportAction action) throws ActionException {
        ReportFileTypeEnum fileTypeEnum = action.getFileTypeEnum();
        try {
            DynamicReport dynamicReport = buildReportLayout(action);
            JasperReport jasperReport = DynamicJasperHelper.
                    generateJasperReport(dynamicReport, new ClassicLayoutManager(), null);

            JasperPrint jasperPrint = JasperFillManager.
                    fillReport(jasperReport, null, new JRBeanCollectionDataSource(buildReportData(action)));

            String filePath = ServletUtils.getInstance().
                    getRealPath(ReportServlet.REPORT_DIRECTORY, REPORT_FILE_NAME
                            + fileTypeEnum.getFileExt());

            if (fileTypeEnum == ReportFileTypeEnum.PDF) {
                ReportExporter.exportReport(jasperPrint, filePath);
            } else if (fileTypeEnum == ReportFileTypeEnum.EXCEL) {
                ReportExporter.exportReportXls(jasperPrint, filePath);
            }

            return new StringBuilder().append(ConfigurationServerUtil.getServerBaseUrl())
                    .append(ConfigurationServerUtil.getConfiguration().serverServletRootPath())
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

    private DynamicReport buildReportLayout(ReportAction action) {

        ReportTypeEnum reportTypeEnum = action.getReportTypeEnum();
        ReportFileTypeEnum reportFileTypeEnum = action.getFileTypeEnum();
        long stationId = action.getStationId();
        Long branchId = action.getBranchId();

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
                    .addColumn("Số lần", "task.quota", Integer.class, 15, detailStyle);

            List<Station> stations = new ArrayList<Station>();
            if (stationId == StationCodeEnum.COMPANY.getId()) {
                stations = generalDao.getAll(Station.class);
                //Add two more columns. Business rule. TODO remove @dungvn3000
                AdditionStationColumnRule.addStation(stations);
                fastReportBuilder.setTitle("KẾ HOẠCH SCTX – KCHT THÔNG TIN TÍN HIỆU ĐS " + reportTypeEnum.getName()
                        + " NĂM " + action.getYear() + " \\n CÔNG TY TTTH ĐS VINH \\n");
            } else {
                Station station = generalDao.findById(Station.class, stationId);
                stations.add(station);
                if (branchId != null) {
                    Branch branch = generalDao.findById(Branch.class, branchId);
                    fastReportBuilder.setTitle("KẾ HOẠCH SCTX – KCHT THÔNG TIN TÍN HIỆU ĐS "
                            + reportTypeEnum.getName() + " NĂM " + action.getYear() + " \\n"
                            + branch.getName().toUpperCase() + "\\n");
                } else {
                    fastReportBuilder.setTitle("KẾ HOẠCH SCTX – KCHT THÔNG TIN TÍN HIỆU ĐS "
                            + reportTypeEnum.getName() + " NĂM " + action.getYear() + " \\n"
                            + station.getName().toUpperCase() + "\\n");
                }
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

    private List<SumReportBean> buildReportData(ReportAction reportAction) throws ActionException {
        ReportTypeEnum reportTypeEnum = reportAction.getReportTypeEnum();
        long stationId = reportAction.getStationId();
        Long branchId = reportAction.getBranchId();

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
        List<TaskDetailKDKDataView> subTaskDetails = sqlQueryDao.
                getTaskDetailKDK(stationIds, reportAction.getYear());
        List<TaskDetailDKDataView> subAnnualTaskDetails = sqlQueryDao
                .getTaskDetailDK(stationIds, reportAction.getYear());
        List<TaskDetailNamDataView> taskDetailNams = sqlQueryDao.getTaskDetailNam(stationIds, reportAction.getYear());
        List<TaskQuota> taskQuotas = generalDao.getAll(TaskQuota.class);
        List<TaskDefaultValue> taskDefaultValues = generalDao.getAll(TaskDefaultValue.class);

        List<SumReportBean> beans = new ArrayList<SumReportBean>();
        List<SumReportBean> parentBeans = new ArrayList<SumReportBean>();
        List<Task> tasks = generalDao.getAll(Task.class, Order.asc("code"));
        for (Task task : tasks) {
            SumReportBean bean = new SumReportBean();
            bean.setTask(task);
            for (Station station : stations) {
                StationReportBean stationReport = calculateForStation(bean.getTask(), station,
                        reportAction, subTaskDetails, subAnnualTaskDetails, taskDetailNams,
                        taskDefaultValues, taskQuotas);
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
            AdditionStationColumnRule.addDataForDSND(beans, reportTypeEnum, subAnnualTaskDetails, subTaskDetails);
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
                    if (StringUtils.isEmpty(parentBean.getTask().getChildTasks())) {
                        if (parentBean.getTask().getCode().length() > 3
                                && childBean.getTask().getCode().length() > 3) {
                            String parentPrefix = parentBean.getTask().getCode().substring(0, 3);
                            String childPrefix = childBean.getTask().getCode().substring(0, 3);
                            if (parentPrefix.equals(childPrefix)) {
                                return true;
                            }
                        }
                    } else {
                        String fromCode = TaskCodeUtils.extractFormCode(parentBean.getTask().getChildTasks());
                        String toCode = TaskCodeUtils.extractToCode(parentBean.getTask().getChildTasks());

                        Integer from = TaskCodeUtils.convert(fromCode);
                        Integer to = TaskCodeUtils.convert(toCode);

                        if (NumberUtils.isNumber(childBean.getTask().getCode())
                                && childBean.getTask().getCode().length() >= 5) {
                            Integer childCode = TaskCodeUtils.convert(childBean.getTask().getCode());
                            if (childCode >= from && childCode <= to) {
                                return true;
                            }
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


    private StationReportBean calculateForStation(TaskReportBean task, Station station,
                                                  ReportAction reportAction,
                                                  List<TaskDetailKDKDataView> taskDetailKDKs,
                                                  List<TaskDetailDKDataView> taskDetailDKs,
                                                  List<TaskDetailNamDataView> taskDetailNams,
                                                  List<TaskDefaultValue> taskDefaultValues, List<TaskQuota> taskQuotas) {
        StationReportBean stationReport = new StationReportBean();
        stationReport.setId(station.getId());
        stationReport.setName(station.getName());
        //We don't calculate for company because it is sum of another station.
        if (!station.isCompany() && (task.getTaskTypeCode() == TaskTypeEnum.KDK.getCode()
                || task.getTaskTypeCode() == TaskTypeEnum.DK.getCode())
                || task.getTaskTypeCode() == TaskTypeEnum.NAM.getCode()) {
            //Calculate Weight
            if (TaskTypeEnum.DK.getCode() == task.getTaskTypeCode()) {
                calculateForDKTask(stationReport, task,
                        taskDetailDKs, taskDefaultValues, taskQuotas, reportAction);
            } else if (TaskTypeEnum.KDK.getCode() == task.getTaskTypeCode()) {
                calculateForKDKTask(stationReport, task, taskDetailKDKs, taskDefaultValues, reportAction);
            } else if (TaskTypeEnum.NAM.getCode() == task.getTaskTypeCode()) {
                calculateForNamTask(stationReport, task, taskDetailNams, taskDefaultValues, reportAction);
            }
        }
        return stationReport;
    }

    private void calculateForKDKTask(StationReportBean station, TaskReportBean task,
                                     List<TaskDetailKDKDataView> subTaskDetails,
                                     List<TaskDefaultValue> taskDefaultValues, ReportAction reportAction) {
        List<TaskDetailKDKDataView> select;

        if (reportAction.getBranchId() != null) {
            select = select(subTaskDetails, having(on(TaskDetailKDKDataView.class).getTaskId(), equalTo(task.getId())).
                    and(having(on(TaskDetailKDKDataView.class).getStationId(), equalTo(station.getId()))).
                    and(having(on(TaskDetailKDKDataView.class).getBranchId(), equalTo(reportAction.getBranchId()))));
        } else {
            select = select(subTaskDetails, having(on(TaskDetailKDKDataView.class).getTaskId(), equalTo(task.getId())).
                    and(having(on(TaskDetailKDKDataView.class).getStationId(), equalTo(station.getId()))));
        }

        if (CollectionUtils.isNotEmpty(select)) {
            Double weight = 0d;
            double time = 0d;
            for (TaskDetailKDKDataView taskDetail : select) {
                weight += calculateWeightForDKAndNamTask(reportAction.getReportTypeEnum(), taskDetail.getQ1(),
                        taskDetail.getQ2(), taskDetail.getQ3(), taskDetail.getQ4());
                time += calculateTimeForDKAndNamTask(reportAction, task, taskDefaultValues,
                        taskDetail.getQ1(), taskDetail.getQ2(), taskDetail.getQ3(), taskDetail.getQ4());
            }
            if (weight > 0) {
                station.setValue(weight);
            }
            if (time > 0) {
                station.setTime(time);
            }
        }
    }

    private void calculateForNamTask(StationReportBean station, TaskReportBean task,
                                     List<TaskDetailNamDataView> subTaskDetails,
                                     List<TaskDefaultValue> taskDefaultValues, ReportAction reportAction) {
        List<TaskDetailNamDataView> select;

        if (reportAction.getBranchId() != null) {
            select = select(subTaskDetails, having(on(TaskDetailNamDataView.class).getTaskId(), equalTo(task.getId())).
                    and(having(on(TaskDetailNamDataView.class).getStationId(), equalTo(station.getId()))).
                    and(having(on(TaskDetailNamDataView.class).getBranchId(), equalTo(reportAction.getBranchId()))));
        } else {
            select = select(subTaskDetails, having(on(TaskDetailNamDataView.class).getTaskId(), equalTo(task.getId())).
                    and(having(on(TaskDetailNamDataView.class).getStationId(), equalTo(station.getId()))));
        }

        if (CollectionUtils.isNotEmpty(select)) {
            double weight = 0d;
            double time = 0d;
            for (TaskDetailNamDataView taskDetail : select) {
                weight += calculateWeightForDKAndNamTask(reportAction.getReportTypeEnum(), taskDetail.getQ1(),
                        taskDetail.getQ2(), taskDetail.getQ3(), taskDetail.getQ4());
                time += calculateTimeForDKAndNamTask(reportAction, task, taskDefaultValues,
                        taskDetail.getQ1(), taskDetail.getQ2(), taskDetail.getQ3(), taskDetail.getQ4());
            }
            if (weight > 0) {
                station.setValue(weight);
            }
            if (time > 0) {
                station.setTime(time);
            }
        }
    }

    private double calculateTimeForDKAndNamTask(ReportAction reportAction, TaskReportBean task, List<TaskDefaultValue> taskDefaultValues,
                                                Double weightQ1, Double weightQ2, Double weightQ3, Double weightQ4) {
        double defaultValue = 0d;
        double defaultValueQ1 = 0d;
        double defaultValueQ2 = 0d;
        double defaultValueQ3 = 0d;
        double defaultValueQ4 = 0d;
        weightQ1 = convertNullToDouble(weightQ1);
        weightQ2 = convertNullToDouble(weightQ2);
        weightQ3 = convertNullToDouble(weightQ3);
        weightQ4 = convertNullToDouble(weightQ4);
        if (reportAction.getReportTypeEnum() != CA_NAM) {
            defaultValue = getDefaultValue(taskDefaultValues, task, reportAction.getYear(),
                    reportAction.getReportTypeEnum().getValue());
            task.setDefaultValue(defaultValue);
        } else {
            defaultValueQ1 = getDefaultValue(taskDefaultValues, task, reportAction.getYear(), Q1.getValue());
            defaultValueQ2 = getDefaultValue(taskDefaultValues, task, reportAction.getYear(), Q2.getValue());
            defaultValueQ3 = getDefaultValue(taskDefaultValues, task, reportAction.getYear(), Q3.getValue());
            defaultValueQ4 = getDefaultValue(taskDefaultValues, task, reportAction.getYear(), Q4.getValue());
        }

        switch (reportAction.getReportTypeEnum()) {
            case Q1:
                return defaultValue * weightQ1 * task.getQuota();
            case Q2:
                return defaultValue * weightQ2 * task.getQuota();
            case Q3:
                return defaultValue * weightQ3 * task.getQuota();
            case Q4:
                return defaultValue * weightQ4 * task.getQuota();
            case CA_NAM:
                double time = (defaultValueQ1 * weightQ1 + defaultValueQ2 * weightQ2
                        + defaultValueQ3 * weightQ3 + defaultValueQ4 * weightQ4) * task.getQuota();
                double yearDefaultValue = time / (weightQ1 + weightQ2 + weightQ3 + weightQ4) / task.getQuota();
                task.setDefaultValue(yearDefaultValue);
                return time;
        }
        return 0d;
    }

    private double calculateWeightForDKAndNamTask(ReportTypeEnum reportTypeEnum,
                                                  Double q1, Double q2, Double q3, Double q4) {
        q1 = convertNullToDouble(q1);
        q2 = convertNullToDouble(q2);
        q3 = convertNullToDouble(q3);
        q4 = convertNullToDouble(q4);

        switch (reportTypeEnum) {
            case Q1:
                return q1;
            case Q2:
                return q2;
            case Q3:
                return q3;
            case Q4:
                return q4;
            case CA_NAM:
                return q1 + q2 + q3 + q4;
        }
        return 0d;
    }


    private void calculateForDKTask(StationReportBean station, TaskReportBean task,
                                    List<TaskDetailDKDataView> subAnnualTaskDetails,
                                    List<TaskDefaultValue> taskDefaultValues, List<TaskQuota> taskQuotas, ReportAction reportAction) {
        List<TaskQuota> selectTaskQuotas = select(taskQuotas,
                having(on(TaskQuota.class).getTask().getId(), equalTo(task.getId())).
                        and(having(on(TaskQuota.class).getYear(), lessThanOrEqualTo(reportAction.getYear()))));
        TaskQuota taskQuota = selectMax(selectTaskQuotas, on(TaskQuota.class).getYear());
        Preconditions.checkNotNull(taskQuota, "No task quota for this task's id "
                + task.getId() + " in year " + reportAction.getYear());
        int defaultQuota = 0;
        if (Q1 == reportAction.getReportTypeEnum()) {
            defaultQuota = taskQuota.getQ1();
        } else if (Q2 == reportAction.getReportTypeEnum()) {
            defaultQuota = taskQuota.getQ2();
        } else if (Q3 == reportAction.getReportTypeEnum()) {
            defaultQuota = taskQuota.getQ3();
        } else if (Q4 == reportAction.getReportTypeEnum()) {
            defaultQuota = taskQuota.getQ4();
        }

        List<TaskDetailDKDataView> select;
        if (reportAction.getBranchId() != null) {
            select = select(subAnnualTaskDetails, having(on(TaskDetailDKDataView.class).getTaskId(), equalTo(task.getId())).
                    and(having(on(TaskDetailDKDataView.class).getStationId(), equalTo(station.getId()))).
                    and(having(on(TaskDetailDKDataView.class).getBranchId(), equalTo(reportAction.getBranchId()))));
        } else {
            select = select(subAnnualTaskDetails, having(on(TaskDetailDKDataView.class).getTaskId(), equalTo(task.getId())).
                    and(having(on(TaskDetailDKDataView.class).getStationId(), equalTo(station.getId()))));
        }

        if (CollectionUtils.isNotEmpty(select)) {
            Double weight = 0d;
            for (TaskDetailDKDataView subTaskAnnualDetail : select) {
                if (subTaskAnnualDetail.getRealValue() != null) {
                    weight += subTaskAnnualDetail.getRealValue();
                }
            }
            if (weight > 0) {
                station.setValue(weight);
            }
        }

        //Calculate time
        if (station.getValue() != null) {
            Double time = 0d;
            double defaultValue = 0d;
            if (reportAction.getReportTypeEnum() != CA_NAM) {
                defaultValue = getDefaultValue(taskDefaultValues, task, reportAction.getYear(),
                        reportAction.getReportTypeEnum().getValue());
                time = defaultValue * defaultQuota * station.getValue();
            } else {
                double defaultValueQ1 = getDefaultValue(taskDefaultValues, task, reportAction.getYear(), Q1.getValue());
                double defaultValueQ2 = getDefaultValue(taskDefaultValues, task, reportAction.getYear(), Q2.getValue());
                double defaultValueQ3 = getDefaultValue(taskDefaultValues, task, reportAction.getYear(), Q3.getValue());
                double defaultValueQ4 = getDefaultValue(taskDefaultValues, task, reportAction.getYear(), Q4.getValue());
                time = station.getValue() * (defaultValueQ1 * taskQuota.getQ1() + defaultValueQ2 * taskQuota.getQ2()
                        + defaultValueQ3 * taskQuota.getQ3() + defaultValueQ4 * taskQuota.getQ4()
                );
                defaultQuota = taskQuota.getQ1() + taskQuota.getQ2() + taskQuota.getQ3() + taskQuota.getQ4();
                defaultValue = time / defaultQuota / station.getValue();
            }
            task.setDefaultValue(defaultValue);
            task.setQuota(defaultQuota);
            station.setTime(time);
        }
    }

    private double getDefaultValue(List<TaskDefaultValue> taskDefaultValues, TaskReportBean task, int year, int quarter) {
        List<TaskDefaultValue> selectValues = select(taskDefaultValues,
                having(on(TaskDefaultValue.class).getYear(), greaterThanOrEqualTo(year)).
                        and(having(on(TaskDefaultValue.class).getQuarter(), greaterThan(quarter)).
                                and(having(on(TaskDefaultValue.class).getTask().getId(), equalTo(task.getId())))
                        )
        );
        TaskDefaultValue taskDefaultValue = selectMin(selectValues, on(TaskDefaultValue.class).getId());
        Double defaultValue;
        if (taskDefaultValue != null) {
            defaultValue = taskDefaultValue.getDefaultValue();
        } else {
            defaultValue = task.getDefaultValue();
        }
        return defaultValue;
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
