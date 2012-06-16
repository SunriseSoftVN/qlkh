/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.system.presenter;

import com.qlkh.client.client.module.system.place.ApplicationUpgradePlace;
import com.qlkh.client.client.module.system.view.ApplicationUpgradeView;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

/**
 * The Class ApplicationUpgradePresenter.
 *
 * @author Nguyen Duc Dung
 * @since 6/16/12, 7:08 PM
 */
@Presenter(view = ApplicationUpgradeView.class, place = ApplicationUpgradePlace.class)
public class ApplicationUpgradePresenter extends AbstractPresenter<ApplicationUpgradeView> {
    @Override
    public void onActivate() {
        view.show();
    }
}
