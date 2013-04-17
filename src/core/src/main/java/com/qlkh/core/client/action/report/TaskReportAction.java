/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.report;

import com.qlkh.core.client.constant.ReportFileTypeEnum;
import com.qlkh.core.client.constant.ReportFormEnum;
import com.qlkh.core.client.constant.ReportTypeEnum;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class ReportAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 6:50 PM
 */
public class TaskReportAction implements Action<TaskReportResult> {

    private ReportTypeEnum reportTypeEnum;
    private ReportFileTypeEnum fileTypeEnum;
    private ReportFormEnum reportFormEnum;
    private long stationId;
    private Long branchId;
    private int year;

    public TaskReportAction() {
    }

    public TaskReportAction(PriceReportAction priceReportAction) {
        this.reportTypeEnum = priceReportAction.getReportTypeEnum();
        this.fileTypeEnum = priceReportAction.getFileTypeEnum();
        this.reportFormEnum = ReportFormEnum.MAU_2;
        this.branchId = priceReportAction.getBranchId();
        this.stationId = priceReportAction.getStationId();
        this.year = priceReportAction.getYear();
    }

    public TaskReportAction(ReportTypeEnum reportTypeEnum, ReportFormEnum reportFormEnum,
                            ReportFileTypeEnum fileTypeEnum, long stationId, Long branchId, int year) {
        this.reportFormEnum = reportFormEnum;
        this.reportTypeEnum = reportTypeEnum;
        this.fileTypeEnum = fileTypeEnum;
        this.stationId = stationId;
        this.branchId = branchId;
        this.year = year;
    }

    public ReportTypeEnum getReportTypeEnum() {
        return reportTypeEnum;
    }

    public ReportFileTypeEnum getFileTypeEnum() {
        return fileTypeEnum;
    }

    public long getStationId() {
        return stationId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public int getYear() {
        return year;
    }

    public ReportFormEnum getReportFormEnum() {
        return reportFormEnum;
    }
}
