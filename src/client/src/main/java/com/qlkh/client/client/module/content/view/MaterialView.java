package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.google.gwt.i18n.client.NumberFormat;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialConstant;
import com.qlkh.client.client.module.content.view.security.MaterialSecurity;
import com.qlkh.client.client.module.content.view.share.AbstractTaskDetailView;
import com.smvp4g.mvp.client.core.i18n.I18nField;
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
@ViewSecurity(configuratorClass = MaterialSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = MaterialConstant.class)
public class MaterialView extends AbstractTaskDetailView<MaterialConstant> {

    public static final String MATERIAL_CODE_COLUMN = "code";
    public static final int MATERIAL_CODE_WIDTH = 100;
    public static final String MATERIAL_NAME_COLUMN = "name";
    public static final int MATERIAL_NAME_WIDTH = 200;
    public static final String MATERIAL_UNIT_COLUMN = "unit";
    public static final int MATERIAL_UNIT_WIDTH = 70;
    public static final String MATERIAL_QUANTITY_COLUMN = "quantity";
    public static final int MATERIAL_QUANTITY_WIDTH = 70;

    @I18nField
    CheckBox cbShowTaskHasLimit = new CheckBox();

    @I18nField
    CheckBox cbShowTaskHasNoLimit = new CheckBox();

    @Override
    public void createTaskGrid(ListStore<BeanModel> listStore) {
        super.createTaskGrid(listStore);

        //TODO: Remove
        cbShowTaskHasLimit.setBoxLabel(getConstant().cbShowTaskHasLimit());
        cbShowTaskHasNoLimit.setBoxLabel(getConstant().cbShowTaskHasNoLimit());

        toolBar.add(cbShowTaskHasLimit);
        toolBar.add(cbShowTaskHasNoLimit);
    }

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
