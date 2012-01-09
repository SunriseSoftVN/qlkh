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

package com.qlvt.client.client.module.main.presenter;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Window;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.main.place.LoginPlace;
import com.qlvt.client.client.module.main.view.LoginView;
import com.qlvt.client.client.service.LoginService;
import com.qlvt.client.client.service.LoginServiceAsync;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.client.client.utils.UrlUtils;
import com.qlvt.core.client.exception.UserAuthenticationException;
import com.qlvt.core.client.model.User;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.LoginUtils;

/**
 * The Class LoginPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 9:51 AM
 */
@Presenter(view = LoginView.class, place = LoginPlace.class)
public class LoginPresenter extends AbstractPresenter<LoginView> {

    private LoginServiceAsync loginService = LoginService.App.getInstance();

    @Override
    public void onActivate() {
        view.show();
        view.getTxtUserName().focus();
    }

    @Override
    protected void doBind() {
        view.getBtnOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                checkLogin(view.getTxtUserName().getValue(),
                        LoginUtils.md5hash(view.getTxtPassWord().getValue()));
            }
        });
        view.getTxtPassWord().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    checkLogin(view.getTxtUserName().getValue(),
                            LoginUtils.md5hash(view.getTxtPassWord().getValue()));
                }
            }
        });
        view.getBtnCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                view.getTxtPassWord().clear();
                view.getTxtUserName().clear();
            }
        });
    }

    public void checkLogin(final String userName, final String passWord) {
        loginService.checkLogin(userName, passWord, new AbstractAsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof UserAuthenticationException) {
                    DiaLogUtils.showMessage("The username or password you entered is incorrect");
                } else {
                    super.onFailure(caught);
                }
            }

            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    LoginUtils.login(userName, user.getUserRole());
                    //Reload page and go to home page.
                    goToHomePage();
                }
            }
        });
    }

    private void goToHomePage() {
        String url = UrlUtils
                .removeHistoryToken(Window.Location.getHref());
        if (url.equals(Window.Location.getHref())) {
            Window.Location.reload();
        } else {
            Window.Location.replace(url);
        }
    }
}
