package com.qlkh.client.client.module.content.presenter;

import com.qlkh.client.client.module.content.place.LimitJobPlace;
import com.qlkh.client.client.module.content.view.LimitJobView;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

/**
 * The Class LimitJobPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 10:10 AM
 */
@Presenter(view = LimitJobView.class, place = LimitJobPlace.class)
public class LimitJobPresenter extends AbstractPresenter<LimitJobView> {
    @Override
    public void onActivate() {
        view.show();
    }
}
