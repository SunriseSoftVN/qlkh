package com.qlkh.client.client.module.content.presenter;

import com.qlkh.client.client.module.content.place.MaterialPersonPlace;
import com.qlkh.client.client.module.content.view.MaterialPersonView;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

/**
 * The Class MaterialPersonPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 3:20 AM
 */
@Presenter(view = MaterialPersonView.class, place = MaterialPersonPlace.class)
public class MaterialPersonPresenter extends AbstractPresenter<MaterialPersonView> {

    @Override
    public void onActivate() {
        view.show();
    }
}
