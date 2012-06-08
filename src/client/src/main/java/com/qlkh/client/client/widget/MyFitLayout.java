/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.widget;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Window;

/**
 * The Class MyFitLayout.
 *
 * @author Nguyen Duc Dung
 * @since 1/4/12, 4:12 PM
 */
public class MyFitLayout extends FitLayout {
    @Override
    protected void onLayout(Container<?> container, El target) {
        container.setWidth(Window.getClientWidth() - 5);
        super.onLayout(container, target);
    }
}
