/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.main.presenter;

import com.qlkh.client.client.module.main.view.CopyRightView;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

@Presenter(view = CopyRightView.class)
public class CopyRightPresenter extends AbstractPresenter<CopyRightView> {
    @Override
    public void onActivate() {
        view.show();
    }
}
