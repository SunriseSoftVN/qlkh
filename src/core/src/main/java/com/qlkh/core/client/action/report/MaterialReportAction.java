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

    public MaterialReportAction() {
    }

    public MaterialReportAction(ReportFileTypeEnum fileTypeEnum) {
        this.fileTypeEnum = fileTypeEnum;
    }

    public ReportFileTypeEnum getFileTypeEnum() {
        return fileTypeEnum;
    }
}
