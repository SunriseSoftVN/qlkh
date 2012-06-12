/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.TaskDetailNamConstant;
import com.qlkh.client.client.module.content.view.security.TaskDetailNamSecurity;
import com.qlkh.client.client.module.content.view.share.AbstractTaskDetailView;
import com.qlkh.client.client.widget.MyNumberField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TaskDetailNamView.
 *
 * @author Nguyen Duc Dung
 * @since 6/11/12, 9:03 PM
 */
@ViewSecurity(configuratorClass = TaskDetailNamSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = TaskDetailNamConstant.class)
public class TaskDetailNamView extends AbstractTaskDetailView<TaskDetailNamConstant> {

    public static final String BRANCH_NAME_COLUMN = "branch.name";
    public static int BRANCH_NAME_WIDTH = 150;
    public static final String LAST_YEAR_VALUE_COLUMN = "lastYearValue";
    public static final int LAST_YEAR_VALUE_WIDTH = 70;
    public static final String INCREASE_VALUE_COLUMN = "increaseValue";
    public static final int INCREASE_VALUE_WIDTH = 45;
    public static final String DECREASE_VALUE_COLUMN = "decreaseValue";
    public static final int DECREASE_VALUE_WIDTH = 45;
    public static final String REAL_VALUE_COLUMN = "realValue";
    public static final int REAL_VALUE_WIDTH = 70;
    public static final String Q1_UNIT_COLUMN = "q1";
    public static final int Q1_UNIT_WIDTH = 45;
    public static final String Q2_UNIT_COLUMN = "q2";
    public static final int Q2_UNIT_WIDTH = 45;
    public static final String Q3_UNIT_COLUMN = "q3";
    public static final int Q3_UNIT_WIDTH = 45;
    public static final String Q4_UNIT_COLUMN = "q4";
    public static final int Q4_UNIT_WIDTH = 45;

    private int currentYear;
    private boolean q1Lock;
    private boolean q2Lock;
    private boolean q3Lock;
    private boolean q4Lock;

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
        lastYearValueColumnConfig.setEditor(new CellEditor(lastYearValueNumberField));
        lastYearValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        columnConfigs.add(lastYearValueColumnConfig);

        ColumnConfig increaseValueColumnConfig = new ColumnConfig(INCREASE_VALUE_COLUMN,
                getConstant().increaseValueColumnTitle(), INCREASE_VALUE_WIDTH);
        MyNumberField increaseValueNumberField = new MyNumberField();
        increaseValueNumberField.setSelectOnFocus(true);
        increaseValueColumnConfig.setEditor(new CellEditor(increaseValueNumberField));
        increaseValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        columnConfigs.add(increaseValueColumnConfig);

        ColumnConfig decreaseValueColumnConfig = new ColumnConfig(DECREASE_VALUE_COLUMN,
                getConstant().decreaseValueColumnTitle(), DECREASE_VALUE_WIDTH);
        MyNumberField decreaseValueNumberField = new MyNumberField();
        decreaseValueNumberField.setSelectOnFocus(true);
        decreaseValueColumnConfig.setEditor(new CellEditor(decreaseValueNumberField));
        decreaseValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        columnConfigs.add(decreaseValueColumnConfig);

        SummaryColumnConfig realValueColumnConfig = new SummaryColumnConfig(REAL_VALUE_COLUMN,
                getConstant().realValueColumnTitle() + " " + currentYear, REAL_VALUE_WIDTH);
        realValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        columnConfigs.add(realValueColumnConfig);

        ColumnConfig q1ColumnConfig = new ColumnConfig(Q1_UNIT_COLUMN, getConstant().q1ColumnTitle(), Q1_UNIT_WIDTH);
        if (!q1Lock) {
            MyNumberField q1NumberField = new MyNumberField();
            q1NumberField.setSelectOnFocus(true);
            q1ColumnConfig.setEditor(new CellEditor(q1NumberField));
        }
        columnConfigs.add(q1ColumnConfig);

        ColumnConfig q2ColumnConfig = new ColumnConfig(Q2_UNIT_COLUMN, getConstant().q2ColumnTitle(), Q2_UNIT_WIDTH);
        if (!q2Lock) {
            MyNumberField q2NumberField = new MyNumberField();
            q2NumberField.setSelectOnFocus(true);
            q2ColumnConfig.setEditor(new CellEditor(q2NumberField));
        }
        columnConfigs.add(q2ColumnConfig);

        ColumnConfig q3ColumnConfig = new ColumnConfig(Q3_UNIT_COLUMN, getConstant().q3ColumnTitle(), Q3_UNIT_WIDTH);
        if (!q3Lock) {
            MyNumberField q3NumberField = new MyNumberField();
            q3NumberField.setSelectOnFocus(true);
            q3ColumnConfig.setEditor(new CellEditor(q3NumberField));
        }
        columnConfigs.add(q3ColumnConfig);

        ColumnConfig q4ColumnConfig = new ColumnConfig(Q4_UNIT_COLUMN, getConstant().q4ColumnTitle(), Q4_UNIT_WIDTH);
        if (!q4Lock) {
            MyNumberField q4NumberField = new MyNumberField();
            q4NumberField.setSelectOnFocus(true);
            q4ColumnConfig.setEditor(new CellEditor(q4NumberField));
        }
        columnConfigs.add(q4ColumnConfig);

        return columnConfigs;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public void setQ1Lock(boolean q1Lock) {
        this.q1Lock = q1Lock;
    }

    public void setQ2Lock(boolean q2Lock) {
        this.q2Lock = q2Lock;
    }

    public void setQ3Lock(boolean q3Lock) {
        this.q3Lock = q3Lock;
    }

    public void setQ4Lock(boolean q4Lock) {
        this.q4Lock = q4Lock;
    }
}