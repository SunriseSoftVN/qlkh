package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialLimitConstant;
import com.qlkh.client.client.module.content.view.security.MaterialLimitSecurity;
import com.qlkh.client.client.module.content.view.share.AbstractTaskDetailView;
import com.qlkh.client.client.widget.MyFormPanel;
import com.qlkh.client.client.widget.MyNumberField;
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
@ViewSecurity(configuratorClass = MaterialLimitSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = MaterialLimitConstant.class)
public class MaterialLimitView extends AbstractTaskDetailView<MaterialLimitConstant> {

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

    @I18nField
    Button btnSubTaskAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @Override
    protected ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();
        toolBar.add(getTxtSearch());
        toolBar.add(new SeparatorToolItem());
        cbShowTaskHasLimit.setBoxLabel(getConstant().cbShowTaskHasLimit());
        cbShowTaskHasNoLimit.setBoxLabel(getConstant().cbShowTaskHasNoLimit());
        toolBar.add(cbShowTaskHasLimit);
        toolBar.add(cbShowTaskHasNoLimit);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(getBtnRefresh());
        return toolBar;
    }

    @Override
    protected ToolBar createSubToolBar() {
        ToolBar subToolBar = new ToolBar();
        subToolBar.add(btnSubTaskAdd);
        subToolBar.add(new SeparatorToolItem());
        subToolBar.add(getBtnSubTaskSave());
        subToolBar.add(new SeparatorToolItem());
        subToolBar.add(getBtnSubTaskRefresh());
        return subToolBar;
    }

    @Override
    protected List<ColumnConfig> createSubTaskColumnConfigs() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig materialCodeColumn = new ColumnConfig(MATERIAL_CODE_COLUMN,
                getConstant().materialCodeColumnTitle(), MATERIAL_CODE_WIDTH);

        TextField codeTextField = new TextField();
        codeTextField.setSelectOnFocus(true);
        materialCodeColumn.setEditor(new CellEditor(codeTextField));
        columnConfigs.add(materialCodeColumn);

        ColumnConfig materialNameColumn = new ColumnConfig(MATERIAL_NAME_COLUMN, getConstant().materialNameColumnTitle(),
                MATERIAL_NAME_WIDTH);
        TextField nameTextField = new TextField();
        nameTextField.setSelectOnFocus(true);
        materialNameColumn.setEditor(new CellEditor(nameTextField));
        columnConfigs.add(materialNameColumn);

        ColumnConfig materialUnitColumn = new ColumnConfig(MATERIAL_UNIT_COLUMN,
                getConstant().materialUnitColumnTitle(), MATERIAL_UNIT_WIDTH);
        TextField unitTextField = new TextField();
        unitTextField.setSelectOnFocus(true);
        materialUnitColumn.setEditor(new CellEditor(unitTextField));
        columnConfigs.add(materialUnitColumn);

        ColumnConfig decreaseValueColumnConfig = new ColumnConfig(MATERIAL_QUANTITY_COLUMN,
                getConstant().materialQuantityColumnTitle(), MATERIAL_QUANTITY_WIDTH);
        MyNumberField quantityNumberField = new MyNumberField();
        quantityNumberField.setSelectOnFocus(true);
        decreaseValueColumnConfig.setEditor(new CellEditor(quantityNumberField));
        columnConfigs.add(decreaseValueColumnConfig);

        for (int i = 1; i < columnConfigs.size(); i++) {
            columnConfigs.get(i).setNumberFormat(NumberFormat.getDecimalFormat());
        }
        return columnConfigs;
    }

    private MyFormPanel materialEditPanel = new MyFormPanel();

    @I18nField
    Button btnMaterialEditOk = new Button();

    @I18nField
    Button btnMaterialEditCancel = new Button();

    public com.extjs.gxt.ui.client.widget.Window createMaterialEditWindow() {
        com.extjs.gxt.ui.client.widget.Window window = new com.extjs.gxt.ui.client.widget.Window();
        if (!materialEditPanel.isRendered()) {
            materialEditPanel.setHeaderVisible(false);
            materialEditPanel.setBodyBorder(false);
            materialEditPanel.setBorders(false);
            materialEditPanel.setLabelWidth(150);
        }

        window.add(materialEditPanel);
        window.addButton(btnMaterialEditOk);
        window.addButton(btnMaterialEditCancel);
        window.setSize(400, 250);
        window.setAutoHeight(true);
        window.setResizable(false);
        window.setModal(true);
        window.setHeading("dung ne");
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                materialEditPanel.clear();
                getSubTaskDetailGird().focus();
            }
        });
        return window;
    }

    public CheckBox getCbShowTaskHasNoLimit() {
        return cbShowTaskHasNoLimit;
    }

    public CheckBox getCbShowTaskHasLimit() {
        return cbShowTaskHasLimit;
    }

    public Button getBtnSubTaskAdd() {
        return btnSubTaskAdd;
    }
}
