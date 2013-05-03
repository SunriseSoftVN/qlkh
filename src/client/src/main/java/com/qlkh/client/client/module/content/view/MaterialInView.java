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
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialInViewConstant;
import com.qlkh.client.client.module.content.view.security.MaterialInSecurity;
import com.qlkh.client.client.widget.MyFitLayout;
import com.qlkh.client.client.widget.MyFormPanel;
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
 * The Class MaterialInView.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 4:05 AM
 */
@ViewSecurity(configuratorClass = MaterialInSecurity.class)
@View(constantsClass = MaterialInViewConstant.class, parentDomId = DomIdConstant.CONTENT_PANEL)
public class MaterialInView extends AbstractView<MaterialInViewConstant> {

    public static final int LIST_SIZE = 200;

    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 40;
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
    Button btnEditOk = new Button();

    @I18nField
    Button btnEditCancel = new Button();

    @I18nField
    Button btnCopy = new Button(null, IconHelper.createPath("assets/images/icons/fam/database_save.png"));

    @I18nField(emptyText = true)
    TextField<String> txtNameSearch = new TextField<String>();

    @I18nField(emptyText = true)
    TextField<String> txtCodeSearch = new TextField<String>();

    @I18nField(emptyText = true)
    TextField<String> txtMaterialSearch = new TextField<String>();

    ComboBox<BeanModel> cbStation = new ComboBox<BeanModel>();
    SimpleComboBox<QuarterEnum> cbQuarter = new SimpleComboBox<QuarterEnum>();
    SimpleComboBox<Integer> cbYear = new SimpleComboBox<Integer>();

    @I18nField
    ComboBox<BeanModel> cbGroup = new ComboBox<BeanModel>();

    @I18nField
    ComboBox<BeanModel> cbPerson = new ComboBox<BeanModel>();

    @I18nField
    MyNumberField txtTotal = new MyNumberField();

    @I18nField
    MyNumberField txtWeight = new MyNumberField();

    @I18nField
    TextField<String> txtCode = new TextField<String>();

    @I18nField
    DateField exportDate = new DateField();

    private ContentPanel gridPanel = new ContentPanel();
    private MyFormPanel editPanel = new MyFormPanel();
    private PagingToolBar materialPagingToolBar = new PagingToolBar(100);
    private Grid<BeanModel> materialGrid;

    private PagingToolBar pagingToolBar;
    private Grid<BeanModel> gird;
    private ContentPanel contentPanel = new ContentPanel();

    public void createGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        ColumnModel cm = new ColumnModel(createColumnConfig(selectionModel));
        gird = new Grid<BeanModel>(listStore, cm);
        gird.setBorders(true);
        gird.setLoadMask(true);
        gird.setStripeRows(true);
        gird.setSelectionModel(selectionModel);
        gird.addPlugin(selectionModel);
        gird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        gird.getStore().getLoader().setSortField("id");
        gird.addListener(Events.OnKeyDown, new KeyListener() {
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

        pagingToolBar = new PagingToolBar(LIST_SIZE);
        ToolBar toolBar = new ToolBar();
        txtCodeSearch.setWidth(100);
        toolBar.add(txtCodeSearch);
        toolBar.add(new SeparatorToolItem());
        txtNameSearch.setWidth(100);
        toolBar.add(txtNameSearch);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(cbStation);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(cbQuarter);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(cbYear);
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

        if (!cbStation.isRendered()) {
            cbStation.setDisplayField("name");
            cbStation.setWidth(150);
            cbStation.setEditable(false);
            cbStation.setSelectOnFocus(true);
            cbStation.setTriggerAction(ComboBox.TriggerAction.ALL);
            cbStation.setForceSelection(true);
        }

        if (!cbQuarter.isRendered()) {
            cbQuarter.setWidth(50);
            cbQuarter.setEditable(false);
            cbQuarter.setSelectOnFocus(true);
            cbQuarter.add(Arrays.asList(QuarterEnum.values()));
            cbQuarter.setTriggerAction(ComboBox.TriggerAction.ALL);
            cbQuarter.setForceSelection(true);
        }

        if (!cbYear.isRendered()) {
            cbYear.setWidth(50);
            cbYear.setEditable(false);
            cbYear.setSelectOnFocus(true);
            for (int i = 2012; i < 2100; i++) {
                cbYear.add(i);
            }
            cbYear.setTriggerAction(ComboBox.TriggerAction.ALL);
            cbYear.setForceSelection(true);
        }

        contentPanel.setLayout(new MyFitLayout());
        contentPanel.add(gird);
        contentPanel.setTopComponent(toolBar);
        contentPanel.setBottomComponent(pagingToolBar);
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

    private List<ColumnConfig> createColumnConfig(CheckBoxSelectionModel<BeanModel> selectionModel) {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
        columnConfigs.add(selectionModel.getColumn());
        ColumnConfig sttColumnConfig = new ColumnConfig("stt", getConstant().sttColumnTitle(), 40);
        sttColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                                 ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
                if (model.get("stt") == null) {
                    model.set("stt", rowIndex + 1);
                }
                return new Text(String.valueOf(model.get("stt")));
            }
        });
        columnConfigs.add(sttColumnConfig);

        ColumnConfig materialCodeColumnConfig = new ColumnConfig("material.code", getConstant().materialCodeColumnTitle(), 50);
        columnConfigs.add(materialCodeColumnConfig);

        ColumnConfig materialNameColumnConfig = new ColumnConfig("material.name", getConstant().nameColumnTitle(), 170);
        columnConfigs.add(materialNameColumnConfig);

        ColumnConfig materialUnitColumnConfig = new ColumnConfig("material.unit", getConstant().unitColumnTitle(), 50);
        columnConfigs.add(materialUnitColumnConfig);

        ColumnConfig totalColumnConfig = new ColumnConfig("total", getConstant().totalColumnTitle(), 70);
        totalColumnConfig.setNumberFormat(NumberFormat.getFormat("###,###.##"));
        columnConfigs.add(totalColumnConfig);

        ColumnConfig reasonColumnConfig = new ColumnConfig("materialGroup.name", getConstant().reasonColumnTitle(), 150);
        columnConfigs.add(reasonColumnConfig);

        ColumnConfig groupCodeColumnConfig = new ColumnConfig("materialGroup.code", getConstant().groupCodeColumnTitle(), 70);
        columnConfigs.add(groupCodeColumnConfig);

        ColumnConfig dateColumnConfig = new ColumnConfig("exportDate", getConstant().dateColumnTitle(), 80);
        dateColumnConfig.setDateTimeFormat(DateTimeFormat.getFormat("dd-MM-yyyy"));
        columnConfigs.add(dateColumnConfig);

        ColumnConfig personColumnConfig = new ColumnConfig("materialPerson.personName", getConstant().personColumnTitle(), 100);
        columnConfigs.add(personColumnConfig);

        ColumnConfig codeColumnConfig = new ColumnConfig("code", getConstant().codeColumnTitle(), 100);
        columnConfigs.add(codeColumnConfig);

        ColumnConfig weightColumnConfig = new ColumnConfig("weight", getConstant().weightColumnTitle(), 70);
        columnConfigs.add(weightColumnConfig);

        ColumnConfig remainColumnConfig = new ColumnConfig("remain", getConstant().remainColumnTitle(), 80);
        columnConfigs.add(remainColumnConfig);

        return columnConfigs;
    }

    public com.extjs.gxt.ui.client.widget.Window createEditWindow(ListStore<BeanModel> childGridStore) {
        com.extjs.gxt.ui.client.widget.Window window = new com.extjs.gxt.ui.client.widget.Window();
        if (!gridPanel.isRendered() && childGridStore != null) {
            gridPanel.setHeaderVisible(false);
            gridPanel.setBodyBorder(false);
            gridPanel.setBorders(false);

            ColumnModel childColumnModel = new ColumnModel(createMaterialColumnConfigs());

            materialGrid = new Grid<BeanModel>(childGridStore, childColumnModel);
            materialGrid.setBorders(true);
            materialGrid.setHeight(200);

            ToolBar toolBar = new ToolBar();
            toolBar.add(txtMaterialSearch);

            gridPanel.setTopComponent(toolBar);
            gridPanel.setBottomComponent(materialPagingToolBar);
            gridPanel.add(materialGrid);
        }


        if (!editPanel.isRendered()) {
            editPanel.setHeaderVisible(false);
            editPanel.setBorders(false);
            editPanel.setHeight(170);
            editPanel.setLabelWidth(100);

            cbGroup.setDisplayField("code");
            cbPerson.setDisplayField("personName");

            cbGroup.setAllowBlank(false);
            cbGroup.setEditable(false);
            cbGroup.setSelectOnFocus(true);
            cbGroup.setTriggerAction(ComboBox.TriggerAction.ALL);
            cbGroup.setForceSelection(true);

            cbPerson.setAllowBlank(false);
            cbPerson.setEditable(false);
            cbPerson.setSelectOnFocus(true);
            cbPerson.setTriggerAction(ComboBox.TriggerAction.ALL);
            cbPerson.setForceSelection(true);

            txtCode.setAllowBlank(false);
            txtWeight.setAllowBlank(false);
            txtTotal.setReadOnly(true);
            txtTotal.setEnabled(false);

            exportDate.setAllowBlank(false);
            exportDate.setEditable(false);
            exportDate.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd-MM-yyyy"));

            editPanel.add(txtCode);
            editPanel.add(cbGroup);
            editPanel.add(cbPerson);
            editPanel.add(txtTotal);
            editPanel.add(txtWeight);
            editPanel.add(exportDate);
        }

        if (childGridStore != null) {
            window.add(gridPanel);
        }
        window.add(editPanel);

        window.addButton(btnEditOk);
        window.addButton(btnEditCancel);
        window.setSize(600, 400);
        window.setAutoHeight(true);
        window.setResizable(false);
        window.setModal(true);
        window.setHeading(getConstant().editPanel());
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                gird.focus();
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

        ColumnConfig codeColumnConfig = new ColumnConfig(MATERIAL_CODE_COLUMN, getConstant().codeColumnTitle(), MATERIAL_CODE_WIDTH);
        columnConfigs.add(codeColumnConfig);

        ColumnConfig nameColumnConfig = new ColumnConfig(MATERIAL_NAME_COLUMN, getConstant().nameColumnTitle(),
                MATERIAL_NAME_WIDTH);
        columnConfigs.add(nameColumnConfig);

        ColumnConfig unitColumnConfig = new ColumnConfig(MATERIAL_UNIT_COLUMN, getConstant().unitColumnTitle(),
                MATERIAL_UNIT_WIDTH);
        columnConfigs.add(unitColumnConfig);

        ColumnConfig noteColumnConfig = new ColumnConfig(MATERIAL_NOTE_COLUMN, getConstant().nameColumnTitle(),
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

    public TextField<String> getTxtNameSearch() {
        return txtNameSearch;
    }

    public TextField<String> getTxtCodeSearch() {
        return txtCodeSearch;
    }

    public ContentPanel getGridPanel() {
        return gridPanel;
    }

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }

    public Grid<BeanModel> getGird() {
        return gird;
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public ComboBox<BeanModel> getCbStation() {
        return cbStation;
    }

    public SimpleComboBox<QuarterEnum> getCbQuarter() {
        return cbQuarter;
    }

    public SimpleComboBox<Integer> getCbYear() {
        return cbYear;
    }

    public Button getBtnEditOk() {
        return btnEditOk;
    }

    public Button getBtnEditCancel() {
        return btnEditCancel;
    }

    public TextField<String> getTxtMaterialSearch() {
        return txtMaterialSearch;
    }

    public PagingToolBar getMaterialPagingToolBar() {
        return materialPagingToolBar;
    }

    public Grid<BeanModel> getMaterialGrid() {
        return materialGrid;
    }

    public ComboBox<BeanModel> getCbGroup() {
        return cbGroup;
    }

    public ComboBox<BeanModel> getCbPerson() {
        return cbPerson;
    }

    public MyNumberField getTxtWeight() {
        return txtWeight;
    }

    public MyFormPanel getEditPanel() {
        return editPanel;
    }

    public MyNumberField getTxtTotal() {
        return txtTotal;
    }

    public TextField<String> getTxtCode() {
        return txtCode;
    }

    public void setCbPerson(ComboBox<BeanModel> cbPerson) {
        this.cbPerson = cbPerson;
    }

    public Button getBtnCopy() {
        return btnCopy;
    }

    public DateField getExportDate() {
        return exportDate;
    }

    public void resetEditPanel() {
        editPanel.clear();
        cbGroup.clear();
        cbPerson.clear();
        txtTotal.clear();
        txtWeight.clear();
        txtCode.clear();
        exportDate.clear();
    }
}
