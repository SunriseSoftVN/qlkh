package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialPriceConstant;
import com.qlkh.client.client.module.content.view.security.MaterialPriceSecurity;
import com.qlkh.client.client.widget.MyFitLayout;
import com.qlkh.client.client.widget.MyNumberField;
import com.qlkh.core.client.constant.QuarterEnum;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;
import com.smvp4g.mvp.client.widget.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Class MaterialPriceView.
 *
 * @author Nguyen Duc Dung
 * @since 4/9/13 7:21 PM
 */
@ViewSecurity(configuratorClass = MaterialPriceSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = MaterialPriceConstant.class)
public class MaterialPriceView extends AbstractView<MaterialPriceConstant> {

    public static final int TASK_LIST_SIZE = 200;
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 40;
    public static final String CODE_COLUMN = "material.code";
    public static final int CODE_COLUMN_WIDTH = 100;
    public static final String NAME_COLUMN = "material.name";
    public static final int NAME_COLUMN_WIDTH = 200;
    public static final String UNIT_COLUMN = "material.unit";
    public static final int UNIT_COLUMN_WIDTH = 100;
    public static final String PRICE_COLUMN = "price";
    public static final int PRICE_COLUMN_WIDTH = 100;
    public static final String NOTE_COLUMN = "material.note";
    public static final int NOTE_COLUMN_WIDTH = 200;

    public static final String MATERIAL_CODE_COLUMN = "code";
    public static final int MATERIAL_CODE_WIDTH = 70;
    public static final String MATERIAL_NAME_COLUMN = "name";
    public static final int MATERIAL_NAME_WIDTH = 150;
    public static final String MATERIAL_UNIT_COLUMN = "unit";
    public static final int MATERIAL_UNIT_WIDTH = 70;
    public static final String MATERIAL_NOTE_COLUMN = "note";
    public static final int MATERIAL_NOTE_WIDTH = 150;

    @I18nField
    Button btnAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnEdit = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnDelete = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField
    Button btnCopy = new Button(null, IconHelper.createPath("assets/images/icons/fam/database_save.png"));

    @I18nField
    Button btnMaterialAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnMaterialEditCancel = new Button();

    @I18nField
    SimpleComboBox<QuarterEnum> cbQuarter = new SimpleComboBox<QuarterEnum>();

    @I18nField(emptyText = true)
    TextField<String> txtSearch = new TextField<String>();

    @I18nField(emptyText = true)
    TextField<String> txtMaterialSearch = new TextField<String>();

    private PagingToolBar materialPricePagingToolBar;
    private Grid<BeanModel> materialPriceGird;
    private ContentPanel contentPanel = new ContentPanel();

    private ContentPanel materialPanel = new ContentPanel();
    private PagingToolBar materialPagingToolBar = new PagingToolBar(100);
    private Grid<BeanModel> materialGrid;

    /**
     * Create Grid on View.
     */
    public void createGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        ColumnModel cm = new ColumnModel(createMaterialPriceColumnConfig(selectionModel));
        materialPriceGird = new EditorGrid<BeanModel>(listStore, cm);
        materialPriceGird.setBorders(true);
        materialPriceGird.setLoadMask(true);
        materialPriceGird.setStripeRows(true);
        materialPriceGird.setSelectionModel(selectionModel);
        materialPriceGird.addPlugin(selectionModel);
        materialPriceGird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        materialPriceGird.getStore().getLoader().setSortField("material.id");
        materialPriceGird.addListener(Events.OnKeyDown, new KeyListener() {
            @Override
            public void handleEvent(ComponentEvent e) {
                if (e.getKeyCode() == 112) {
                    //F1
                    btnAdd.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 113 || e.getKeyCode() == KeyCodes.KEY_ENTER) {
                    //F2 or Enter
                    btnEdit.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 115) {
                    //F4
                    btnRefresh.fireEvent(Events.Select);
                }
            }
        });

        if(!cbQuarter.isRendered()) {
            cbQuarter.add(Arrays.asList(QuarterEnum.values()));
            cbQuarter.setTriggerAction(ComboBox.TriggerAction.ALL);
            cbQuarter.setEditable(false);
            cbQuarter.setSelectOnFocus(true);
        }

        materialPricePagingToolBar = new PagingToolBar(TASK_LIST_SIZE);
        ToolBar toolBar = new ToolBar();
        txtSearch.setWidth(150);
        toolBar.add(txtSearch);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(cbQuarter);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnEdit);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnRefresh);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnCopy);

        contentPanel.setLayout(new MyFitLayout());
        contentPanel.add(materialPriceGird);
        contentPanel.setTopComponent(toolBar);
        contentPanel.setBottomComponent(materialPricePagingToolBar);
        contentPanel.setHeaderVisible(false);
        contentPanel.setHeight(Window.getClientHeight() - 90);
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                contentPanel.layout(true);
            }
        });
        setWidget(contentPanel);
    }

    private List<ColumnConfig> createMaterialPriceColumnConfig(CheckBoxSelectionModel<BeanModel> selectionModel) {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
        columnConfigs.add(selectionModel.getColumn());
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

        ColumnConfig codeColumnConfig = new ColumnConfig(CODE_COLUMN, getConstant().codeColumnTitle(), CODE_COLUMN_WIDTH);
        columnConfigs.add(codeColumnConfig);

        ColumnConfig nameColumnConfig = new ColumnConfig(NAME_COLUMN, getConstant().nameColumnTitle(),
                NAME_COLUMN_WIDTH);
        columnConfigs.add(nameColumnConfig);

        ColumnConfig unitColumnConfig = new ColumnConfig(UNIT_COLUMN, getConstant().unitColumnTitle(),
                UNIT_COLUMN_WIDTH);
        columnConfigs.add(unitColumnConfig);

        ColumnConfig priceColumnConfig = new ColumnConfig(PRICE_COLUMN, getConstant().priceColumnTitle(),
                PRICE_COLUMN_WIDTH);
        MyNumberField priceNumberField = new MyNumberField();
        priceNumberField.setSelectOnFocus(true);
        priceColumnConfig.setEditor(new CellEditor(priceNumberField));
        columnConfigs.add(priceColumnConfig);

        ColumnConfig noteColumnConfig = new ColumnConfig(NOTE_COLUMN, getConstant().noteColumnTitle(),
                NOTE_COLUMN_WIDTH);
        columnConfigs.add(noteColumnConfig);

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

            CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
            ColumnModel childColumnModel = new ColumnModel(createMaterialColumnConfigs(selectionModel));

            materialGrid = new Grid<BeanModel>(childGridStore, childColumnModel);
            materialGrid.setBorders(true);
            materialGrid.setHeight(400);
            materialGrid.setSelectionModel(selectionModel);
            materialGrid.addPlugin(selectionModel);

            ToolBar toolBar = new ToolBar();
            toolBar.add(txtMaterialSearch);

            materialPanel.setTopComponent(toolBar);
            materialPanel.setBottomComponent(materialPagingToolBar);
            materialPanel.add(materialGrid);
        }

        window.add(materialPanel);
        window.addButton(btnMaterialAdd);
        window.addButton(btnMaterialEditCancel);
        window.setSize(600, 400);
        window.setAutoHeight(true);
        window.setResizable(false);
        window.setModal(true);
        window.setHeading(getConstant().materialEditPanel());
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                materialPriceGird.focus();
            }
        });
        return window;
    }


    private List<ColumnConfig> createMaterialColumnConfigs(CheckBoxSelectionModel<BeanModel> selectionModel) {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        columnConfigs.add(selectionModel.getColumn());

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

        ColumnConfig noteColumnConfig = new ColumnConfig(MATERIAL_NOTE_COLUMN, getConstant().materialNoteColumnTitle(),
                MATERIAL_NOTE_WIDTH);
        columnConfigs.add(noteColumnConfig);

        return columnConfigs;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }

    public Button getBtnEdit() {
        return btnEdit;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public Button getBtnRefresh() {
        return btnRefresh;
    }

    public TextField<String> getTxtSearch() {
        return txtSearch;
    }

    public PagingToolBar getMaterialPricePagingToolBar() {
        return materialPricePagingToolBar;
    }

    public Grid<BeanModel> getMaterialPriceGird() {
        return materialPriceGird;
    }

    public SimpleComboBox<QuarterEnum> getCbQuarter() {
        return cbQuarter;
    }

    public Button getBtnMaterialAdd() {
        return btnMaterialAdd;
    }

    public Button getBtnMaterialEditCancel() {
        return btnMaterialEditCancel;
    }

    public TextField<String> getTxtMaterialSearch() {
        return txtMaterialSearch;
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public ContentPanel getMaterialPanel() {
        return materialPanel;
    }

    public PagingToolBar getMaterialPagingToolBar() {
        return materialPagingToolBar;
    }

    public Grid<BeanModel> getMaterialGrid() {
        return materialGrid;
    }

    public Button getBtnCopy() {
        return btnCopy;
    }
}
