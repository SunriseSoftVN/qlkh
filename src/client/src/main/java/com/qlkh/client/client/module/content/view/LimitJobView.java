package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.google.gwt.i18n.client.NumberFormat;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.LimitJobConstant;
import com.qlkh.client.client.module.content.view.security.LimitJobSecurity;
import com.qlkh.client.client.module.content.view.share.AbstractTaskDetailView;
import com.qlkh.client.client.widget.DefaultAggregationRenderer;
import com.qlkh.client.client.widget.MyNumberField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class LimitJobView.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 10:10 AM
 */
@ViewSecurity(configuratorClass = LimitJobSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = LimitJobConstant.class)
public class LimitJobView extends AbstractTaskDetailView<LimitJobConstant> {

    public static final String MATERIAL_CODE_COLUMN = "material.code";
    public static final int MATERIAL_CODE_WIDTH = 100;
    public static final String MATERIAL_NAME_COLUMN = "material.name";
    public static final int MATERIAL_NAME_WIDTH = 200;
    public static final String MATERIAL_UNIT_COLUMN = "material.unit";
    public static final int MATERIAL_UNIT_WIDTH = 70;
    public static final String MATERIAL_QUANTITY_COLUMN = "material.quantity";
    public static final int MATERIAL_QUANTITY_WIDTH = 70;

    @Override
    protected List<ColumnConfig> createSubTaskColumnConfigs() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig materialCodeColumn = new ColumnConfig(MATERIAL_CODE_COLUMN,
                getConstant().materialCodeColumnTitle(), MATERIAL_CODE_WIDTH);
        columnConfigs.add(materialCodeColumn);

        ColumnConfig materialNameColumn = new ColumnConfig(MATERIAL_NAME_COLUMN, getConstant().materialNameColumnTitle(),
                MATERIAL_NAME_WIDTH);
        columnConfigs.add(materialNameColumn);

        ColumnConfig increaseValueColumnConfig = new ColumnConfig(MATERIAL_UNIT_COLUMN,
                getConstant().materialUnitColumnTitle(), MATERIAL_UNIT_WIDTH);
        columnConfigs.add(increaseValueColumnConfig);

        ColumnConfig decreaseValueColumnConfig = new ColumnConfig(MATERIAL_QUANTITY_COLUMN,
                getConstant().materialQuantityColumnTitle(), MATERIAL_QUANTITY_WIDTH);
        columnConfigs.add(decreaseValueColumnConfig);

        for (int i = 1; i < columnConfigs.size(); i++) {
            columnConfigs.get(i).setNumberFormat(NumberFormat.getDecimalFormat());
        }
        return columnConfigs;
    }
}
