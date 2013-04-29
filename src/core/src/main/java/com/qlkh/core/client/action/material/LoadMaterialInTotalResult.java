package com.qlkh.core.client.action.material;

import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class LoadMaterialInTotalResult.
 *
 * @author Nguyen Duc Dung
 * @since 4/29/13 2:54 PM
 */
public class LoadMaterialInTotalResult implements Result {

    private double total;

    public LoadMaterialInTotalResult() {
    }

    public LoadMaterialInTotalResult(double total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
