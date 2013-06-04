package com.qlkh.core.client.action.report;

import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class MaterialInReportAction.
 *
 * @author Nguyen Duc Dung
 * @since 5/24/13 11:42 AM
 */
public class MaterialOutReportAction implements Action<MaterialOutReportResult> {

    private int form;
    private int to;

    public MaterialOutReportAction() {
    }

    public MaterialOutReportAction(int form, int to) {
        this.form = form;
        this.to = to;
    }

    public int getForm() {
        return form;
    }

    public int getTo() {
        return to;
    }
}
