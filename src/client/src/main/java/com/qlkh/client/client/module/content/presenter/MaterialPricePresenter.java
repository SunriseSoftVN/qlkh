package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.module.content.place.MaterialPricePlace;
import com.qlkh.client.client.module.content.view.MaterialPriceView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.material.LoadMaterialLimitResult;
import com.qlkh.core.client.action.material.LoadMaterialPriceAction;
import com.qlkh.core.client.action.material.LoadMaterialPriceResult;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailDKAction;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailDKResult;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.client.model.MaterialPrice;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.TaskDetailDK;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import net.customware.gwt.dispatch.client.DispatchAsync;

/**
 * The Class MaterialPricePresenter.
 *
 * @author Nguyen Duc Dung
 * @since 4/4/13 11:41 PM
 */
@Presenter(view = MaterialPriceView.class, place = MaterialPricePlace.class)
public class MaterialPricePresenter extends AbstractPresenter<MaterialPriceView> {

    protected DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;
    private Material currentMaterial;


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

        view.getMaterialGird().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
                int index = se.getSelection().size() - 1;
                if (index >= 0) {
                    Material material = se.getSelection().get(index).getBean();
                    if (material.getId() != null) {
                        currentMaterial = material;
                        view.getPricePagingToolBar().refresh();
                    } else {
                        emptySubGird();
                    }
                }
            }
        });

        view.createPriceGrid(createPriceListStore());
        view.getPricePagingToolBar().bind((PagingLoader<?>) view.getPriceGird().getStore().getLoader());
    }

    protected ListStore<BeanModel> createPriceListStore() {
        RpcProxy<LoadMaterialPriceResult> rpcProxy = new RpcProxy<LoadMaterialPriceResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadMaterialPriceResult> callback) {
                long currentMaterialId = -1;
                if (currentMaterial != null) {
                    currentMaterialId = currentMaterial.getId();
                }
                dispatch.execute(new LoadMaterialPriceAction((BasePagingLoadConfig) loadConfig,
                        currentMaterialId), callback);
            }
        };

        PagingLoader<PagingLoadResult<MaterialPrice>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<MaterialPrice>>(rpcProxy, new LoadGridDataReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<BeanModel>(pagingLoader);
    }

    protected void resetView() {
        view.getMaterialPagingToolBar().refresh();
        emptySubGird();
    }

    protected void emptySubGird() {
        if (view.getPriceGird() != null) {
            view.getPriceGird().getStore().removeAll();
        }
    }
}
