package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialLimitConstant;
import com.qlkh.client.client.module.content.view.security.MaterialLimitSecurity;
import com.qlkh.client.client.module.content.view.share.AbstractTaskDetailView;
import com.qlkh.client.client.widget.MyNumberField;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.annotation.View;
import com.smvp4g.mvp.client.widget.TextField;

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

    public static final String MATERIAL_LIMIT_CODE_COLUMN = "material.code";
    public static final int MATERIAL_LIMIT_CODE_WIDTH = 70;
    public static final String MATERIAL_LIMIT_NAME_COLUMN = "material.name";
    public static final int MATERIAL_LIMIT_NAME_WIDTH = 200;
    public static final String MATERIAL_LIMIT_UNIT_COLUMN = "material.unit";
    public static final int MATERIAL_LIMIT_UNIT_WIDTH = 50;
    public static final String MATERIAL_LIMIT_PRICE_COLUMN = "material.price";
    public static final int MATERIAL_LIMIT_PRICE_WIDTH = 50;
    public static final String MATERIAL_LIMIT_QUANTITY_COLUMN = "quantity";
    public static final int MATERIAL_LIMIT_QUANTITY_WIDTH = 70;

    public static final String MATERIAL_CODE_COLUMN = "code";
    public static final int MATERIAL_CODE_WIDTH = 100;
    public static final String MATERIAL_NAME_COLUMN = "name";
    public static final int MATERIAL_NAME_WIDTH = 200;
    public static final String MATERIAL_UNIT_COLUMN = "unit";
    public static final int MATERIAL_UNIT_WIDTH = 100;
    public static final String MATERIAL_PRICE_COLUMN = "price";
    public static final int MATERIAL_PRICE_WIDTH = 100;

    @I18nField
    CheckBox cbShowTaskHasLimit = new CheckBox();

    @I18nField
    CheckBox cbShowTaskHasNoLimit = new CheckBox();

    @I18nField
    Button btnMaterialTaskAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnDeleteTaskMaterial = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnMaterialAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnMaterialEditOk = new Button();

    @I18nField
    Button btnMaterialEditCancel = new Button();

    @I18nField(emptyText = true)
    TextField<String> txtMaterialSearch = new TextField<String>();

    private ContentPanel materialPanel = new ContentPanel();
    private PagingToolBar materialPagingToolBar = new PagingToolBar(100);


    private Grid<BeanModel> materialGrid;

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
        subToolBar.add(btnMaterialTaskAdd);
        subToolBar.add(new SeparatorToolItem());
        subToolBar.add(getBtnSubTaskSave());
        subToolBar.add(new SeparatorToolItem());
        subToolBar.add(btnDeleteTaskMaterial);
        subToolBar.add(new SeparatorToolItem());
        subToolBar.add(getBtnSubTaskRefresh());
        return subToolBar;
    }

    @Override
    protected List<ColumnConfig> createSubTaskColumnConfigs() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig materialCodeColumn = new ColumnConfig(MATERIAL_LIMIT_CODE_COLUMN,
                getConstant().materialCodeColumnTitle(), MATERIAL_LIMIT_CODE_WIDTH);
        columnConfigs.add(materialCodeColumn);

        ColumnConfig materialNameColumn = new ColumnConfig(MATERIAL_LIMIT_NAME_COLUMN, getConstant().materialNameColumnTitle(),
                MATERIAL_LIMIT_NAME_WIDTH);
        columnConfigs.add(materialNameColumn);

        ColumnConfig materialUnitColumn = new ColumnConfig(MATERIAL_LIMIT_UNIT_COLUMN,
                getConstant().materialUnitColumnTitle(), MATERIAL_LIMIT_UNIT_WIDTH);
        columnConfigs.add(materialUnitColumn);

        ColumnConfig materialPriceColumn = new ColumnConfig(MATERIAL_LIMIT_PRICE_COLUMN,
                getConstant().materialPriceColumnTitle(), MATERIAL_LIMIT_PRICE_WIDTH);
        columnConfigs.add(materialPriceColumn);

        ColumnConfig quantityColumnConfig = new ColumnConfig(MATERIAL_LIMIT_QUANTITY_COLUMN,
                getConstant().materialQuantityColumnTitle(), MATERIAL_LIMIT_QUANTITY_WIDTH);
        MyNumberField quantityNumberField = new MyNumberField();
        quantityNumberField.setSelectOnFocus(true);
        quantityColumnConfig.setEditor(new CellEditor(quantityNumberField));
        columnConfigs.add(quantityColumnConfig);

        for (int i = 1; i < columnConfigs.size(); i++) {
            columnConfigs.get(i).setNumberFormat(NumberFormat.getDecimalFormat());
        }
        return columnConfigs;
    }

    public com.extjs.gxt.ui.client.widget.Window createMaterialEditWindow(ListStore<BeanModel> childGridStore) {
        com.extjs.gxt.ui.client.widget.Window window = new com.extjs.gxt.ui.client.widget.Window();
        if (!materialPanel.isRendered()) {
            materialPanel.setHeaderVisible(false);
            materialPanel.setBodyBorder(false);
            materialPanel.setBorders(false);

            ColumnModel childColumnModel = new ColumnModel(createMaterialColumnConfigs());
            materialGrid = new Grid<BeanModel>(childGridStore, childColumnModel);
            materialGrid.setBorders(true);
            materialGrid.setHeight(400);
            materialGrid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);

            ToolBar toolBar = new ToolBar();
            toolBar.add(txtMaterialSearch);
            toolBar.add(new SeparatorToolItem());
            toolBar.add(btnMaterialAdd);

            materialPanel.setTopComponent(toolBar);
            materialPanel.setBottomComponent(materialPagingToolBar);
            materialPanel.add(materialGrid);
        }

        window.add(materialPanel);
        window.addButton(btnMaterialEditOk);
        window.addButton(btnMaterialEditCancel);
        window.setSize(600, 400);
        window.setAutoHeight(true);
        window.setResizable(false);
        window.setModal(true);
        window.setHeading(getConstant().materialEditPanel());
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                getSubTaskDetailGird().focus();
            }
        });
        return window;
    }


    private List<ColumnConfig> createMaterialColumnConfigs() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig sttColumnConfig = new ColumnConfig(STT_COLUMN, getConstant().sttColumnTitle(), STT_COLUMN_WIDTH);
        sttColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                                 ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
                if (model.get(STT_COLUMN) == null) {
                    model.set(STT_COLUMN, rowIndex + 1);
                }
                return new Text(String.valueOf(model.get(STT_COLUMN)));
            }
        });
        columnConfigs.add(sttColumnConfig);

        ColumnConfig codeColumnConfig = new ColumnConfig(MATERIAL_CODE_COLUMN, getConstant().materialCodeColumnTitle(), MATERIAL_CODE_WIDTH);
        columnConfigs.add(codeColumnConfig);

        ColumnConfig nameColumnConfig = new ColumnConfig(MATERIAL_NAME_COLUMN, getConstant().materialNameColumnTitle(),
                MATERIAL_NAME_WIDTH);
        columnConfigs.add(nameColumnConfig);

        ColumnConfig unitColumnConfig = new ColumnConfig(MATERIAL_UNIT_COLUMN, getConstant().materialUnitColumnTitle(),
                MATERIAL_UNIT_WIDTH);
        columnConfigs.add(unitColumnConfig);

        ColumnConfig priceColumnConfig = new ColumnConfig(MATERIAL_PRICE_COLUMN, getConstant().materialPriceColumnTitle(),
                MATERIAL_PRICE_WIDTH);
        columnConfigs.add(priceColumnConfig);

        return columnConfigs;
    }

    public CheckBox getCbShowTaskHasNoLimit() {
        return cbShowTaskHasNoLimit;
    }

    public CheckBox getCbShowTaskHasLimit() {
        return cbShowTaskHasLimit;
    }

    public Button getBtnMaterialAdd() {
        return btnMaterialAdd;
    }

    public Button getBtnMaterialTaskAdd() {
        return btnMaterialTaskAdd;
    }

    public PagingToolBar getMaterialPagingToolBar() {
        return materialPagingToolBar;
    }

    public Grid<BeanModel> getMaterialGrid() {
        return materialGrid;
    }

    public Button getBtnDeleteTaskMaterial() {
        return btnDeleteTaskMaterial;
    }

    public Button getBtnMaterialEditCancel() {
        return btnMaterialEditCancel;
    }

    public Button getBtnMaterialEditOk() {
        return btnMaterialEditOk;
    }

    public TextField<String> getTxtMaterialSearch() {
        return txtMaterialSearch;
    }
}
