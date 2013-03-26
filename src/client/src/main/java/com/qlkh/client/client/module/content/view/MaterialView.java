package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
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
import com.qlkh.client.client.module.content.view.i18n.MaterialConstant;
import com.qlkh.client.client.module.content.view.security.MaterialSecurity;
import com.qlkh.client.client.widget.MyFitLayout;
import com.qlkh.client.client.widget.MyFormPanel;
import com.qlkh.client.client.widget.MyNumberField;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;
import com.smvp4g.mvp.client.widget.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MaterialView.
 *
 * @author Nguyen Duc Dung
 * @since 3/25/13 1:34 PM
 */

@ViewSecurity(configuratorClass = MaterialSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = MaterialConstant.class)
public class MaterialView extends AbstractView<MaterialConstant> {

    public static final int TASK_LIST_SIZE = 200;
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 40;
    public static final String CODE_COLUMN = "code";
    public static final int CODE_COLUMN_WIDTH = 100;
    public static final String NAME_COLUMN = "name";
    public static final int NAME_COLUMN_WIDTH = 200;
    public static final String UNIT_COLUMN = "unit";
    public static final int UNIT_COLUMN_WIDTH = 100;
    public static final String PRICE_COLUMN = "price";
    public static final int PRICE_COLUMN_WIDTH = 100;
    public static final String NOTE_COLUMN = "note";
    public static final int NOTE_COLUMN_WIDTH = 200;

    @I18nField
    Button btnAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnEdit = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnDelete = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField(emptyText = true)
    TextField<String> txtNameSearch = new TextField<String>();

    @I18nField(emptyText = true)
    TextField<String> txtCodeSearch = new TextField<String>();

    @I18nField
    TextField<String> txtCode = new TextField<String>();

    @I18nField
    TextField<String> txtName = new TextField<String>();

    @I18nField
    TextField<String> txtUnit = new TextField<String>();

    @I18nField
    MyNumberField txtPrice = new MyNumberField();

    @I18nField
    TextArea txtNote = new TextArea();

    @I18nField
    Button btnTaskEditOk = new Button();

    @I18nField
    Button btnTaskEditCancel = new Button();

    private MyFormPanel materialEditPanel = new MyFormPanel();

    private PagingToolBar pagingToolBar;
    private Grid<BeanModel> materialGird;

    private ContentPanel contentPanel = new ContentPanel();

    /**
     * Create Grid on View.
     */
    public void createGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        ColumnModel cm = new ColumnModel(createColumnConfig(selectionModel));
        materialGird = new Grid<BeanModel>(listStore, cm);
        materialGird.setBorders(true);
        materialGird.setLoadMask(true);
        materialGird.setStripeRows(true);
        materialGird.setSelectionModel(selectionModel);
        materialGird.addPlugin(selectionModel);
        materialGird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        materialGird.getStore().getLoader().setSortField(CODE_COLUMN);
        materialGird.addListener(Events.OnKeyDown, new KeyListener() {
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

        pagingToolBar = new PagingToolBar(TASK_LIST_SIZE);
        ToolBar toolBar = new ToolBar();
        txtCodeSearch.setWidth(100);
        toolBar.add(txtCodeSearch);
        toolBar.add(new SeparatorToolItem());
        txtNameSearch.setWidth(150);
        toolBar.add(txtNameSearch);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnEdit);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnRefresh);

        contentPanel.setLayout(new MyFitLayout());
        contentPanel.add(materialGird);
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
        priceColumnConfig.setNumberFormat(NumberFormat.getDecimalFormat());
        columnConfigs.add(priceColumnConfig);

        ColumnConfig noteColumnConfig = new ColumnConfig(NOTE_COLUMN, getConstant().noteColumnTitle(),
                NOTE_COLUMN_WIDTH);
        columnConfigs.add(noteColumnConfig);

        return columnConfigs;
    }

    public com.extjs.gxt.ui.client.widget.Window createMaterialEditWindow() {
        com.extjs.gxt.ui.client.widget.Window window = new com.extjs.gxt.ui.client.widget.Window();
        if (!materialEditPanel.isRendered()) {
            materialEditPanel.setHeaderVisible(false);
            materialEditPanel.setBodyBorder(false);
            materialEditPanel.setBorders(false);
            materialEditPanel.setLabelWidth(120);
        }

        if (!txtCode.isRendered()) {
            txtCode.setAllowBlank(false);
            txtCode.setSelectOnFocus(true);
            txtCode.setMaxLength(11);
        }

        if (!txtName.isRendered()) {
            txtName.setAllowBlank(false);
        }

        if (!txtPrice.isRendered()) {
            txtPrice.setAllowBlank(false);
        }

        if (!txtPrice.isRendered()) {
            txtUnit.setAllowBlank(false);
        }

        materialEditPanel.add(txtCode);
        materialEditPanel.add(txtName);
        materialEditPanel.add(txtPrice);
        materialEditPanel.add(txtUnit);
        materialEditPanel.add(txtNote);

        window.setFocusWidget(txtCode);

        window.add(materialEditPanel);
        window.addButton(btnTaskEditOk);
        window.addButton(btnTaskEditCancel);
        window.setSize(400, 250);
        window.setAutoHeight(true);
        window.setResizable(false);
        window.setModal(true);
        window.setHeading(getConstant().materialEditPanel());
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                materialEditPanel.clear();
                materialGird.focus();
            }
        });
        return window;
    }

    public Grid<BeanModel> getMaterialGird() {
        return materialGird;
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

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }

    public Button getBtnTaskEditOk() {
        return btnTaskEditOk;
    }

    public void setBtnTaskEditOk(Button btnTaskEditOk) {
        this.btnTaskEditOk = btnTaskEditOk;
    }

    public Button getBtnTaskEditCancel() {
        return btnTaskEditCancel;
    }

    public void setBtnTaskEditCancel(Button btnTaskEditCancel) {
        this.btnTaskEditCancel = btnTaskEditCancel;
    }

    public TextField<String> getTxtCode() {
        return txtCode;
    }

    public TextField<String> getTxtName() {
        return txtName;
    }

    public TextField<String> getTxtUnit() {
        return txtUnit;
    }

    public MyNumberField getTxtPrice() {
        return txtPrice;
    }

    public TextArea getTxtNote() {
        return txtNote;
    }

    public MyFormPanel getMaterialEditPanel() {
        return materialEditPanel;
    }
}
