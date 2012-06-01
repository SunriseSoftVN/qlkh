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

package com.qlvt.client.client.module.user.presenter;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.qlvt.client.client.core.dispatch.StandardDispatchAsync;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.user.place.UserManagerPlace;
import com.qlvt.client.client.module.user.view.UserManagerView;
import com.qlvt.client.client.service.UserService;
import com.qlvt.client.client.service.UserServiceAsync;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.client.client.utils.GridUtils;
import com.qlvt.client.client.utils.LoadingUtils;
import com.qlvt.core.client.constant.UserRoleEnum;
import com.qlvt.core.client.exception.CodeExistException;
import com.qlvt.core.client.model.Station;
import com.qlvt.core.client.model.User;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.LoginUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class UserManagerPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 4:53 PM
 */
@Presenter(view = UserManagerView.class, place = UserManagerPlace.class)
public class UserManagerPresenter extends AbstractPresenter<UserManagerView> {

    private DispatchAsync dispatch = new StandardDispatchAsync(new DefaultExceptionHandler());

    private UserServiceAsync userService = UserService.App.getInstance();

    private ListStore<BeanModel> stationListStore;
    private Window newUserWindow;
    private Window changePasswordWindow;

    @Override
    public void onActivate() {
        view.show();
        if (stationListStore != null) {
            //reload stations list.
            stationListStore = GridUtils.getListStoreForCb(Station.class, dispatch);
            view.getCbbUserStation().setStore(stationListStore);
        }
        view.getPagingToolBar().refresh();
        view.getUsersGrid().focus();
    }

    @Override
    protected void doBind() {
        stationListStore = GridUtils.createListStore(Station.class, dispatch);
        view.getCbbUserStation().setStore(stationListStore);
        view.setChangePasswordCellRenderer(new ChangePasswordCellRenderer());
        view.createGrid(GridUtils.createListStore(User.class, dispatch));
        view.getPagingToolBar().bind((PagingLoader<?>) view.getUsersGrid().getStore().getLoader());
        view.getBtnRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                view.getPagingToolBar().refresh();
            }
        });
        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                newUserWindow = view.createNewUserWindow();
                newUserWindow.show();
            }
        });
        view.getBtnNewUserOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (view.getNewUserPanel().isValid()) {
                    String newPass = view.getTxtNewPass().getValue();
                    String comPass = view.getTxtConfirmPass().getValue();
                    if (StringUtils.isNotBlank(newPass) && newPass.equals(comPass)) {
                        User user = new User();
                        user.setUserName(view.getTxtUserName().getValue());
                        user.setPassWord(LoginUtils.md5hash(newPass));
                        user.setUserRole(view.getCbbUserRole().getValue().getValue().getRole());
                        user.setStation(view.getCbbUserStation().getValue().<Station>getBean());
                        user.setCreateBy(1l);
                        user.setUpdateBy(1l);
                        LoadingUtils.showLoading();
                        userService.updateUser(user, new AbstractAsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingUtils.hideLoading();
                                if (caught instanceof CodeExistException) {
                                    DiaLogUtils.logAndShowErrorMessage(view.getConstant().userExitsErrorMessage(), caught);
                                } else {
                                    super.onFailure(caught);
                                }
                            }

                            @Override
                            public void onSuccess(Void result) {
                                super.onSuccess(result);
                                DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                                newUserWindow.hide();
                                view.getPagingToolBar().refresh();
                            }
                        });
                    } else {
                        DiaLogUtils.showMessage(view.getConstant().passWordErrorMessage());
                    }
                }
            }
        });
        view.getBtnNewUserCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                newUserWindow.hide();
            }
        });
    }


    private class DeleteButtonEventListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            final List<BeanModel> models = view.getUsersGrid().getSelectionModel().getSelectedItems();
            if (CollectionsUtils.isNotEmpty(models)) {
                if (models.size() == 1) {
                    final User user = (User) models.get(0).getBean();
                    //Check don't allow delete Admin & Manager Users.
                    if (user.getUserRole().equals(UserRoleEnum.ADMIN.getRole())
                            || user.getUserRole().equals(UserRoleEnum.MANAGER.getRole())) {
                        DiaLogUtils.showMessage(view.getConstant().deleteAdminOrManagerMessage());
                        return;
                    }
                    showDeleteTagConform(user.getId(), user.getUserName());
                } else {
                    List<Long> userIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        final User user = (User) model.getBean();
                        //Check don't allow delete Admin & Manager Users.
                        if (user.getUserRole().equals(UserRoleEnum.ADMIN.getRole())
                                || user.getUserRole().equals(UserRoleEnum.MANAGER.getRole())) {
                            DiaLogUtils.showMessage(view.getConstant().deleteAdminOrManagerMessage());
                            return;
                        }
                        Log.debug(user.getUserRole());
                        userIds.add(user.getId());
                    }
                    showDeleteTagConform(userIds, null);
                }
            }
        }
    }

    private class ChangePasswordCellRenderer implements GridCellRenderer<BeanModel> {
        @Override
        public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                             ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
            return createChangePassWordAnchor(model, beanModelListStore);
        }
    }

    private Anchor createChangePassWordAnchor(final BeanModel model, final ListStore<BeanModel> beanModelListStore) {
        Anchor ancChangePassword = new Anchor(view.getConstant().btnChangePassword());
        ancChangePassword.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                changePasswordWindow = view.createChangePassWordWindow();
                changePasswordWindow.show();
                view.getBtnChangePassWordOk().removeAllListeners();
                view.getBtnChangePassWordOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        String newPass = view.getTxtNewPass().getValue();
                        String comPass = view.getTxtConfirmPass().getValue();
                        if (StringUtils.isNotBlank(newPass) && newPass.equals(comPass)
                                && view.getChangePasswordPanel().isValid()) {
                            User user = model.getBean();
                            user.setPassWord(LoginUtils.md5hash(newPass));
                            userService.updateUser(user, new AbstractAsyncCallback<Void>() {
                                @Override
                                public void onSuccess(Void result) {
                                    super.onSuccess(result);
                                    DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                                    beanModelListStore.update(model);
                                    changePasswordWindow.hide();
                                }
                            });
                        } else {
                            DiaLogUtils.showMessage(view.getConstant().passWordErrorMessage());
                        }
                    }
                });
                view.getBtnChangePassWordCancel().removeAllListeners();
                view.getBtnChangePassWordCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        changePasswordWindow.hide();
                    }
                });
                changePasswordWindow.addWindowListener(new WindowListener() {
                    @Override
                    public void windowHide(WindowEvent we) {
                        view.getChangePasswordPanel().clear();
                    }
                });
            }
        });
        return ancChangePassword;
    }

    private void showDeleteTagConform(long tagId, String tagName) {
        List<Long> tagIds = new ArrayList<Long>(1);
        tagIds.add(tagId);
        showDeleteTagConform(tagIds, tagName);
    }

    private void showDeleteTagConform(final List<Long> userIds, String tagName) {
        assert userIds != null;
        String deleteMessage;
        final AsyncCallback<Void> callback = new AbstractAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                super.onSuccess(result);
                //Reload grid.
                view.getPagingToolBar().refresh();
                DiaLogUtils.notify(view.getConstant().deleteUserMessageSuccess());
            }
        };
        final boolean hasManyTag = userIds.size() > 1;
        if (hasManyTag) {
            deleteMessage = view.getConstant().deleteAllUserMessage();
        } else {
            deleteMessage = StringUtils.substitute(view.getConstant().deleteUserMessage(), tagName);
        }

        DiaLogUtils.conform(deleteMessage, new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked().getText().equals("Yes")) {
                    LoadingUtils.showLoading();
                    if (hasManyTag) {
                        userService.deleteUserByIds(userIds, callback);
                    } else {
                        userService.deleteUserById(userIds.get(0), callback);
                    }
                }
            }
        });
    }

    @Override
    public String mayStop() {
        if ((newUserWindow != null && newUserWindow.isVisible())
                || (changePasswordWindow != null && changePasswordWindow.isVisible())) {
            return view.getConstant().conformExitMessage();
        }
        return null;
    }

    @Override
    public void onCancel() {
        if (newUserWindow != null && newUserWindow.isVisible()) {
            newUserWindow.hide();
        }

        if (changePasswordWindow != null && changePasswordWindow.isVisible()) {
            changePasswordWindow.hide();
        }
    }
}
