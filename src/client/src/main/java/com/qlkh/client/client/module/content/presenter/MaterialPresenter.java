package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.qlkh.client.client.module.content.place.MaterialPlace;
import com.qlkh.client.client.module.content.view.MaterialView;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.model.Material;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

/**
 * The Class MaterialPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 3/25/13 1:34 PM
 */
@Presenter(view = MaterialView.class, place = MaterialPlace.class)
public class MaterialPresenter extends AbstractPresenter<MaterialView> {

    private Window materialWindow;

    @Override
    public void onActivate() {
        view.show();
        view.getPagingToolBar().refresh();
    }

    @Override
    protected void doBind() {
        view.createGrid(GridUtils.createListStore(Material.class));
        view.getPagingToolBar().bind((PagingLoader<?>) view.getTaskGird().getStore().getLoader());
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                materialWindow = view.createMaterialEditWindow();
                materialWindow.show();
                materialWindow.layout(true);
            }
        });
    }
}
