package com.qlkh.core.client.action.report;

import net.customware.gwt.dispatch.shared.Result;

import java.util.List;

/**
 * The Class MaterialInReportAction.
 *
 * @author Nguyen Duc Dung
 * @since 5/24/13 11:42 AM
 */
public class MaterialOutReportResult implements Result {

    private List<String> reportUrls;

    public MaterialOutReportResult() {
    }

    public MaterialOutReportResult(List<String> reportUrls) {
        this.reportUrls = reportUrls;
    }

    public List<String> getReportUrls() {
        return reportUrls;
    }
}
