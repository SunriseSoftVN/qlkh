package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.MaterialPricePlace;
import com.qlkh.client.client.module.content.view.MaterialPriceView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.material.LoadMaterialPriceAction;
import com.qlkh.core.client.action.material.LoadMaterialPriceResult;
import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.client.model.MaterialPrice;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import net.customware.gwt.dispatch.client.DispatchAsync;

/**
 * The Class MaterialPricePresenter.
 *
 * @author Nguyen Duc Dung
 * @since 4/9/13 7:26 PM
 */
@Presenter(view = MaterialPriceView.class, place = MaterialPricePlace.class)
public class MaterialPricePresenter extends AbstractPresenter<MaterialPriceView> {

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;
    private QuarterEnum currentQuarter;
    private int currentYear;
    private Window materialEditWindow;

    @Override
    public void onActivate() {
        view.show();
        if(currentQuarter != null && currentYear > 0) {
            view.getMaterialPricePagingToolBar().refresh();
        }
    }

    @Override
    protected void doBind() {
        dispatch.execute(new GetServerTimeAction(), new AbstractAsyncCallback<GetServerTimeResult>() {
            @Override
            public void onSuccess(GetServerTimeResult result) {
                currentQuarter = result.getQuarter();
                currentYear = result.getYear();

                view.createGrid(createMaterialPriceListStore());
                view.getMaterialPricePagingToolBar().bind((PagingLoader<?>) view.getMaterialPriceGird().getStore().getLoader());
                view.getMaterialPricePagingToolBar().refresh();

                view.getCbQuarter().setSimpleValue(result.getQuarter());
            }
        });

        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                materialEditWindow = view.createMaterialEditWindow(GridUtils.createListStore(Material.class));
                view.getMaterialPagingToolBar().bind((PagingLoader<?>) view.getMaterialGrid().getStore().getLoader());
                if (view.getMaterialGrid().getStore().getLoadConfig() != null) {
                    resetMaterialFilter();
                }
                view.getMaterialPagingToolBar().refresh();
                materialEditWindow.show();
            }
        });
    }

    private ListStore<BeanModel> createMaterialPriceListStore() {
        RpcProxy<LoadMaterialPriceResult> rpcProxy = new RpcProxy<LoadMaterialPriceResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadMaterialPriceResult> callback) {
                dispatch.execute(new LoadMaterialPriceAction((BasePagingLoadConfig) loadConfig,
                        currentQuarter, currentYear), callback);
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

    private void resetMaterialFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getMaterialGrid().
                getStore().getLoadConfig();
        loadConfig.set("hasFilter", false);
        loadConfig.set("filters", null);
        view.getTxtMaterialSearch().clear();
    }
}
