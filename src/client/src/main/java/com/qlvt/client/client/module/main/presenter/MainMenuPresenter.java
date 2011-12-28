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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.qlvt.client.client.module.main.view.MainMenuView;
import com.qlvt.client.client.utils.UrlUtils;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.LoginUtils;

/**
 * The Class MainMenuPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 9:07 AM
 */
@Presenter(view = MainMenuView.class)
public class MainMenuPresenter extends AbstractPresenter<MainMenuView> {
    @Override
    public void onActivate() {
        view.show();
        if (LoginUtils.getLogin()) {
            view.getLblWelcome().setText(view.getLblWelcome().getText() + " "
                    + LoginUtils.getUserName());
            view.getLayout().layout();
        }
    }

    @Override
    protected void doBind() {
        view.getAncLogout().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                LoginUtils.logOut();
                goToHomePage();
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
