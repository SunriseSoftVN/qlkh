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
import com.qlkh.client.client.utils.NumberUtils;
import com.qlkh.client.client.utils.TaskCodeUtils;
import com.qlkh.core.client.action.report.TaskReportAction;
import com.qlkh.core.client.action.report.TaskReportResult;
import com.qlkh.core.client.constant.ReportFileTypeEnum;
import com.qlkh.core.client.constant.ReportTypeEnum;
import com.qlkh.core.client.constant.TaskTypeEnum;
import com.qlkh.core.client.model.Branch;
import com.qlkh.core.client.model.Station;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.TaskDefaultValue;
import com.qlkh.core.client.model.view.TaskDetailDKDataView;
import com.qlkh.core.client.model.view.TaskDetailKDKDataView;
import com.qlkh.core.client.model.view.TaskDetailNamDataView;
import com.qlkh.core.client.report.StationReportBean;
import com.qlkh.core.client.report.TaskReportBean;
import com.qlkh.core.client.report.TaskSumReportBean;
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
import static com.qlkh.server.business.rule.StationCodeEnum.CAUGIAT;
import static com.qlkh.server.business.rule.StationCodeEnum.COMPANY;
import static com.qlkh.server.util.DateTimeUtils.getDateForQuarter;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * The Class ReportHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 6:52 PM
 */
public class TaskReportHandler extends AbstractHandler<TaskReportAction, TaskReportResult> {

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
    public Class<TaskReportAction> getActionType() {
        return TaskReportAction.class;
    }

    @Override
    public TaskReportResult execute(TaskReportAction action, ExecutionContext context) throws DispatchException {
        return new TaskReportResult(reportForCompany(action));
    }

    private String reportForCompany(TaskReportAction action) throws ActionException {
        ReportFileTypeEnum fileTypeEnum = action.getFileTypeEnum();
        try {
            DynamicReport dynamicReport = buildReportLayout(action);
            JasperReport jasperReport = DynamicJasperHelper.
                    generateJasperReport(dynamicReport, new ClassicLayoutManager(), null);

            JasperPrint jasperPrint = JasperFillManager.
                    fillReport(jasperReport, null, new JRBeanCollectionDataSource(buildReportData(action)));

            String fileName = REPORT_FILE_NAME;
            if (action.getBranchId() != null) {
                Branch branch = generalDao.findById(Branch.class, action.getBranchId());
                fileName += "_" + StringUtils.convertNonAscii(branch.getName()).
                        replaceAll(" ", "_").toLowerCase();
            } else {
                Station station = generalDao.findById(Station.class, action.getStationId());
                fileName += "_" + StringUtils.convertNonAscii(station.getName()).
                        replaceAll(" ", "_").toLowerCase();
            }

            fileName += "_" + action.getReportTypeEnum() + "_"
                    + action.getYear() + "_" + action.getReportFormEnum() + fileTypeEnum.getFileExt();

            String filePath = ServletUtils.getInstance().getRealPath(ReportServlet.REPORT_DIRECTORY, fileName);

            if (fileTypeEnum == ReportFileTypeEnum.PDF) {
                ReportExporter.exportReport(jasperPrint, filePath);
            } else if (fileTypeEnum == ReportFileTypeEnum.EXCEL) {
                ReportExporter.exportReportXls(jasperPrint, filePath);
            }

            return new StringBuilder().append(ConfigurationServerUtil.getServerBaseUrl())
                    .append(ConfigurationServerUtil.getConfiguration().serverServletRootPath())
                    .append(ReportServlet.REPORT_SERVLET_URI)
                    .append(ReportServlet.REPORT_FILENAME_PARAMETER)
                    .append("=")
                    .append(fileName).toString();

        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DynamicReport buildReportLayout(TaskReportAction action) {

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
        headerStyle.setBorder(Border.THIN());
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);

        Style detailStyle = new Style();
        detailStyle.setFont(DEFAULT_FONT);
        detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        detailStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        detailStyle.setBorderLeft(Border.THIN());
        detailStyle.setBorderRight(Border.THIN());
        detailStyle.setBorderBottom(Border.THIN());

        Style numberStyle = new Style();
        numberStyle.setFont(DEFAULT_FONT);
        numberStyle.setBorder(Border.THIN());
        numberStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
        numberStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        numberStyle.setBorderLeft(Border.THIN());
        numberStyle.setBorderRight(Border.THIN());
        numberStyle.setBorderBottom(Border.THIN());
        numberStyle.setPaddingRight(5);

        Style nameStyle = new Style();
        nameStyle.setFont(DEFAULT_FONT);
        nameStyle.setBorder(Border.THIN());
        nameStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        nameStyle.setBorderLeft(Border.THIN());
        nameStyle.setBorderRight(Border.THIN());
        nameStyle.setBorderBottom(Border.THIN());

        try {
            fastReportBuilder.addColumn("Mã CV", "task.code", String.class, 15, detailStyle)
                    .addColumn("Nội dung công việc", "task.name", String.class, 80, nameStyle)
                    .addColumn("Đơn vị", "task.unit", String.class, 15, detailStyle)
                    .addColumn("Định mức", "task.defaultValueForPrinting", Double.class, 15, false, "###,###.###", detailStyle)
                    .addColumn("Số lần", "task.quotaForPrinting", Integer.class, 15, detailStyle);

            List<Station> stations = new ArrayList<Station>();
            if (stationId == COMPANY.getId()) {
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
                    if (stationId == CAUGIAT.getId()) {
                        //Add two more columns. Business rule. TODO remove @dungvn3000
                        AdditionStationColumnRule.addStation(stations);
                    }
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

                if (stationId == COMPANY.getId()) {
                    for (Integer colIndex : colSpans.keySet()) {
                        fastReportBuilder.setColspan(colIndex, 2, colSpans.get(colIndex));
                    }
                    //set style two last columns. Business rule. TODO remove @dungvn3000
                    AdditionStationColumnRule.setStyle(fastReportBuilder.getColumns());
                }

                //Add two more column for CAU GIAT station report.
                if (stationId == CAUGIAT.getId() && branchId == null) {
                    for (Integer colIndex : colSpans.keySet()) {
                        fastReportBuilder.setColspan(colIndex, 2, colSpans.get(colIndex));
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        fastReportBuilder.setHeaderHeight(50);
        if (stationId == COMPANY.getId()) {
            fastReportBuilder.setPageSizeAndOrientation(Page.Page_A4_Landscape());
        } else {
            fastReportBuilder.setPageSizeAndOrientation(Page.Page_A4_Portrait());
        }

        fastReportBuilder.setDefaultStyles(titleStyle, null, headerStyle, null);
        fastReportBuilder.setUseFullPageWidth(true);
        fastReportBuilder.setLeftMargin(10);

        if (reportFileTypeEnum == ReportFileTypeEnum.EXCEL) {
            fastReportBuilder.setIgnorePagination(true);
        }

        return fastReportBuilder.build();
    }

    public List<TaskSumReportBean> buildReportData(TaskReportAction taskReportAction) throws ActionException {
        long stationId = taskReportAction.getStationId();

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
                getTaskDetailKDK(stationIds, taskReportAction.getYear());
        List<TaskDetailDKDataView> subAnnualTaskDetails = sqlQueryDao
                .getTaskDetailDK(stationIds, taskReportAction.getYear());
        List<TaskDetailNamDataView> taskDetailNams = sqlQueryDao.getTaskDetailNam(stationIds, taskReportAction.getYear());
        List<TaskDefaultValue> taskDefaultValues = generalDao.getAll(TaskDefaultValue.class);

        List<TaskSumReportBean> beans = new ArrayList<TaskSumReportBean>();
        List<TaskSumReportBean> parentBeans = new ArrayList<TaskSumReportBean>();
        List<Task> tasks = generalDao.getAll(Task.class, Order.asc("code"));
        for (Task task : tasks) {
            TaskSumReportBean bean = new TaskSumReportBean();
            bean.setTask(task);
            for (Station station : stations) {
                StationReportBean stationReport = calculateForStation(bean.getTask(), station,
                        taskReportAction, subTaskDetails, subAnnualTaskDetails, taskDetailNams,
                        taskDefaultValues);
                bean.getStations().put(String.valueOf(stationReport.getId()), stationReport);
            }
            beans.add(bean);
            if (task.getTaskTypeCode() == SUBSUM.getCode()
                    || task.getTaskTypeCode() == SUM.getCode()) {
                parentBeans.add(bean);
            }
        }

        //Add two more columns. Business rule.
        if (stationId == COMPANY.getId() || stationId == CAUGIAT.getId()) {
            AdditionStationColumnRule.addDataForDSND(beans);
        }

        //Build tree
        buildTree(beans, parentBeans);
        //Calculate for Sum or SubSum Task.
        forEach(beans).calculate();

        //Sort by Alphabetical.
        Collections.sort(beans);

        //Business Order.
        ReportOrderRule.sort(beans);

        //Remove empty task, exclude sum task
        List<TaskSumReportBean> removeBeans = new ArrayList<TaskSumReportBean>();
        for (TaskSumReportBean bean : beans) {
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
        if (stationId == COMPANY.getId()) {
            calculateForCompany(beans);
        }

        //Hide unnecessary detail
        HideDetailTaskRule.hide(beans, taskReportAction);

        //Calculate for DSTN
        if (stationId == COMPANY.getId() || stationId == CAUGIAT.getId()) {
            AdditionStationColumnRule.addDataForDSTN(beans, stationId);
        }

        return beans;
    }


    private void buildTree(List<TaskSumReportBean> beans,
                           List<TaskSumReportBean> parentBeans) {
        for (TaskSumReportBean childBean : beans) {
            for (TaskSumReportBean parentBean : parentBeans) {
                if (isParent(parentBean, childBean)) {
                    parentBean.getChildBeans().add(childBean);
                }
            }
        }
    }

    private boolean isParent(TaskSumReportBean parentBean, TaskSumReportBean childBean) {
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
                    break;
            }
        }
        return false;
    }


    private StationReportBean calculateForStation(TaskReportBean task, Station station,
                                                  TaskReportAction taskReportAction,
                                                  List<TaskDetailKDKDataView> taskDetailKDKs,
                                                  List<TaskDetailDKDataView> taskDetailDKs,
                                                  List<TaskDetailNamDataView> taskDetailNams,
                                                  List<TaskDefaultValue> taskDefaultValues) {
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
                        taskDetailDKs, taskDefaultValues, taskReportAction);
            } else if (TaskTypeEnum.KDK.getCode() == task.getTaskTypeCode()) {
                calculateForKDKTask(stationReport, task, taskDetailKDKs, taskDefaultValues, taskReportAction);
            } else if (TaskTypeEnum.NAM.getCode() == task.getTaskTypeCode()) {
                calculateForNamTask(stationReport, task, taskDetailNams, taskDefaultValues, taskReportAction);
            }
        }
        return stationReport;
    }

    private void calculateForKDKTask(StationReportBean station, TaskReportBean task,
                                     List<TaskDetailKDKDataView> subTaskDetails,
                                     List<TaskDefaultValue> taskDefaultValues, TaskReportAction taskReportAction) {
        List<TaskDetailKDKDataView> select;

        if (taskReportAction.getBranchId() != null) {
            select = select(subTaskDetails, having(on(TaskDetailKDKDataView.class).getTaskId(), equalTo(task.getId())).
                    and(having(on(TaskDetailKDKDataView.class).getStationId(), equalTo(station.getId()))).
                    and(having(on(TaskDetailKDKDataView.class).getBranchId(), equalTo(taskReportAction.getBranchId()))));
        } else {
            select = select(subTaskDetails, having(on(TaskDetailKDKDataView.class).getTaskId(), equalTo(task.getId())).
                    and(having(on(TaskDetailKDKDataView.class).getStationId(), equalTo(station.getId()))));
        }

        if (CollectionUtils.isNotEmpty(select)) {
            Double weight = 0d;
            double time = 0d;
            for (TaskDetailKDKDataView taskDetail : select) {
                weight += calculateWeightForDKAndNamTask(taskReportAction.getReportTypeEnum(), taskDetail.getQ1(),
                        taskDetail.getQ2(), taskDetail.getQ3(), taskDetail.getQ4());
                time += calculateTimeForDKAndNamTask(taskReportAction, task, taskDefaultValues,
                        taskDetail.getQ1(), taskDetail.getQ2(), taskDetail.getQ3(), taskDetail.getQ4());
                if (taskDetail.getBranchId() == BranchCodeEnum.ND.getId()) {
                    double ndWeight = calculateWeightForDKAndNamTask(taskReportAction.getReportTypeEnum(), taskDetail.getQ1(),
                            taskDetail.getQ2(), taskDetail.getQ3(), taskDetail.getQ4());
                    double ndTime = calculateTimeForDKAndNamTask(taskReportAction, task, taskDefaultValues,
                            taskDetail.getQ1(), taskDetail.getQ2(), taskDetail.getQ3(), taskDetail.getQ4());
                    if (ndWeight > 0) {
                        station.setNdValue(ndWeight);
                        station.setNdTime(ndTime);
                    }
                }
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
                                     List<TaskDefaultValue> taskDefaultValues, TaskReportAction taskReportAction) {
        List<TaskDetailNamDataView> select;

        if (taskReportAction.getBranchId() != null) {
            select = select(subTaskDetails, having(on(TaskDetailNamDataView.class).getTaskId(), equalTo(task.getId())).
                    and(having(on(TaskDetailNamDataView.class).getStationId(), equalTo(station.getId()))).
                    and(having(on(TaskDetailNamDataView.class).getBranchId(), equalTo(taskReportAction.getBranchId()))));
        } else {
            select = select(subTaskDetails, having(on(TaskDetailNamDataView.class).getTaskId(), equalTo(task.getId())).
                    and(having(on(TaskDetailNamDataView.class).getStationId(), equalTo(station.getId()))));
        }

        if (CollectionUtils.isNotEmpty(select)) {
            double weight = 0d;
            double time = 0d;
            for (TaskDetailNamDataView taskDetail : select) {
                weight += calculateWeightForDKAndNamTask(taskReportAction.getReportTypeEnum(), taskDetail.getQ1(),
                        taskDetail.getQ2(), taskDetail.getQ3(), taskDetail.getQ4());
                time += calculateTimeForDKAndNamTask(taskReportAction, task, taskDefaultValues,
                        taskDetail.getQ1(), taskDetail.getQ2(), taskDetail.getQ3(), taskDetail.getQ4());
                if (taskDetail.getBranchId() == BranchCodeEnum.ND.getId()) {
                    double ndWeight = calculateWeightForDKAndNamTask(taskReportAction.getReportTypeEnum(), taskDetail.getQ1(),
                            taskDetail.getQ2(), taskDetail.getQ3(), taskDetail.getQ4());
                    double ndTime = calculateTimeForDKAndNamTask(taskReportAction, task, taskDefaultValues,
                            taskDetail.getQ1(), taskDetail.getQ2(), taskDetail.getQ3(), taskDetail.getQ4());
                    if (ndWeight > 0) {
                        station.setNdValue(ndWeight);
                        station.setNdTime(ndTime);
                    }
                }
            }
            if (weight > 0) {
                station.setValue(weight);
            }
            if (time > 0) {
                station.setTime(time);
            }
        }
    }

    private double calculateTimeForDKAndNamTask(TaskReportAction taskReportAction, TaskReportBean task, List<TaskDefaultValue> taskDefaultValues,
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
        if (taskReportAction.getReportTypeEnum() != CA_NAM) {
            defaultValue = getDefaultValue(taskDefaultValues, task, taskReportAction.getYear(),
                    taskReportAction.getReportTypeEnum().getValue());
            task.setDefaultValueForPrinting(defaultValue);
        } else {
            defaultValueQ1 = getDefaultValue(taskDefaultValues, task, taskReportAction.getYear(), Q1.getValue());
            defaultValueQ2 = getDefaultValue(taskDefaultValues, task, taskReportAction.getYear(), Q2.getValue());
            defaultValueQ3 = getDefaultValue(taskDefaultValues, task, taskReportAction.getYear(), Q3.getValue());
            defaultValueQ4 = getDefaultValue(taskDefaultValues, task, taskReportAction.getYear(), Q4.getValue());
        }

        switch (taskReportAction.getReportTypeEnum()) {
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
                if (yearDefaultValue > 0) {
                    task.setDefaultValueForPrinting(yearDefaultValue);
                }
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
                                    List<TaskDefaultValue> taskDefaultValues, TaskReportAction taskReportAction) {

        int defaultQuota = 0;
        if (task.isDynamicQuota()) {
            if (taskReportAction.getReportTypeEnum() != CA_NAM) {
                defaultQuota = getDateForQuarter(taskReportAction.getReportTypeEnum().getValue(),
                        taskReportAction.getYear());
            }
        } else {
            defaultQuota = task.getQuota();
        }

        List<TaskDetailDKDataView> select;
        if (taskReportAction.getBranchId() != null) {
            select = select(subAnnualTaskDetails, having(on(TaskDetailDKDataView.class).getTaskId(), equalTo(task.getId())).
                    and(having(on(TaskDetailDKDataView.class).getStationId(), equalTo(station.getId()))).
                    and(having(on(TaskDetailDKDataView.class).getBranchId(), equalTo(taskReportAction.getBranchId()))));
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
                //Only for display, not for any calculation.
                if (subTaskAnnualDetail.getBranchId() == BranchCodeEnum.ND.getId()) {
                    Double ndWeight = subTaskAnnualDetail.getRealValue();
                    if (ndWeight != null && ndWeight > 0) {
                        station.setNdValue(ndWeight);
                    }
                }
            }
            if (weight > 0) {
                station.setValue(weight);
            }
        }

        //Calculate time
        if (station.getValue() != null) {
            Double time = 0d;
            Double ndTime = 0d;
            double defaultValue = 0d;
            if (taskReportAction.getReportTypeEnum() != CA_NAM) {
                defaultValue = getDefaultValue(taskDefaultValues, task, taskReportAction.getYear(),
                        taskReportAction.getReportTypeEnum().getValue());
                time = defaultValue * defaultQuota * station.getValue();
                if (station.getNdValue() != null) {
                    ndTime = defaultValue * defaultQuota * station.getNdValue();
                }
            } else {
                double defaultValueQ1 = getDefaultValue(taskDefaultValues, task, taskReportAction.getYear(), Q1.getValue());
                double defaultValueQ2 = getDefaultValue(taskDefaultValues, task, taskReportAction.getYear(), Q2.getValue());
                double defaultValueQ3 = getDefaultValue(taskDefaultValues, task, taskReportAction.getYear(), Q3.getValue());
                double defaultValueQ4 = getDefaultValue(taskDefaultValues, task, taskReportAction.getYear(), Q4.getValue());
                if (task.isDynamicQuota()) {
                    int quotaQ1 = getDateForQuarter(Q1.getValue(),
                            taskReportAction.getYear());
                    int quotaQ2 = getDateForQuarter(Q2.getValue(),
                            taskReportAction.getYear());
                    int quotaQ3 = getDateForQuarter(Q3.getValue(),
                            taskReportAction.getYear());
                    int quotaQ4 = getDateForQuarter(Q4.getValue(),
                            taskReportAction.getYear());
                    time = station.getValue() * (defaultValueQ1 * quotaQ1 + defaultValueQ2 * quotaQ2
                            + defaultValueQ3 * quotaQ3 + defaultValueQ4 * quotaQ4
                    );
                    if (station.getNdValue() != null) {
                        ndTime = station.getNdValue() * (defaultValueQ1 * quotaQ1 + defaultValueQ2 * quotaQ2
                                + defaultValueQ3 * quotaQ3 + defaultValueQ4 * quotaQ4
                        );
                    }
                    defaultQuota = quotaQ1 + quotaQ2 + quotaQ3 + quotaQ4;
                } else {
                    time = station.getValue() * defaultQuota * (defaultValueQ1 + defaultValueQ2
                            + defaultValueQ3 + defaultValueQ4);
                    if (station.getNdValue() != null) {
                        ndTime = station.getNdValue() * defaultQuota * (defaultValueQ1 + defaultValueQ2
                                + defaultValueQ3 + defaultValueQ4);
                    }
                    defaultQuota *= 4;
                }
                defaultValue = time / defaultQuota / station.getValue();
            }
            task.setDefaultValueForPrinting(defaultValue);
            task.setQuotaForPrinting(defaultQuota);
            station.setTime(time);
            station.setNdTime(ndTime);
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

    private void calculateForCompany(List<TaskSumReportBean> beans) {
        for (TaskSumReportBean bean : beans) {
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
                bean.getStations().get(String.valueOf(COMPANY.getId())).setValue(sumValue);
            }
            if (sumTime > 0) {
                bean.getStations().get(String.valueOf(COMPANY.getId())).setTime(sumTime);
            }
        }
    }
}
