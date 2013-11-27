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
    private boolean pdf;

    public MaterialOutReportAction() {
    }

    public MaterialOutReportAction(int form, int to, boolean pdf) {
        this.form = form;
        this.to = to;
        this.pdf = pdf;
    }

    public int getForm() {
        return form;
    }

    public int getTo() {
        return to;
    }

    public boolean isPdf() {
        return pdf;
    }
}
