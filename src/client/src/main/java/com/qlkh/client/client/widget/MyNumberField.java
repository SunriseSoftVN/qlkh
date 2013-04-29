/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.widget;

import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.google.gwt.i18n.client.NumberFormat;

/**
 * The Class MyNumberField.
 *
 * @author Nguyen Duc Dung
 * @since 1/30/12, 3:49 PM
 */
public class MyNumberField extends NumberField {
    public MyNumberField() {
        super();
        decimalSeparator = ".";
        setFormat(NumberFormat.getFormat("###,###.##"));
    }
}
