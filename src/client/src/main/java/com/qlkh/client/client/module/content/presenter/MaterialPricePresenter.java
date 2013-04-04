package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.PagingLoader;
import com.qlkh.client.client.module.content.place.MaterialPricePlace;
import com.qlkh.client.client.module.content.view.MaterialPriceView;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.model.Material;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

/**
 * The Class MaterialPricePresenter.
 *
 * @author Nguyen Duc Dung
 * @since 4/4/13 11:41 PM
 */
@Presenter(view = MaterialPriceView.class, place = MaterialPricePlace.class)
public class MaterialPricePresenter extends AbstractPresenter<MaterialPriceView> {

    @Override
    public void onActivate() {
        view.show();
        view.getMaterialPagingToolBar().refresh();
    }

    @Override
    protected void doBind() {
        view.show();
        view.createMaterialGrid(GridUtils.createListStore(Material.class));
        view.getMaterialPagingToolBar().bind((PagingLoader<?>) view.getMaterialGird().getStore().getLoader());
    }
}
