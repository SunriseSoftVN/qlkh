package com.qlkh.client.client.module.content.presenter;

import com.qlkh.client.client.module.content.place.MaterialInPlace;
import com.qlkh.client.client.module.content.view.MaterialInView;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

/**
 * The Class MaterialInPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 4:08 AM
 */
@Presenter(place = MaterialInPlace.class, view = MaterialInView.class)
public class MaterialInPresenter extends AbstractPresenter<MaterialInView> {
    @Override
    public void onActivate() {
        view.show();
    }
}
