package com.qlkh.core.client.action.material;

import com.qlkh.core.client.constant.ReportTypeEnum;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadMaterialInTotalAction.
 *
 * @author Nguyen Duc Dung
 * @since 4/29/13 2:53 PM
 */
public class LoadMaterialInTotalAction implements Action<LoadMaterialInTotalResult> {

    private ReportTypeEnum reportTypeEnum;
    private long stationId;
    private Long branchId;
    private int year;
    private long materialId;

    public LoadMaterialInTotalAction() {
    }

    public LoadMaterialInTotalAction(long materialId, long stationId, Long branchId, int quarter, int year) {
        this.materialId = materialId;
        this.reportTypeEnum = ReportTypeEnum.valueOf(quarter);
        this.stationId = stationId;
        this.branchId = branchId;
        this.year = year;
    }

    public ReportTypeEnum getReportTypeEnum() {
        return reportTypeEnum;
    }

    public void setReportTypeEnum(ReportTypeEnum reportTypeEnum) {
        this.reportTypeEnum = reportTypeEnum;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getMaterialId() {
        return materialId;
    }
}
