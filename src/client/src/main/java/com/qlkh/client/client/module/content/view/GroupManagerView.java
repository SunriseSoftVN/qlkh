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
import com.extjs.gxt.ui.client.widget.form.FormPanel;
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
import com.qlkh.client.client.module.content.view.i18n.GroupManagerConstant;
import com.qlkh.client.client.module.content.view.security.GroupManagerSecurity;
import com.qlkh.client.client.widget.MyFitLayout;
import com.qlkh.core.client.model.Group;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class GroupManagerView.
 *
 * @author Nguyen Duc Dung
 * @since 7/26/13 9:20 AM
 */
@ViewSecurity(configuratorClass = GroupManagerSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = GroupManagerConstant.class)
public class GroupManagerView extends AbstractView<GroupManagerConstant> {

    public static final String ID_COLUMN = "id";
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 40;
    public static final String NAME_COLUMN = "name";
    public static final int NAME_WIDTH = 300;
    public static final String STATION_NAME_COLUMN = "station";
    public static final int STATION_NAME_WIDTH = 300;

    public static final int GROUP_LIST_SIZE = 100;

    @I18nField
    Button btnAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnDelete = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnEdit = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField
    Button btnGroupEditOk = new Button();

    @I18nField
    Button btnGroupEditCancel = new Button();

    @I18nField
    TextField<String> txtGroupName = new TextField<String>();

    @I18nField
    ComboBox<BeanModel> cbbStation = new ComboBox<BeanModel>();

    private ContentPanel contentPanel = new ContentPanel();
    private PagingToolBar pagingToolBar;
    private Grid<BeanModel> groupGird;

    private FormPanel groupEditPanel = new FormPanel();

    /**
     * Create Grid on View.
     */
    public void createGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        ColumnModel cm = new ColumnModel(createColumnConfig(selectionModel));
        groupGird = new Grid<BeanModel>(listStore, cm);
        groupGird.setBorders(true);
        groupGird.setLoadMask(true);
        groupGird.setStripeRows(true);
        groupGird.setSelectionModel(selectionModel);
        groupGird.addPlugin(selectionModel);
        groupGird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        groupGird.getStore().getLoader().setSortField(ID_COLUMN);
        groupGird.addListener(Events.OnKeyDown, new KeyListener() {
            @Override
            public void handleEvent(ComponentEvent e) {
                if (e.getKeyCode() == 112) {
                    btnAdd.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 113 || e.getKeyCode() == KeyCodes.KEY_ENTER) {
                    btnEdit.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 115) {
                    btnRefresh.fireEvent(Events.Select);
                }
            }
        });

        pagingToolBar = new PagingToolBar(GROUP_LIST_SIZE);
        ToolBar toolBar = new ToolBar();
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnEdit);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnRefresh);

        contentPanel.setLayout(new MyFitLayout());
        contentPanel.add(groupGird);
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
        ColumnConfig nameColumnConfig = new ColumnConfig(NAME_COLUMN, getConstant().nameColumnTitle(),
                NAME_WIDTH);
        columnConfigs.add(nameColumnConfig);

        ColumnConfig stationNameColumnConfig = new ColumnConfig(STATION_NAME_COLUMN, getConstant().stationNameColumnTitle(),
                STATION_NAME_WIDTH);
        stationNameColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
                Group group = model.getBean();
                String st = StringUtils.EMPTY;
                if (group.getStation() != null) {
                    st = group.getStation().getName();
                }
                return new Text(st);
            }
        });
        columnConfigs.add(stationNameColumnConfig);
        return columnConfigs;
    }

    public com.extjs.gxt.ui.client.widget.Window createGroupEditWindow() {
        com.extjs.gxt.ui.client.widget.Window window = new com.extjs.gxt.ui.client.widget.Window();
        if (!groupEditPanel.isRendered()) {
            groupEditPanel.setHeaderVisible(false);
            groupEditPanel.setBodyBorder(false);
            groupEditPanel.setBorders(false);
            groupEditPanel.setLabelWidth(120);
        }

        if (!txtGroupName.isRendered()) {
            txtGroupName.setAllowBlank(false);
            txtGroupName.setSelectOnFocus(true);
            txtGroupName.setMinLength(4);
        }

        groupEditPanel.add(txtGroupName);
        window.setFocusWidget(txtGroupName);

        if (!cbbStation.isRendered()) {
            cbbStation.setDisplayField("name");
            cbbStation.setForceSelection(true);
            cbbStation.setSelectOnFocus(true);
            cbbStation.setTriggerAction(ComboBox.TriggerAction.ALL);
            cbbStation.setAllowBlank(false);
        }

        groupEditPanel.add(cbbStation);

        window.add(groupEditPanel);
        window.addButton(btnGroupEditOk);
        window.addButton(btnGroupEditCancel);
        window.setSize(380, 150);
        window.setResizable(false);
        window.setModal(true);
        window.setHeading(getConstant().groupEditPanelTitle());
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                groupEditPanel.clear();
                groupGird.focus();
            }
        });
        return window;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public Button getBtnEdit() {
        return btnEdit;
    }

    public Button getBtnRefresh() {
        return btnRefresh;
    }

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }

    public Grid<BeanModel> getGroupGird() {
        return groupGird;
    }

    public Button getBtnGroupEditOk() {
        return btnGroupEditOk;
    }

    public Button getBtnGroupEditCancel() {
        return btnGroupEditCancel;
    }

    public TextField<String> getTxtGroupName() {
        return txtGroupName;
    }

    public ComboBox<BeanModel> getCbbStation() {
        return cbbStation;
    }

    public FormPanel getGroupEditPanel() {
        return groupEditPanel;
    }

}
