package com.qlkh.client.client.module.content.presenter;

import com.qlkh.client.client.module.content.place.MaterialGroupPlace;
import com.qlkh.client.client.module.content.view.MaterialGroupView;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

/**
 * The Class MaterialGroupPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 3:46 AM
 */
@Presenter(view = MaterialGroupView.class, place = MaterialGroupPlace.class)
public class MaterialGroupPresenter extends AbstractPresenter<MaterialGroupView> {

    @Override
    public void onActivate() {
        view.show();
    }
}
