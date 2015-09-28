package com.qlkh.core.client.action.report;

import com.qlkh.core.client.constant.ReportFileTypeEnum;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class MaterialMissingPriceReportAction.
 *
 * @author Nguyen Duc Dung
 * @since 5/23/13 1:29 PM
 */
public class MaterialReportAction implements Action<MaterialReportResult> {

    private ReportFileTypeEnum fileTypeEnum;
    private int quarter;
    private int year;

    public MaterialReportAction() {
    }

    public MaterialReportAction(ReportFileTypeEnum fileTypeEnum, int quarter, int year) {
        this.fileTypeEnum = fileTypeEnum;
        this.quarter = quarter;
        this.year = year;
    }

    public ReportFileTypeEnum getFileTypeEnum() {
        return fileTypeEnum;
    }

    public int getQuarter() {
        return quarter;
    }

    public int getYear() {
        return year;
    }
}
