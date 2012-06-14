/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.TaskDetailKDKConstant;
import com.qlkh.client.client.module.content.view.security.TaskDetailKDKSecurity;
import com.qlkh.client.client.module.content.view.share.AbstractTaskDetailView;
import com.qlkh.client.client.widget.DefaultAggregationRenderer;
import com.qlkh.client.client.widget.MyNumberField;
import com.qlkh.core.client.model.TaskDetailKDK;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TaskDetailView.
 *
 * @author Nguyen Duc Dung
 * @since 1/1/12, 3:36 PM
 */
@ViewSecurity(configuratorClass = TaskDetailKDKSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = TaskDetailKDKConstant.class)
public class TaskDetailKDKView extends AbstractTaskDetailView<TaskDetailKDKConstant> {

    public static final String BRANCH_NAME_COLUMN = "branch.name";
    public static int BRANCH_NAME_WIDTH = 150;
    public static final String Q1_UNIT_COLUMN = "q1";
    public static final int Q1_UNIT_WIDTH = 70;
    public static final String Q2_UNIT_COLUMN = "q2";
    public static final int Q2_UNIT_WIDTH = 70;
    public static final String Q3_UNIT_COLUMN = "q3";
    public static final int Q3_UNIT_WIDTH = 70;
    public static final String Q4_UNIT_COLUMN = "q4";
    public static final int Q4_UNIT_WIDTH = 70;

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
                    TaskDetailKDK taskDetailKDK = beanModel.getBean();
                    if (taskDetailKDK.getQ1() != null) {
                        sum += taskDetailKDK.getQ1();
                    }
                    if (taskDetailKDK.getQ2() != null) {
                        sum += taskDetailKDK.getQ2();
                    }
                    if (taskDetailKDK.getQ3() != null) {
                        sum += taskDetailKDK.getQ3();
                    }
                    if (taskDetailKDK.getQ4() != null) {
                        sum += taskDetailKDK.getQ4();
                    }
                }
                if (sum == 0) {
                    sum = null;
                }
                return StringUtils.substitute(getConstant().sumKLTitle(), sum);
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

        columnModel.addAggregationRow(sumRow);
        return columnModel;
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
