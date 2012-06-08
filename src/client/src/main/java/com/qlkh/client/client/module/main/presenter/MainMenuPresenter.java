/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.main.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.module.main.view.MainMenuView;
import com.qlkh.client.client.utils.UrlUtils;
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
            view.getLblWelcome().setHTML(view.getLblWelcome().getText() +
                    " <b>" +
                        LoginUtils.getUserName() +
                    "</b>");
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
