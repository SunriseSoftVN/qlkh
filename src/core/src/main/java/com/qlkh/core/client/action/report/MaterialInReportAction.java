package com.qlkh.core.client.action.report;

import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class MaterialInReportAction.
 *
 * @author Nguyen Duc Dung
 * @since 5/24/13 11:42 AM
 */
public class MaterialInReportAction implements Action<MaterialInReportResult> {

    private String regex;

    public MaterialInReportAction() {
    }

    public MaterialInReportAction(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public String[] getRegexs() {
        return regex.split(",");
    }
}
