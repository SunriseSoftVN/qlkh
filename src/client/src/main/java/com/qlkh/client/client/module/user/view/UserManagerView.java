/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.user.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.StationManagerView;
import com.qlkh.client.client.module.user.view.i18n.UserManagerConstant;
import com.qlkh.client.client.module.user.view.security.UserManagerSecurity;
import com.qlkh.client.client.widget.MyFitLayout;
import com.qlkh.core.client.constant.UserRoleEnum;
import com.qlkh.core.client.model.User;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Class UserManagerView.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 4:51 PM
 */
@ViewSecurity(configuratorClass = UserManagerSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = UserManagerConstant.class)
public class UserManagerView extends AbstractView<UserManagerConstant> {

    public static final String ID_COLUMN = "id";
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 40;
    public static final String USER_NAME_COLUMN = "userName";
    public static final int USER_NAME_COLUMN_WIDTH = 200;
    public static final String USER_STATION_COLUMN = "station";
    public static final int USER_STATION_COLUMN_WIDTH = 200;
    public static final String USER_ROLE_COLUMN = "userRole";
    public static final int USER_ROLE_COLUMN_WIDTH = 200;
    public static final String USER_PASSWORD_COLUMN = "passWord";
    public static final int USER_PASSWORD_COLUMN_WIDTH = 90;

    public static final int USER_LIST_SIZE = 100;

    @I18nField
    Button btnAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnDelete = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField
    TextField<String> txtNewPass = new TextField<String>();

    @I18nField
    TextField<String> txtConfirmPass = new TextField<String>();

    @I18nField
    Button btnChangePassWordOk = new Button();

    @I18nField
    Button btnChangePassWordCancel = new Button();

    @I18nField
    TextField<String> txtUserName = new TextField<String>();

    @I18nField
    SimpleComboBox<UserRoleEnum> cbbUserRole = new SimpleComboBox<UserRoleEnum>();

    @I18nField
    ComboBox<BeanModel> cbbUserStation = new ComboBox<BeanModel>();

    @I18nField
    Button btnNewUserOk = new Button();

    @I18nField
    Button btnNewUserCancel = new Button();

    private ContentPanel contentPanel = new ContentPanel();
    private FormPanel changePasswordPanel;
    private FormPanel newUserPanel = new FormPanel();

    private PagingToolBar pagingToolBar;
    private Grid<BeanModel> usersGrid;

    private GridCellRenderer<BeanModel> changePasswordCellRenderer;

    /**
     * Create Grid on View.
     */
    public void createGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        ColumnModel cm = new ColumnModel(createColumnConfig(selectionModel));
        usersGrid = new Grid<BeanModel>(listStore, cm);
        usersGrid.setBorders(true);
        usersGrid.setLoadMask(true);
        usersGrid.setStripeRows(true);
        usersGrid.setSelectionModel(selectionModel);
        usersGrid.addPlugin(selectionModel);
        usersGrid.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        usersGrid.getStore().getLoader().setSortField(ID_COLUMN);
        usersGrid.addListener(Events.OnKeyDown, new KeyListener() {
            @Override
            public void handleEvent(ComponentEvent e) {
                if (e.getKeyCode() == 112) {
                    btnAdd.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 115) {
                    btnRefresh.fireEvent(Events.Select);
                }
            }
        });

        pagingToolBar = new PagingToolBar(USER_LIST_SIZE);

        ToolBar toolBar = new ToolBar();
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnRefresh);

        contentPanel.setLayout(new MyFitLayout());
        contentPanel.add(usersGrid);
        contentPanel.setTopComponent(toolBar);
        contentPanel.setBottomComponent(pagingToolBar);
        contentPanel.setHeaderVisible(false);
        contentPanel.setHeight(com.google.gwt.user.client.Window.getClientHeight() - 90);
        com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {
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
                return new Text(String.valueOf(model.get("stt")));
            }
        });
        columnConfigs.add(sttColumnConfig);
        ColumnConfig userNameColumnConfig = new ColumnConfig(USER_NAME_COLUMN, getConstant().userNameColumnTitle(),
                USER_NAME_COLUMN_WIDTH);
        columnConfigs.add(userNameColumnConfig);

        ColumnConfig userStationColumnConfig = new ColumnConfig(USER_STATION_COLUMN, getConstant().userStationColumnTitle(),
                USER_STATION_COLUMN_WIDTH);
        userStationColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
                User user = model.getBean();
                String st = StringUtils.EMPTY;
                if (user.getStation() != null) {
                    st = user.getStation().getName();
                }
                return new Text(st);
            }
        });
        columnConfigs.add(userStationColumnConfig);

        ColumnConfig userRoleColumnConfig = new ColumnConfig(USER_ROLE_COLUMN, getConstant().userRoleColumnTitle(),
                USER_ROLE_COLUMN_WIDTH);
        columnConfigs.add(userRoleColumnConfig);
        ColumnConfig passWordColumnConfig = new ColumnConfig(USER_PASSWORD_COLUMN, getConstant().passWordColumnTitle(),
                USER_PASSWORD_COLUMN_WIDTH);
        passWordColumnConfig.setRenderer(getChangePasswordCellRenderer());
        passWordColumnConfig.setSortable(false);
        columnConfigs.add(passWordColumnConfig);
        return columnConfigs;
    }

    public Window createNewUserWindow() {
        Window window = new Window();
        if (!newUserPanel.isRendered()) {
            newUserPanel.setHeaderVisible(false);
            newUserPanel.setBodyBorder(false);
            newUserPanel.setBorders(false);
            newUserPanel.setLabelWidth(120);
        }

        if (!txtUserName.isRendered()) {
            txtUserName.setAllowBlank(false);
            txtUserName.setMinLength(4);
        }
        newUserPanel.add(txtUserName);
        window.setFocusWidget(txtUserName);

        if (!txtNewPass.isRendered()) {
            txtNewPass.setAllowBlank(false);
            txtNewPass.setMinLength(4);
            txtNewPass.setPassword(true);
        }
        newUserPanel.add(txtNewPass);

        if (!txtConfirmPass.isRendered()) {
            txtConfirmPass.setPassword(true);
            txtConfirmPass.setMinLength(4);
            txtConfirmPass.setAllowBlank(false);
        }
        newUserPanel.add(txtConfirmPass);

        if (!cbbUserStation.isRendered()) {
            cbbUserStation.setTriggerAction(ComboBox.TriggerAction.ALL);
            cbbUserStation.setForceSelection(true);
            cbbUserStation.setAllowBlank(false);
            cbbUserStation.setDisplayField(StationManagerView.STATION_NAME_COLUMN);
        }
        newUserPanel.add(cbbUserStation);

        if (!cbbUserRole.isRendered()) {
            cbbUserRole.add(Arrays.asList(UserRoleEnum.getAll()));
            cbbUserRole.setTriggerAction(ComboBox.TriggerAction.ALL);
            cbbUserRole.setSimpleValue(UserRoleEnum.USER);
        }
        cbbUserRole.setSimpleValue(UserRoleEnum.USER);
        newUserPanel.add(cbbUserRole);
        window.add(newUserPanel);
        window.addButton(btnNewUserOk);
        window.addButton(btnNewUserCancel);
        window.setSize(380, 220);
        window.setResizable(false);
        window.setHeading(getConstant().newUserWindowTitle());
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                getNewUserPanel().clear();
                getCbbUserRole().setSimpleValue(UserRoleEnum.USER);
                getUsersGrid().focus();
            }
        });
        return window;
    }

    public Window createChangePassWordWindow() {
        Window window = new Window();
        changePasswordPanel = new FormPanel();
        if (!txtNewPass.isRendered()) {
            txtNewPass.setAllowBlank(false);
            txtNewPass.setMinLength(4);
            txtNewPass.setPassword(true);
        }
        if (!txtConfirmPass.isRendered()) {
            txtConfirmPass.setPassword(true);
            txtConfirmPass.setMinLength(4);
            txtConfirmPass.setAllowBlank(false);
        }
        changePasswordPanel.add(txtNewPass);
        window.setFocusWidget(txtNewPass);
        changePasswordPanel.add(txtConfirmPass);
        changePasswordPanel.setHeaderVisible(false);
        changePasswordPanel.setBodyBorder(false);
        changePasswordPanel.setBorders(false);
        changePasswordPanel.setLabelWidth(120);
        window.add(changePasswordPanel);
        window.addButton(btnChangePassWordOk);
        window.addButton(btnChangePassWordCancel);
        window.setSize(380, 150);
        window.setResizable(false);
        window.setHeading(getConstant().btnChangePassword());
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                usersGrid.focus();
            }
        });
        return window;
    }

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }

    public Grid<BeanModel> getUsersGrid() {
        return usersGrid;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public Button getBtnRefresh() {
        return btnRefresh;
    }

    public GridCellRenderer<BeanModel> getChangePasswordCellRenderer() {
        return changePasswordCellRenderer;
    }

    public void setChangePasswordCellRenderer(GridCellRenderer<BeanModel> changePasswordCellRenderer) {
        this.changePasswordCellRenderer = changePasswordCellRenderer;
    }

    public TextField<String> getTxtNewPass() {
        return txtNewPass;
    }

    public TextField<String> getTxtConfirmPass() {
        return txtConfirmPass;
    }

    public Button getBtnChangePassWordOk() {
        return btnChangePassWordOk;
    }

    public Button getBtnChangePassWordCancel() {
        return btnChangePassWordCancel;
    }

    public Button getBtnNewUserCancel() {
        return btnNewUserCancel;
    }

    public Button getBtnNewUserOk() {
        return btnNewUserOk;
    }

    public SimpleComboBox<UserRoleEnum> getCbbUserRole() {
        return cbbUserRole;
    }

    public TextField<String> getTxtUserName() {
        return txtUserName;
    }

    public FormPanel getChangePasswordPanel() {
        return changePasswordPanel;
    }

    public FormPanel getNewUserPanel() {
        return newUserPanel;
    }

    public ComboBox<BeanModel> getCbbUserStation() {
        return cbbUserStation;
    }
}
