/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.google.gwt.i18n.client.NumberFormat;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.TaskDetailDKConstant;
import com.qlkh.client.client.module.content.view.security.TaskAnnualDetailDK;
import com.qlkh.client.client.module.content.view.share.AbstractTaskDetailView;
import com.qlkh.client.client.widget.DefaultAggregationRenderer;
import com.qlkh.client.client.widget.MyNumberField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TaskAnnualDetailView.
 *
 * @author Nguyen Duc Dung
 * @since 1/4/12, 9:12 PM
 */
@ViewSecurity(configuratorClass = TaskAnnualDetailDK.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = TaskDetailDKConstant.class)
public class TaskDetailDKView extends AbstractTaskDetailView<TaskDetailDKConstant> {

    public static final String BRANCH_NAME_COLUMN = "branch.name";
    public static int BRANCH_NAME_WIDTH = 150;
    public static final String LAST_YEAR_VALUE_COLUMN = "lastYearValue";
    public static final int LAST_YEAR_VALUE_WIDTH = 70;
    public static final String INCREASE_VALUE_COLUMN = "increaseValue";
    public static final int INCREASE_VALUE_WIDTH = 70;
    public static final String DECREASE_VALUE_COLUMN = "decreaseValue";
    public static final int DECREASE_VALUE_WIDTH = 70;
    public static final String REAL_VALUE_COLUMN = "realValue";
    public static final int REAL_VALUE_WIDTH = 80;

    private int currentYear;
    private boolean isLock;

    @Override
    protected List<ColumnConfig> createSubTaskColumnConfigs() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig branchNameColumnConfig = new ColumnConfig(BRANCH_NAME_COLUMN, getConstant().branchNameColumnTitle(),
                BRANCH_NAME_WIDTH);
        columnConfigs.add(branchNameColumnConfig);

        ColumnConfig lastYearValueColumnConfig = new ColumnConfig(LAST_YEAR_VALUE_COLUMN,
                getConstant().lastYearValueColumnTitle() + " " + (currentYear - 1), LAST_YEAR_VALUE_WIDTH);
        MyNumberField lastYearValueNumberField = new MyNumberField();
        lastYearValueNumberField.setSelectOnFocus(true);
        lastYearValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        if (!isLock()) {
            lastYearValueColumnConfig.setEditor(new CellEditor(lastYearValueNumberField));
        } else {
            lastYearValueColumnConfig.setStyle("background-color: #F1F2F4;");
        }
        columnConfigs.add(lastYearValueColumnConfig);

        ColumnConfig increaseValueColumnConfig = new ColumnConfig(INCREASE_VALUE_COLUMN,
                getConstant().increaseValueColumnTitle(), INCREASE_VALUE_WIDTH);
        MyNumberField increaseValueNumberField = new MyNumberField();
        increaseValueNumberField.setSelectOnFocus(true);
        increaseValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        if (!isLock()) {
            increaseValueColumnConfig.setEditor(new CellEditor(increaseValueNumberField));
        } else {
            increaseValueColumnConfig.setStyle("background-color: #F1F2F4;");
        }
        columnConfigs.add(increaseValueColumnConfig);

        ColumnConfig decreaseValueColumnConfig = new ColumnConfig(DECREASE_VALUE_COLUMN,
                getConstant().decreaseValueColumnTitle(), DECREASE_VALUE_WIDTH);
        MyNumberField decreaseValueNumberField = new MyNumberField();
        decreaseValueNumberField.setSelectOnFocus(true);
        decreaseValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        if (!isLock()) {
            decreaseValueColumnConfig.setEditor(new CellEditor(decreaseValueNumberField));
        } else {
            decreaseValueColumnConfig.setStyle("background-color: #F1F2F4;");
        }
        columnConfigs.add(decreaseValueColumnConfig);

        SummaryColumnConfig realValueColumnConfig = new SummaryColumnConfig(REAL_VALUE_COLUMN,
                getConstant().realValueColumnTitle() + " " + currentYear, REAL_VALUE_WIDTH);
        realValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        realValueColumnConfig.setStyle("background-color: #F1F2F4;");
        columnConfigs.add(realValueColumnConfig);

        for (int i = 1; i < columnConfigs.size(); i++) {
            columnConfigs.get(i).setNumberFormat(NumberFormat.getDecimalFormat());
        }
        return columnConfigs;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    @Override
    protected ColumnModel createSubTaskModel() {
        ColumnModel columnModel = super.createSubTaskModel();
        AggregationRowConfig<BeanModel> sumRow = new AggregationRowConfig<BeanModel>();
        sumRow.setHtml(DECREASE_VALUE_COLUMN, getConstant().sumKLTitle());
        sumRow.setSummaryType(REAL_VALUE_COLUMN, SummaryType.SUM);
        sumRow.setRenderer(REAL_VALUE_COLUMN, new DefaultAggregationRenderer());
        columnModel.addAggregationRow(sumRow);
        return columnModel;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }
}
