/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.qlvt.client.client.module.user.view;

import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.user.view.i18n.UserManagerConstant;
import com.qlvt.client.client.module.user.view.security.UserManagerSecurity;
import com.qlvt.core.client.constant.UserRoleEnum;
import com.qlvt.core.client.model.Station;
import com.qlvt.core.client.model.User;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
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
    public static final int STT_COLUMN_WIDTH = 50;
    public static final String USER_NAME_COLUMN = "userName";
    public static final int USER_NAME_COLUMN_WIDTH = 200;
    public static final String USER_STATION_COLUMN = "station";
    public static final int USER_STATION_COLUMN_WIDTH = 200;
    public static final String USER_ROLE_COLUMN = "userRole";
    public static final int USER_ROLE_COLUMN_WIDTH = 200;
    public static final String USER_PASSWORD_COLUMN = "passWord";
    public static final int USER_PASSWORD_COLUMN_WIDTH = 90;
    public static final int USER_LIST_SIZE = 50;

    @I18nField
    TextButton btnAdd = new TextButton();

    @I18nField
    TextButton btnDelete = new TextButton();

    @I18nField
    TextButton btnSave = new TextButton();

    @I18nField
    TextButton btnCancel = new TextButton();

    @I18nField
    TextField txtNewPass = new TextField();

    @I18nField
    TextField txtConfirmPass = new TextField();

    @I18nField
    TextButton btnChangePassWordOk = new TextButton();

    @I18nField
    TextButton btnChangePassWordCancel = new TextButton();

    @I18nField
    TextField txtUserName = new TextField();

    @I18nField
    SimpleComboBox<UserRoleEnum> cbbUserRole = new SimpleComboBox<UserRoleEnum>(new LabelProvider<UserRoleEnum>() {
        @Override
        public String getLabel(UserRoleEnum item) {
            return item.getRole();
        }
    });

    @I18nField
    SimpleComboBox<Station> cbbUserStation = new SimpleComboBox<Station>(new LabelProvider<Station>() {
        @Override
        public String getLabel(Station item) {
            return item.getName();
        }
    });

    @I18nField
    TextButton btnNewUserOk = new TextButton();

    @I18nField
    TextButton btnNewUserCancel = new TextButton();

    private ContentPanel contentPanel = new ContentPanel();
    private FormPanel changePasswordPanel;
    private FormPanel newUserPanel;

    private PagingToolBar pagingToolBar;
    private Grid<User> usersGrid;

//    private GridCellRenderer<BeanModel> changePasswordCellRenderer;

//    private CellEditor stationCellEditor;

    /**
     * Create Grid on View.
     */
    public void createGrid(ListStore<User> listStore) {
        GridSelectionModel<User> selectionModel = new GridSelectionModel<User>();
        ColumnModel cm = new ColumnModel(createColumnConfig(selectionModel));
        usersGrid = new Grid<User>(listStore, cm);
        usersGrid.setBorders(true);
        usersGrid.setLoadMask(true);
        usersGrid.setStripeRows(true);
        usersGrid.setSelectionModel(selectionModel);
//        usersGrid.addPlugin(selectionModel);
//        usersGrid.getStore().getLoader().setSortDir(Style.SortDir.ASC);
//        usersGrid.getStore().getLoader().setSortField(ID_COLUMN);

        pagingToolBar = new PagingToolBar(USER_LIST_SIZE);

        ToolBar toolBar = new ToolBar();
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnSave);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnCancel);

//        contentPanel.setLayout(new MyFitLayout());
        contentPanel.add(usersGrid);
//        contentPanel.setTopComponent(toolBar);
//        contentPanel.setBottomComponent(pagingToolBar);
        contentPanel.setHeaderVisible(false);
        contentPanel.setHeight(500);
//        com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {
//            @Override
//            public void onResize(ResizeEvent event) {
//                contentPanel.layout(true);
//            }
//        });
        setWidget(contentPanel);
    }

    private List<ColumnConfig> createColumnConfig(GridSelectionModel<User> selectionModel) {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
//        columnConfigs.add(selectionModel.getColumn());
        ColumnConfig sttColumnConfig = new ColumnConfig(new ValueProvider<User, String>() {
            @Override
            public String getValue(User object) {
                return null;
            }

            @Override
            public void setValue(User object, String value) {
            }

            @Override
            public String getPath() {
                return null;
            }
        });
//        sttColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
//            @Override
//            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
//                                 ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
//                if (model.get(STT_COLUMN) == null) {
//                    model.set(STT_COLUMN, rowIndex + 1);
//                }
//                return new Text(String.valueOf(model.get("stt")));
//            }
//        });
        columnConfigs.add(sttColumnConfig);
        ColumnConfig userNameColumnConfig = new ColumnConfig(new ValueProvider<User, String>() {
            @Override
            public String getValue(User object) {
                return null;
            }

            @Override
            public void setValue(User object, String value) {
            }

            @Override
            public String getPath() {
                return null;
            }
        });
        columnConfigs.add(userNameColumnConfig);

        ColumnConfig userStationColumnConfig = new ColumnConfig(new ValueProvider<User, Station>() {
            @Override
            public Station getValue(User object) {
                return null;
            }

            @Override
            public void setValue(User object, Station value) {
            }

            @Override
            public String getPath() {
                return null;
            }
        });
//        userStationColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
//            @Override
//            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
//                User user = model.getBean();
//                String st = StringUtils.EMPTY;
//                if (user.getStation() != null) {
//                    st = user.getStation().getName();
//                }
//                return new Text(st);
//            }
//        });
//        userStationColumnConfig.setEditor(getStationCellEditor());
        columnConfigs.add(userStationColumnConfig);

        ColumnConfig userRoleColumnConfig = new ColumnConfig(new ValueProvider<User, String>() {
            @Override
            public String getValue(User object) {
                return null;
            }

            @Override
            public void setValue(User object, String value) {
            }

            @Override
            public String getPath() {
                return null;
            }
        });
        columnConfigs.add(userRoleColumnConfig);
        ColumnConfig passWordColumnConfig = new ColumnConfig(new ValueProvider<User, String>() {
            @Override
            public String getValue(User object) {
                return null;
            }

            @Override
            public void setValue(User object, String value) {
            }

            @Override
            public String getPath() {
                return null;
            }
        });
//        passWordColumnConfig.setRenderer(getChangePasswordCellRenderer());
        passWordColumnConfig.setSortable(false);
        columnConfigs.add(passWordColumnConfig);
        return columnConfigs;
    }
//
//    public Window createNewUserWindow() {
//        Window window = new Window();
//        newUserPanel = new FormPanel();
//        newUserPanel.setHeaderVisible(false);
//        newUserPanel.setBodyBorder(false);
//        newUserPanel.setBorders(false);
//        newUserPanel.setLabelWidth(120);
//
//        if (!txtUserName.isRendered()) {
//            txtUserName.setAllowBlank(false);
//            txtUserName.setMinLength(4);
//        }
//        newUserPanel.add(txtUserName);
//        window.setFocusWidget(txtUserName);
//
//        if (!txtNewPass.isRendered()) {
//            txtNewPass.setAllowBlank(false);
//            txtNewPass.setMinLength(4);
//            txtNewPass.setPassword(true);
//        }
//        newUserPanel.add(txtNewPass);
//
//        if (!txtConfirmPass.isRendered()) {
//            txtConfirmPass.setPassword(true);
//            txtConfirmPass.setMinLength(4);
//            txtConfirmPass.setAllowBlank(false);
//        }
//        newUserPanel.add(txtConfirmPass);
//
//        if (!cbbUserStation.isRendered()) {
//            cbbUserStation.setTriggerAction(ComboBox.TriggerAction.ALL);
//            cbbUserStation.setForceSelection(true);
//            cbbUserStation.setAllowBlank(false);
//            cbbUserStation.setDisplayField(StationManagerView.STATION_NAME_COLUMN);
//        }
//        newUserPanel.add(cbbUserStation);
//
//        if (!cbbUserRole.isRendered()) {
//            cbbUserRole.add(Arrays.asList(UserRoleEnum.values()));
//            cbbUserRole.setTriggerAction(ComboBox.TriggerAction.ALL);
//        }
//        cbbUserRole.setSimpleValue(UserRoleEnum.USER);
//        newUserPanel.add(cbbUserRole);
//        window.add(newUserPanel);
//        window.addButton(btnNewUserOk);
//        window.addButton(btnNewUserCancel);
//        window.setSize(380, 220);
//        window.setResizable(false);
//        window.setHeading(getConstant().newUsetWindowTitle());
//        return window;
//    }
//
//    public Window createChangePassWordWindow() {
//        Window window = new Window();
//        changePasswordPanel = new FormPanel();
//        if (!txtNewPass.isRendered()) {
//            txtNewPass.setAllowBlank(false);
//            txtNewPass.setMinLength(4);
//            txtNewPass.setPassword(true);
//        }
//        if (!txtConfirmPass.isRendered()) {
//            txtConfirmPass.setPassword(true);
//            txtConfirmPass.setMinLength(4);
//            txtConfirmPass.setAllowBlank(false);
//        }
//        changePasswordPanel.add(txtNewPass);
//        window.setFocusWidget(txtNewPass);
//        changePasswordPanel.add(txtConfirmPass);
//        changePasswordPanel.setHeaderVisible(false);
//        changePasswordPanel.setBodyBorder(false);
//        changePasswordPanel.setBorders(false);
//        changePasswordPanel.setLabelWidth(120);
//        window.add(changePasswordPanel);
//        window.addButton(btnChangePassWordOk);
//        window.addButton(btnChangePassWordCancel);
//        window.setSize(380, 150);
//        window.setResizable(false);
//        window.setHeading(getConstant().btnChangePassword());
//        return window;
//    }
//
//    public PagingToolBar getPagingToolBar() {
//        return pagingToolBar;
//    }
//
//    public Grid<BeanModel> getUsersGrid() {
//        return usersGrid;
//    }
//
//    public Button getBtnAdd() {
//        return btnAdd;
//    }
//
//    public Button getBtnDelete() {
//        return btnDelete;
//    }
//
//    public Button getBtnCancel() {
//        return btnCancel;
//    }
//
//    public Button getBtnSave() {
//        return btnSave;
//    }
//
//    public GridCellRenderer<BeanModel> getChangePasswordCellRenderer() {
//        return changePasswordCellRenderer;
//    }
//
//    public void setChangePasswordCellRenderer(GridCellRenderer<BeanModel> changePasswordCellRenderer) {
//        this.changePasswordCellRenderer = changePasswordCellRenderer;
//    }
//
//    public TextField<String> getTxtNewPass() {
//        return txtNewPass;
//    }
//
//    public TextField<String> getTxtConfirmPass() {
//        return txtConfirmPass;
//    }
//
//    public Button getBtnChangePassWordOk() {
//        return btnChangePassWordOk;
//    }
//
//    public Button getBtnChangePassWordCancel() {
//        return btnChangePassWordCancel;
//    }
//
//    public Button getBtnNewUserCancel() {
//        return btnNewUserCancel;
//    }
//
//    public Button getBtnNewUserOk() {
//        return btnNewUserOk;
//    }
//
//    public SimpleComboBox<UserRoleEnum> getCbbUserRole() {
//        return cbbUserRole;
//    }
//
//    public TextField<String> getTxtUserName() {
//        return txtUserName;
//    }
//
//    public FormPanel getChangePasswordPanel() {
//        return changePasswordPanel;
//    }
//
//    public FormPanel getNewUserPanel() {
//        return newUserPanel;
//    }
//
//    public CellEditor getStationCellEditor() {
//        return stationCellEditor;
//    }
//
//    public void setStationCellEditor(CellEditor stationCellEditor) {
//        this.stationCellEditor = stationCellEditor;
//    }
//
//    public ComboBox<BeanModel> getCbbUserStation() {
//        return cbbUserStation;
//    }
}
