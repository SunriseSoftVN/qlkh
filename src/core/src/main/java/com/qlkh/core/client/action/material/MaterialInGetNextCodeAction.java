package com.qlkh.core.client.action.material;

import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class MaterialInAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/4/13 4:35 PM
 */
public class MaterialInGetNextCodeAction implements Action<MaterialInGetNextCodeResult> {

    private int year;

    public MaterialInGetNextCodeAction() {
    }

    public MaterialInGetNextCodeAction(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
