package com.qlkh.core.client.action.material;

import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class DeleteMaterialResult.
 *
 * @author Nguyen Duc Dung
 * @since 3/26/13 8:06 PM
 */
public class DeleteMaterialResult implements Result {

    private boolean deleted;

    public DeleteMaterialResult() {
    }

    public DeleteMaterialResult(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
