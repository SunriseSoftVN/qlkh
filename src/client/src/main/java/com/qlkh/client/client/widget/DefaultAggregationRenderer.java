/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.widget;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.AggregationRenderer;
import com.extjs.gxt.ui.client.widget.grid.Grid;

import static com.google.gwt.i18n.client.NumberFormat.getFormat;

/**
 * The Class DefaultRender.
 *
 * @author Nguyen Duc Dung
 * @since 6/14/12, 3:58 PM
 */
public class DefaultAggregationRenderer implements AggregationRenderer<BeanModel> {
    @Override
    public Object render(Number value, int colIndex, Grid<BeanModel> beanModelGrid, ListStore<BeanModel> beanModelListStore) {
        if (value != null) {
            return "<p style='font-size:14px;'>" + getFormat("###,###.##").format(value) + "</p>";
        }
        return null;
    }
}
