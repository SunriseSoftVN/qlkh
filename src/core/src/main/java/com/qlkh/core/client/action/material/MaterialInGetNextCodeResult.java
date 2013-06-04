package com.qlkh.core.client.action.material;

import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class MaterialInAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/4/13 4:35 PM
 */
public class MaterialInGetNextCodeResult implements Result {

    private long code;

    public MaterialInGetNextCodeResult() {
    }

    public MaterialInGetNextCodeResult(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }
}
