package com.qlkh.core.client.action.report;

import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class TaskDefaultAction.
 *
 * @author Nguyen Duc Dung
 * @since 12/8/13 9:59 AM
 */
public class TaskDefaultResult implements Result {

    private String reportUrl;

    public TaskDefaultResult() {
    }

    public TaskDefaultResult(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }
}
