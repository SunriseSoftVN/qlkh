package com.qlkh.core.client.action.report;

import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class PriceReportAction.
 *
 * @author Nguyen Duc Dung
 * @since 4/14/13 10:54 AM
 */
public class PriceReportResult implements Result {

    private String reportUrl;

    public PriceReportResult() {
    }

    public PriceReportResult(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getReportUrl() {
        return reportUrl;
    }
}
