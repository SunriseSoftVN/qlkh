/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.report;

import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class ReportResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 6:50 PM
 */
public class TaskReportResult implements Result {

    private String reportUrl;

    public TaskReportResult() {
    }

    public TaskReportResult(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getReportUrl() {
        return reportUrl;
    }
}
