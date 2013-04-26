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
import com.qlkh.client.client.module.content.place.MaterialInPlace;
import com.qlkh.client.client.module.content.view.MaterialInView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.core.LoadAction;
import com.qlkh.core.client.action.core.LoadResult;
import com.qlkh.core.client.action.material.LoadMaterialWithoutPriceAction;
import com.qlkh.core.client.action.material.LoadMaterialWithoutPriceResult;
import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.criterion.ClientRestrictions;
import com.qlkh.core.client.model.*;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import net.customware.gwt.dispatch.client.DispatchAsync;

/**
 * The Class MaterialInPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 4:08 AM
 */
@Presenter(place = MaterialInPlace.class, view = MaterialInView.class)
public class MaterialInPresenter extends AbstractPresenter<MaterialInView> {

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;

    private Window editWindow;
    private MaterialIn currentMaterial;
    private QuarterEnum currentQuarter;
    private int currentYear;

    @Override
    public void onActivate() {
        view.show();
        if (currentQuarter != null && currentYear > 0) {
            view.getPagingToolBar().refresh();
        }
    }

    @Override
    protected void doBind() {
        dispatch.execute(new GetServerTimeAction(), new AbstractAsyncCallback<GetServerTimeResult>() {
            @Override
            public void onSuccess(GetServerTimeResult result) {
                currentQuarter = result.getQuarter();
                currentYear = result.getYear();
                final BeanModelFactory factory = BeanModelLookup.get().getFactory(Station.class);
                final ListStore<BeanModel> store = new ListStore<BeanModel>();
                view.getCbStation().setStore(store);
                StandardDispatchAsync.INSTANCE.execute(new LoadAction(Station.class.getName(), ClientRestrictions.ne("id", 27l)),
                        new AbstractAsyncCallback<LoadResult>() {
                            @Override
                            public void onSuccess(LoadResult result) {
                                for (Station entity : result.<Station>getList()) {
                                    store.add(factory.createModel(entity));
                                }
                                if (!result.getList().isEmpty()) {
                                    view.getCbStation().setValue(store.getAt(0));

                                    view.createGrid(GridUtils.createListStore(MaterialIn.class,
                                            ClientRestrictions.eq("year", currentYear),
                                            ClientRestrictions.eq("quarter", currentQuarter.getCode()),
                                            ClientRestrictions.eq("materialPerson.id", 1l))
                                    );
                                    view.getPagingToolBar().bind((PagingLoader<?>) view.getGird().getStore().getLoader());
                                    view.getPagingToolBar().refresh();
                                    view.getCbQuarter().setSimpleValue(currentQuarter);
                                    view.getCbYear().setSimpleValue(currentYear);
                                }
                            }
                        });
            }
        });

        view.getCbGroup().setStore(GridUtils.createListStore(MaterialGroup.class));
        view.getCbPerson().setStore(GridUtils.createListStore(MaterialPerson.class));

        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                editWindow = view.createMaterialEditWindow(createMaterialStore());
                view.getMaterialPagingToolBar().bind((PagingLoader<?>) view.getMaterialGrid().getStore().getLoader());
                if (view.getMaterialGrid().getStore().getLoadConfig() != null) {
                    resetMaterialFilter();
                }
                view.getMaterialPagingToolBar().refresh();
                editWindow.show();
            }
        });
    }

    private ListStore<BeanModel> createMaterialStore() {
        RpcProxy<LoadMaterialWithoutPriceResult> rpcProxy = new RpcProxy<LoadMaterialWithoutPriceResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadMaterialWithoutPriceResult> callback) {
                dispatch.execute(new LoadMaterialWithoutPriceAction((BasePagingLoadConfig) loadConfig, currentQuarter, currentYear), callback);
            }
        };

        PagingLoader<PagingLoadResult<Material>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<Material>>(rpcProxy, new LoadGridDataReader()) {
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
