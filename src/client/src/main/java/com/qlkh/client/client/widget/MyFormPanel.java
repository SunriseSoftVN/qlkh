/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.widget;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;

/**
 * The Class MyFormPanel.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/12, 3:43 AM
 */
public class MyFormPanel extends FormPanel {

    @Override
    public boolean isValid(boolean preventMark) {
        boolean valid = true;
        for (Field<?> f : getFields()) {
            if (f.isVisible() && !f.isValid(preventMark)) {
                valid = false;
            }
        }
        return valid;
    }
}
