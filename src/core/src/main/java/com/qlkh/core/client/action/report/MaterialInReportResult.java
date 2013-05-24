package com.qlkh.core.client.action.report;

import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class MaterialInReportAction.
 *
 * @author Nguyen Duc Dung
 * @since 5/24/13 11:42 AM
 */
public class MaterialInReportResult implements Result {

    private String reportUrl;

    public MaterialInReportResult() {
    }

    public MaterialInReportResult(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getReportUrl() {
        return reportUrl;
    }
}
