/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.TaskDetailNamConstant;
import com.qlkh.client.client.module.content.view.security.TaskDetailNamSecurity;
import com.qlkh.client.client.module.content.view.share.AbstractTaskDetailView;
import com.qlkh.client.client.widget.DefaultAggregationRenderer;
import com.qlkh.client.client.widget.MyNumberField;
import com.qlkh.core.client.model.TaskDetailNam;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
import java.util.List;

import static com.google.gwt.i18n.client.NumberFormat.getDecimalFormat;
import static com.google.gwt.i18n.client.NumberFormat.getFormat;

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
        lastYearValueColumnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        if (!q4Lock) {
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
        if (!q4Lock) {
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
        if (!q4Lock) {
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

        ColumnConfig q1ColumnConfig = new ColumnConfig(Q1_UNIT_COLUMN, getConstant().q1ColumnTitle(), Q1_UNIT_WIDTH);
        if (!q1Lock) {
            MyNumberField q1NumberField = new MyNumberField();
            q1NumberField.setSelectOnFocus(true);
            q1ColumnConfig.setEditor(new CellEditor(q1NumberField));
        } else {
            q1ColumnConfig.setStyle("background-color: #F1F2F4;");
        }
        columnConfigs.add(q1ColumnConfig);

        ColumnConfig q2ColumnConfig = new ColumnConfig(Q2_UNIT_COLUMN, getConstant().q2ColumnTitle(), Q2_UNIT_WIDTH);
        if (!q2Lock) {
            MyNumberField q2NumberField = new MyNumberField();
            q2NumberField.setSelectOnFocus(true);
            q2ColumnConfig.setEditor(new CellEditor(q2NumberField));
        } else {
            q2ColumnConfig.setStyle("background-color: #F1F2F4;");
        }
        columnConfigs.add(q2ColumnConfig);

        ColumnConfig q3ColumnConfig = new ColumnConfig(Q3_UNIT_COLUMN, getConstant().q3ColumnTitle(), Q3_UNIT_WIDTH);
        if (!q3Lock) {
            MyNumberField q3NumberField = new MyNumberField();
            q3NumberField.setSelectOnFocus(true);
            q3ColumnConfig.setEditor(new CellEditor(q3NumberField));
        } else {
            q3ColumnConfig.setStyle("background-color: #F1F2F4;");
        }
        columnConfigs.add(q3ColumnConfig);

        ColumnConfig q4ColumnConfig = new ColumnConfig(Q4_UNIT_COLUMN, getConstant().q4ColumnTitle(), Q4_UNIT_WIDTH);
        q4ColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                                 ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
                TaskDetailNam taskDetailNam = model.getBean();
                if (taskDetailNam != null) {
                    Double q1 = taskDetailNam.getQ1();
                    Double q2 = taskDetailNam.getQ2();
                    Double q3 = taskDetailNam.getQ3();
                    if (q1 == null || q2 == null || q3 == null) {
                        taskDetailNam.setQ4(null);
                        return null;
                    }
                    Double total = taskDetailNam.getRealValue();
                    if (total != null && total > 0) {
                        Double q4 = total - q1 - q2 - q3;
                        if (q4 > 0) {
                            taskDetailNam.setQ4(q4);
                        } else {
                            taskDetailNam.setQ4(null);
                        }
                        return getDecimalFormat().format(q4);
                    }
                }
                return null;
            }
        });
        q4ColumnConfig.setStyle("background-color: #F1F2F4;");
        columnConfigs.add(q4ColumnConfig);

        for (int i = 1; i < columnConfigs.size(); i++) {
            columnConfigs.get(i).setNumberFormat(getDecimalFormat());
        }
        return columnConfigs;
    }

    @Override
    protected ColumnModel createSubTaskModel() {
        ColumnModel columnModel = super.createSubTaskModel();
        AggregationRowConfig<BeanModel> sumRow = new AggregationRowConfig<BeanModel>();
        sumRow.setRenderer(BRANCH_NAME_COLUMN, new AggregationRenderer<BeanModel>() {
            @Override
            public Object render(Number value, int colIndex, Grid<BeanModel> beanModelGrid,
                                 ListStore<BeanModel> beanModelListStore) {
                Double sum = 0d;
                for (BeanModel beanModel : beanModelListStore.getModels()) {
                    TaskDetailNam taskDetailNam = beanModel.getBean();
                    if (taskDetailNam.getQ1() != null) {
                        sum += taskDetailNam.getQ1();
                    }
                    if (taskDetailNam.getQ2() != null) {
                        sum += taskDetailNam.getQ2();
                    }
                    if (taskDetailNam.getQ3() != null) {
                        sum += taskDetailNam.getQ3();
                    }
                    if (taskDetailNam.getQ4() != null) {
                        sum += taskDetailNam.getQ4();
                    }
                }
                if (sum == 0) {
                    return StringUtils.substitute(getConstant().sumKLTitle(), StringUtils.EMPTY);
                }
                return StringUtils.substitute(getConstant().sumKLTitle(), getFormat("###,###.##").format(sum));
            }
        });
        sumRow.setSummaryType(Q1_UNIT_COLUMN, SummaryType.SUM);
        sumRow.setRenderer(Q1_UNIT_COLUMN, new DefaultAggregationRenderer());
        sumRow.setSummaryType(Q2_UNIT_COLUMN, SummaryType.SUM);
        sumRow.setRenderer(Q2_UNIT_COLUMN, new DefaultAggregationRenderer());
        sumRow.setSummaryType(Q3_UNIT_COLUMN, SummaryType.SUM);
        sumRow.setRenderer(Q3_UNIT_COLUMN, new DefaultAggregationRenderer());
        sumRow.setSummaryType(Q4_UNIT_COLUMN, SummaryType.SUM);
        sumRow.setRenderer(Q4_UNIT_COLUMN, new DefaultAggregationRenderer());
        sumRow.setSummaryType(REAL_VALUE_COLUMN, SummaryType.SUM);
        sumRow.setRenderer(REAL_VALUE_COLUMN, new DefaultAggregationRenderer());

        columnModel.addAggregationRow(sumRow);
        return columnModel;
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
