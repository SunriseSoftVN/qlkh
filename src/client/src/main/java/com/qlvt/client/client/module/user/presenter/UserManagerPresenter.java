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

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.user.place.UserManagerPlace;
import com.qlvt.client.client.module.user.view.UserManagerView;
import com.qlvt.client.client.service.UserService;
import com.qlvt.client.client.service.UserServiceAsync;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.client.client.utils.LoadingUtils;
import com.qlvt.core.client.model.User;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;

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

    private UserServiceAsync userService = UserService.App.getInstance();

    @Override
    public void onActivate() {
        view.show();
        view.getPagingToolBar().refresh();
    }

    @Override
    protected void doBind() {
        view.createGrid(createUserListStore());
        view.getPagingToolBar().bind((PagingLoader<?>) view.getUsersGrid().getStore().getLoader());
        view.getBtnCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                view.getUsersGrid().getStore().rejectChanges();
            }
        });
        view.getBtnSave().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                List<User> users = new ArrayList<User>();
                for (Record record : view.getUsersGrid().getStore().getModifiedRecords()) {
                    users.add(((BeanModel) record.getModel()).<User>getBean());
                }
                LoadingUtils.showLoading();
                userService.updateUsers(users, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        super.onSuccess(result);
                        DiaLogUtils.notify("Cap nhat thanh cong");
                        view.getUsersGrid().getStore().commitChanges();
                    }
                });
            }
        });
        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
    }

    private ListStore<BeanModel> createUserListStore() {
        RpcProxy<BasePagingLoadResult<List<User>>> rpcProxy = new RpcProxy<BasePagingLoadResult<List<User>>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<BasePagingLoadResult<List<User>>> callback) {
                userService.getUsersForGrid((BasePagingLoadConfig) loadConfig, callback);
            }
        };

        PagingLoader<PagingLoadResult<User>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<User>>(rpcProxy, new BeanModelReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
        };

        return new ListStore<BeanModel>(pagingLoader);
    }

    private class DeleteButtonEventListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            final List<BeanModel> models = view.getUsersGrid().getSelectionModel().getSelectedItems();
            if (CollectionsUtils.isNotEmpty(models)) {
                if (models.size() == 1) {
                    final User user = (User) models.get(0).getBean();
                    showDeleteTagConform(user.getId(), user.getUserName());
                } else {
                    List<Long> userIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        final User user = (User) model.getBean();
                        userIds.add(user.getId());
                    }
                    showDeleteTagConform(userIds, null);
                }
            }
        }
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
}
