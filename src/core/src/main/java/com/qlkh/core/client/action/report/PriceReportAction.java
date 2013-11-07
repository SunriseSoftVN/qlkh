package com.qlkh.core.client.action.report;

import com.qlkh.core.client.constant.ReportFileTypeEnum;
import com.qlkh.core.client.constant.ReportTypeEnum;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class PriceReportAction.
 *
 * @author Nguyen Duc Dung
 * @since 4/14/13 10:54 AM
 */
public class PriceReportAction implements Action<PriceReportResult> {

    private ReportTypeEnum reportTypeEnum;
    private ReportFileTypeEnum fileTypeEnum;
    private long stationId;
    private Long branchId;
    private int year;

    public PriceReportAction() {
    }

    public PriceReportAction(ReportTypeEnum reportTypeEnum, ReportFileTypeEnum fileTypeEnum, long stationId, Long branchId, int year) {
        this.reportTypeEnum = reportTypeEnum;
        this.fileTypeEnum = fileTypeEnum;
        this.stationId = stationId;
        this.branchId = branchId;
        this.year = year;
    }

    public ReportTypeEnum getReportTypeEnum() {
        return reportTypeEnum;
    }

    public void setReportTypeEnum(ReportTypeEnum reportTypeEnum) {
        this.reportTypeEnum = reportTypeEnum;
    }

    public ReportFileTypeEnum getFileTypeEnum() {
        return fileTypeEnum;
    }

    public void setFileTypeEnum(ReportFileTypeEnum fileTypeEnum) {
        this.fileTypeEnum = fileTypeEnum;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
