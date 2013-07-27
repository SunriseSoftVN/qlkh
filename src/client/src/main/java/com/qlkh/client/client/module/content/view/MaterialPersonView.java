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
import com.qlkh.client.client.module.content.view.i18n.MaterialPersonConstant;
import com.qlkh.client.client.module.content.view.security.MaterialPersonSecurity;
import com.qlkh.client.client.widget.MyFitLayout;
import com.qlkh.client.client.widget.MyFormPanel;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MaterialPersonView.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 3:21 AM
 */
@ViewSecurity(configuratorClass = MaterialPersonSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = MaterialPersonConstant.class)
public class MaterialPersonView extends AbstractView<MaterialPersonConstant> {

    public static final int LIST_SIZE = 200;
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 40;
    public static final String STATION_COLUMN = "station.name";
    public static final int STATION_WIDTH = 200;
    public static final String GROUP_COLUMN = "group.name";
    public static final int GROUP_WIDTH = 200;
    public static final String PERSON_COLUMN = "personName";
    public static final int PERSON_COLUMN_WIDTH = 200;

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
    ComboBox<BeanModel> cbStation = new ComboBox<BeanModel>();

    @I18nField
    ComboBox<BeanModel> cbGroup = new ComboBox<BeanModel>();

    @I18nField
    TextField<String> txtPersonName = new TextField<String>();

    private MyFormPanel editPanel = new MyFormPanel();

    private PagingToolBar pagingToolBar;
    private Grid<BeanModel> materialPersonGird;
    private ContentPanel contentPanel = new ContentPanel();

    /**
     * Create Grid on View.
     */
    public void createGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        ColumnModel cm = new ColumnModel(createColumnConfig(selectionModel));
        materialPersonGird = new Grid<BeanModel>(listStore, cm);
        materialPersonGird.setBorders(true);
        materialPersonGird.setLoadMask(true);
        materialPersonGird.setStripeRows(true);
        materialPersonGird.setSelectionModel(selectionModel);
        materialPersonGird.addPlugin(selectionModel);
        materialPersonGird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        materialPersonGird.getStore().getLoader().setSortField("id");
        materialPersonGird.addListener(Events.OnKeyDown, new KeyListener() {
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
        contentPanel.add(materialPersonGird);
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

        ColumnConfig stationColumnConfig = new ColumnConfig(STATION_COLUMN, getConstant().stationColumnTitle(), STATION_WIDTH);
        columnConfigs.add(stationColumnConfig);

        ColumnConfig groupColumnConfig = new ColumnConfig(GROUP_COLUMN, getConstant().groupColumnTitle(), GROUP_WIDTH);
        columnConfigs.add(groupColumnConfig);

        ColumnConfig nameColumnConfig = new ColumnConfig(PERSON_COLUMN, getConstant().personColumnTitle(),
                PERSON_COLUMN_WIDTH);
        columnConfigs.add(nameColumnConfig);

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

        if (!txtPersonName.isRendered()) {
            txtPersonName.setAllowBlank(false);
        }

        if (!cbStation.isRendered()) {
            cbStation.setDisplayField("name");
            cbStation.setEditable(false);
            cbStation.setAllowBlank(false);
            cbStation.setSelectOnFocus(true);
            cbStation.setTriggerAction(ComboBox.TriggerAction.ALL);
            cbStation.setForceSelection(true);
        }

        if (!cbGroup.isRendered()) {
            cbGroup.setDisplayField("name");
            cbGroup.setEditable(false);
            cbGroup.setSelectOnFocus(true);
            cbGroup.setTriggerAction(ComboBox.TriggerAction.ALL);
            cbGroup.setForceSelection(true);
        }

        editPanel.add(cbStation);
        editPanel.add(cbGroup);
        editPanel.add(txtPersonName);

        window.setFocusWidget(cbStation);

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
                materialPersonGird.focus();
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

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }

    public Grid<BeanModel> getMaterialPersonGird() {
        return materialPersonGird;
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public Button getBtnEditOk() {
        return btnEditOk;
    }

    public Button getBtnEditCancel() {
        return btnEditCancel;
    }

    public ComboBox<BeanModel> getCbStation() {
        return cbStation;
    }

    public TextField<String> getTxtPersonName() {
        return txtPersonName;
    }

    public MyFormPanel getEditPanel() {
        return editPanel;
    }

    public ComboBox<BeanModel> getCbGroup() {
        return cbGroup;
    }
}
