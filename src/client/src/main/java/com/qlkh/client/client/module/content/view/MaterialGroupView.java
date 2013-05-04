package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialGroupConstant;
import com.qlkh.client.client.module.content.view.security.MaterialGroupSecurity;
import com.qlkh.client.client.widget.MyFitLayout;
import com.qlkh.client.client.widget.MyFormPanel;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MaterialGroupView.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 3:42 AM
 */
@ViewSecurity(configuratorClass = MaterialGroupSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = MaterialGroupConstant.class)
public class MaterialGroupView extends AbstractView<MaterialGroupConstant> {

    public static final int LIST_SIZE = 200;
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 40;
    public static final String CODE_COLUMN = "code";
    public static final int CODE_WIDTH = 100;
    public static final String CODE_DISPLAY_COLUMN = "codeDisplay";
    public static final int CODE_DISPLAY_WIDTH = 100;
    public static final String NAME_COLUMN = "name";
    public static final int NAME_WIDTH = 200;
    public static final String REGEX_COLUMN = "regex";
    public static final int RANGE_WIDTH = 200;

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
    TextField<String> txtName = new TextField<String>();

    @I18nField
    TextField<String> txtCode = new TextField<String>();

    @I18nField
    TextField<String> txtCodeDisplay = new TextField<String>();

    @I18nField
    TextField<String> txtRegex = new TextField<String>();

    private MyFormPanel editPanel = new MyFormPanel();

    private PagingToolBar pagingToolBar;
    private Grid<BeanModel> materialGroupGird;
    private ContentPanel contentPanel = new ContentPanel();

    /**
     * Create Grid on View.
     */
    public void createGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        ColumnModel cm = new ColumnModel(createColumnConfig(selectionModel));
        materialGroupGird = new Grid<BeanModel>(listStore, cm);
        materialGroupGird.setBorders(true);
        materialGroupGird.setLoadMask(true);
        materialGroupGird.setStripeRows(true);
        materialGroupGird.setSelectionModel(selectionModel);
        materialGroupGird.addPlugin(selectionModel);
        materialGroupGird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        materialGroupGird.getStore().getLoader().setSortField("id");
        materialGroupGird.addListener(Events.OnKeyDown, new KeyListener() {
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
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnEdit);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnRefresh);

        contentPanel.setLayout(new MyFitLayout());
        contentPanel.add(materialGroupGird);
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


        ColumnConfig codeColumnConfig = new ColumnConfig(CODE_COLUMN, getConstant().codeColumnTitle(), CODE_WIDTH);
        columnConfigs.add(codeColumnConfig);

        ColumnConfig codeDisplayColumnConfig = new ColumnConfig(CODE_DISPLAY_COLUMN, getConstant().codeDisplayColumnTitle(),
                CODE_DISPLAY_WIDTH);
        columnConfigs.add(codeDisplayColumnConfig);

        ColumnConfig nameColumnConfig = new ColumnConfig(NAME_COLUMN, getConstant().nameColumnTitle(), NAME_WIDTH);
        columnConfigs.add(nameColumnConfig);

        ColumnConfig rangeColumnConfig = new ColumnConfig(REGEX_COLUMN, getConstant().rangeColumnTitle(),
                RANGE_WIDTH);
        columnConfigs.add(rangeColumnConfig);

        return columnConfigs;
    }

    public com.extjs.gxt.ui.client.widget.Window createEditWindow() {
        com.extjs.gxt.ui.client.widget.Window window = new com.extjs.gxt.ui.client.widget.Window();
        if (!editPanel.isRendered()) {
            editPanel.setHeaderVisible(false);
            editPanel.setBodyBorder(false);
            editPanel.setBorders(false);
            editPanel.setLabelWidth(120);
        }

        if (!txtName.isRendered()) {
            txtName.setAllowBlank(false);
        }

        if (!txtCode.isRendered()) {
            txtCode.setAllowBlank(false);
        }

        if (!txtCodeDisplay.isRendered()) {
            txtCodeDisplay.setAllowBlank(false);
        }

        editPanel.add(txtCode);
        editPanel.add(txtCodeDisplay);
        editPanel.add(txtName);
        editPanel.add(txtRegex);

        window.setFocusWidget(txtName);

        window.add(editPanel);
        window.addButton(btnEditOk);
        window.addButton(btnEditCancel);
        window.setSize(400, 250);
        window.setAutoHeight(true);
        window.setResizable(false);
        window.setModal(true);
        window.setHeading(getConstant().editPanel());
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                editPanel.clear();
                materialGroupGird.focus();
            }
        });
        return window;
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

    public Button getBtnEditOk() {
        return btnEditOk;
    }

    public Button getBtnEditCancel() {
        return btnEditCancel;
    }

    public TextField<String> getTxtName() {
        return txtName;
    }

    public TextField<String> getTxtCode() {
        return txtCode;
    }

    public MyFormPanel getEditPanel() {
        return editPanel;
    }

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }

    public Grid<BeanModel> getMaterialGroupGird() {
        return materialGroupGird;
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public TextField<String> getTxtRegex() {
        return txtRegex;
    }

    public TextField<String> getTxtCodeDisplay() {
        return txtCodeDisplay;
    }
}
