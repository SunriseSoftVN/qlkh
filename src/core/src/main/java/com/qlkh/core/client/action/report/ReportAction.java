/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.report;

import com.qlkh.core.client.constant.ReportFileTypeEnum;
import com.qlkh.core.client.constant.ReportTypeEnum;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class ReportAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 6:50 PM
 */
public class ReportAction implements Action<ReportResult> {

    private ReportTypeEnum reportTypeEnum;
    private ReportFileTypeEnum fileTypeEnum;
    private long stationId;
    private Long branchId;
    private int year;

    public ReportAction() {
    }

    public ReportAction(ReportTypeEnum reportTypeEnum,
                        ReportFileTypeEnum fileTypeEnum, long stationId, Long branchId, int year) {
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
}
