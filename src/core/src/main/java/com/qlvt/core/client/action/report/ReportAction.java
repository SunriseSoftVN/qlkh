/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.qlvt.core.client.action.report;

import com.qlvt.core.client.constant.ReportFileTypeEnum;
import com.qlvt.core.client.constant.ReportTypeEnum;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class ReportAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 6:50 PM
 */
public class ReportAction implements Action<ReportResult> {

    private ReportTypeEnum reportTypeEnum;
    private ReportFileTypeEnum fileTypeEnum;
    private long stationId;

    public ReportAction() {
    }

    public ReportAction(ReportTypeEnum reportTypeEnum, ReportFileTypeEnum fileTypeEnum, long stationId) {
        this.reportTypeEnum = reportTypeEnum;
        this.fileTypeEnum = fileTypeEnum;
        this.stationId = stationId;
    }

    public ReportTypeEnum getReportTypeEnum() {
        return reportTypeEnum;
    }

    public ReportFileTypeEnum getFileTypeEnum() {
        return fileTypeEnum;
    }

    public long getStationId() {
        return stationId;
    }
}
