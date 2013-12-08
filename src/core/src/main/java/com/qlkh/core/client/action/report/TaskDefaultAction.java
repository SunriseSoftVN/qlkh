package com.qlkh.core.client.action.report;

import com.qlkh.core.client.constant.ReportFileTypeEnum;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class TaskDefaultAction.
 *
 * @author Nguyen Duc Dung
 * @since 12/8/13 9:59 AM
 */
public class TaskDefaultAction implements Action<TaskDefaultResult> {

    private ReportFileTypeEnum fileTypeEnum;

    public TaskDefaultAction() {
    }

    public TaskDefaultAction(ReportFileTypeEnum fileTypeEnum) {
        this.fileTypeEnum = fileTypeEnum;
    }

    public ReportFileTypeEnum getFileTypeEnum() {
        return fileTypeEnum;
    }

    public void setFileTypeEnum(ReportFileTypeEnum fileTypeEnum) {
        this.fileTypeEnum = fileTypeEnum;
    }
}
