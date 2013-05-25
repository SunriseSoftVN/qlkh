package com.qlkh.core.client.action.report;

import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class MaterialInReportAction.
 *
 * @author Nguyen Duc Dung
 * @since 5/24/13 11:42 AM
 */
public class MaterialOutReportAction implements Action<MaterialOutReportResult> {

    private String regex;

    public MaterialOutReportAction() {
    }

    public MaterialOutReportAction(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public String[] getRegexs() {
        return regex.split(",");
    }
}
