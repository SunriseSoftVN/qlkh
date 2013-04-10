package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Timer;
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
import com.qlkh.core.client.model.MaterialLimit;
import com.qlkh.core.client.model.MaterialPrice;
import com.qlkh.core.client.model.Task;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.HashMap;
import java.util.Map;

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
        if (currentQuarter != null && currentYear > 0) {
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

        view.getTxtMaterialSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                String st = view.getTxtMaterialSearch().getValue();
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    if (StringUtils.isNotBlank(st)) {
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getMaterialGrid().
                                getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put("name", view.getTxtMaterialSearch().getValue());
                        filters.put("code", view.getTxtMaterialSearch().getValue());
                        loadConfig.set("filters", filters);
                    } else {
                        resetMaterialFilter();
                    }
                    view.getMaterialPagingToolBar().refresh();
                } else if (event.getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    resetMaterialFilter();
                    com.google.gwt.user.client.Timer timer = new Timer() {
                        @Override
                        public void run() {
                            view.getMaterialPagingToolBar().refresh();
                        }
                    };
                    timer.schedule(100);
                }
            }
        });

        view.getBtnMaterialAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                Material material = view.getMaterialGrid().getSelectionModel().getSelectedItem().getBean();

                boolean isFound = false;
                for (BeanModel model : view.getMaterialPriceGird().getStore().getModels()) {
                    MaterialPrice materialPrice = model.getBean();
                    if (materialPrice.getMaterial().getId().equals(material.getId())) {
                        isFound = true;
                        break;
                    }
                }

                if (!isFound) {
                    MaterialPrice materialPrice = new MaterialPrice();
                    materialPrice.setMaterial(material);
                    materialPrice.setUpdateBy(1l);
                    materialPrice.setCreateBy(1l);

                    BeanModelFactory factory = BeanModelLookup.get().getFactory(MaterialPrice.class);
                    BeanModel insertModel = factory.createModel(materialPrice);
                    view.getMaterialPriceGird().getStore().add(insertModel);
                    view.getMaterialPriceGird().getSelectionModel().select(insertModel, false);
                    DiaLogUtils.notify(view.getConstant().addSuccess());
                } else {
                    DiaLogUtils.notify(view.getConstant().addAlready());
                }
            }
        });

        view.getBtnMaterialEditOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                materialEditWindow.hide();
            }
        });

        view.getBtnMaterialEditCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                materialEditWindow.hide();
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

    private void updateGrid(MaterialPrice materialPrice) {
        boolean isNotFound = true;
        BeanModelFactory factory = BeanModelLookup.get().getFactory(MaterialPrice.class);
        BeanModel updateModel = factory.createModel(materialPrice);
        for (BeanModel model : view.getMaterialPriceGird().getStore().getModels()) {
            if (materialPrice.getId().equals(model.<MaterialPrice>getBean().getId())) {
                int index = view.getMaterialPriceGird().getStore().indexOf(model);
                view.getMaterialPriceGird().getStore().remove(model);
                view.getMaterialPriceGird().getStore().insert(updateModel, index);
                isNotFound = false;
            }
        }
        if (isNotFound) {
            view.getMaterialPriceGird().getStore().add(updateModel);
            view.getMaterialPriceGird().getView().ensureVisible(view.getMaterialPriceGird()
                    .getStore().getCount() - 1, 1, false);
        }
        view.getMaterialPriceGird().getSelectionModel().select(updateModel, false);
    }
}
