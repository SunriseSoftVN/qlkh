/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.main.presenter;

import com.qlkh.client.client.module.main.view.BannerView;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

/**
 * The Class BannerPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 8:57 AM
 */
@Presenter(view = BannerView.class)
public class BannerPresenter extends AbstractPresenter<BannerView> {
    @Override
    public void onActivate() {
        view.show();
    }
}
