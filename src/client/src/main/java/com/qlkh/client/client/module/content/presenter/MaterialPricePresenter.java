package com.qlkh.client.client.module.content.presenter;

import com.qlkh.client.client.module.content.place.MaterialPlace;
import com.qlkh.client.client.module.content.place.MaterialPricePlace;
import com.qlkh.client.client.module.content.view.MaterialPriceView;
import com.qlkh.client.client.module.content.view.MaterialView;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

/**
 * The Class MaterialPricePresenter.
 *
 * @author Nguyen Duc Dung
 * @since 4/4/13 11:41 PM
 */
@Presenter(view = MaterialView.class, place = MaterialPricePlace.class)
public class MaterialPricePresenter extends AbstractPresenter<MaterialPriceView> {



}
