package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
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
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.action.grid.LoadGridDataAction;
import com.qlkh.core.client.action.grid.LoadGridDataResult;
import com.qlkh.core.client.action.material.LoadMaterialInTotalAction;
import com.qlkh.core.client.action.material.LoadMaterialInTotalResult;
import com.qlkh.core.client.action.material.LoadMaterialWithTaskAction;
import com.qlkh.core.client.action.material.LoadMaterialWithTaskResult;
import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.criterion.ClientRestrictions;
import com.qlkh.core.client.model.*;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.Date;

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
    private Station currentStation;

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
                StandardDispatchAsync.INSTANCE.execute(new LoadAction(Station.class.getName(), ClientRestrictions.eq("company", false)),
                        new AbstractAsyncCallback<LoadResult>() {
                            @Override
                            public void onSuccess(LoadResult result) {
                                for (Station entity : result.<Station>getList()) {
                                    store.add(factory.createModel(entity));
                                }
                                if (!result.getList().isEmpty()) {
                                    view.getCbStation().setValue(store.getAt(0));
                                    currentStation = store.getAt(0).getBean();
                                    view.createGrid(createGridStore());
                                    view.getPagingToolBar().bind((PagingLoader<?>) view.getGird().getStore().getLoader());
                                    view.getPagingToolBar().refresh();
                                    view.getCbQuarter().setSimpleValue(currentQuarter);
                                    view.getCbYear().setSimpleValue(currentYear);

                                    view.getCbStation().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
                                        @Override
                                        public void selectionChanged(SelectionChangedEvent<BeanModel> beanModelSelectionChangedEvent) {
                                            currentStation = view.getCbStation().getValue().getBean();
                                            view.getPagingToolBar().refresh();
                                        }
                                    });

                                    view.getCbQuarter().addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<QuarterEnum>>() {
                                        @Override
                                        public void selectionChanged(SelectionChangedEvent<SimpleComboValue<QuarterEnum>> simpleComboValueSelectionChangedEvent) {
                                            currentQuarter = view.getCbQuarter().getSimpleValue();
                                            view.getPagingToolBar().refresh();
                                        }
                                    });

                                    view.getCbQuarter().addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<QuarterEnum>>() {
                                        @Override
                                        public void selectionChanged(SelectionChangedEvent<SimpleComboValue<QuarterEnum>> simpleComboValueSelectionChangedEvent) {
                                            currentYear = view.getCbYear().getSimpleValue();
                                            view.getPagingToolBar().refresh();
                                        }
                                    });
                                }
                            }
                        });
            }
        });

        view.getCbGroup().setStore(GridUtils.createListStoreForCb(MaterialGroup.class));

        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                if (!currentStation.isCompany()) {
                    if (view.getCbPerson().getStore() != null) {
                        view.getCbPerson().reset();
                        view.getCbPerson().getStore().removeAll();
                    }
                    view.getCbPerson().setStore(GridUtils.createListStoreForCb(MaterialPerson.class,
                            ClientRestrictions.eq("station.id", currentStation.getId())));
                } else {
                    if (view.getCbPerson().getStore() != null) {
                        view.getCbPerson().reset();
                        view.getCbPerson().getStore().removeAll();
                    }
                    view.getCbPerson().setStore(GridUtils.createListStoreForCb(MaterialPerson.class));
                }

                currentMaterial = new MaterialIn();
                currentMaterial.setCreateBy(1l);
                currentMaterial.setUpdateBy(1l);

                editWindow = view.createMaterialEditWindow(createMaterialStore());
                view.getMaterialPagingToolBar().bind((PagingLoader<?>) view.getMaterialGrid().getStore().getLoader());
                if (view.getMaterialGrid().getStore().getLoadConfig() != null) {
                    resetMaterialFilter();
                }
                view.getMaterialPagingToolBar().refresh();
                view.getMaterialGrid().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
                    @Override
                    public void selectionChanged(SelectionChangedEvent<BeanModel> event) {
                        if (event.getSelectedItem() != null) {
                            Material material = event.getSelectedItem().getBean();
                            LoadMaterialInTotalAction loadAction = new LoadMaterialInTotalAction(material.getId(), currentStation.getId(),
                                    null, currentQuarter.getCode(), currentYear);
                            view.getMaterialGrid().mask();
                            dispatch.execute(loadAction, new AbstractAsyncCallback<LoadMaterialInTotalResult>() {
                                @Override
                                public void onSuccess(LoadMaterialInTotalResult result) {
                                    view.getMaterialGrid().unmask();
                                    view.getTxtTotal().setValue(result.getTotal());
                                }
                            });
                        }
                    }
                });
                editWindow.show();
            }
        });

        view.getBtnRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                resetFilter();
                view.getPagingToolBar().refresh();
            }
        });

        view.getBtnEditCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                view.getEditPanel().reset();
                editWindow.hide();
            }
        });

        view.getBtnEditOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                if (view.getEditPanel().isValid()) {
                    if (view.getMaterialGrid().getSelectionModel().getSelectedItem() != null) {
                        Material material = view.getMaterialGrid().getSelectionModel().getSelectedItem().getBean();
                        double total = view.getTxtTotal().getValue().doubleValue();
                        double weight = view.getTxtWeight().getValue().doubleValue();
                        double remain = total - weight;

                        if (remain >= 0) {
                            MaterialPerson materialPerson = view.getCbPerson().getValue().getBean();
                            MaterialGroup materialGroup = view.getCbGroup().getValue().getBean();

                            currentMaterial.setMaterial(material);
                            currentMaterial.setMaterialGroup(materialGroup);
                            currentMaterial.setMaterialPerson(materialPerson);
                            currentMaterial.setTotal(total);
                            currentMaterial.setWeight(weight);
                            currentMaterial.setCreatedDate(new Date());
                            currentMaterial.setStation(currentStation);
                            currentMaterial.setYear(currentYear);
                            currentMaterial.setQuarter(currentQuarter.getCode());
                            currentMaterial.setCode(view.getTxtCode().getValue());

                            dispatch.execute(new SaveAction(currentMaterial), new AbstractAsyncCallback<SaveResult>() {
                                @Override
                                public void onSuccess(SaveResult saveResult) {
                                    editWindow.hide();
                                    view.getEditPanel().reset();
                                    view.getPagingToolBar().refresh();
                                }
                            });
                        } else {
                            DiaLogUtils.showMessage(view.getConstant().totalError());
                        }
                    } else {
                        DiaLogUtils.showMessage(view.getConstant().materialError());
                    }
                }
            }
        });
    }

    private ListStore<BeanModel> createGridStore() {
        RpcProxy<LoadGridDataResult> rpcProxy = new RpcProxy<LoadGridDataResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadGridDataResult> callback) {
                LoadGridDataAction loadAction;
                if (!currentStation.isCompany()) {
                    loadAction = new LoadGridDataAction(MaterialIn.class.getName(),
                            ClientRestrictions.eq("year", currentYear),
                            ClientRestrictions.eq("quarter", currentQuarter.getCode()),
                            ClientRestrictions.eq("station.id", currentStation.getId()));
                } else {
                    loadAction = new LoadGridDataAction(MaterialIn.class.getName(),
                            ClientRestrictions.eq("year", currentYear),
                            ClientRestrictions.eq("quarter", currentQuarter.getCode()));
                }

                loadAction.setConfig((BasePagingLoadConfig) loadConfig);
                dispatch.execute(loadAction, callback);
            }
        };

        PagingLoader<PagingLoadResult<MaterialIn>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<MaterialIn>>(rpcProxy, new LoadGridDataReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<BeanModel>(pagingLoader);
    }


    private ListStore<BeanModel> createMaterialStore() {
        RpcProxy<LoadMaterialWithTaskResult> rpcProxy = new RpcProxy<LoadMaterialWithTaskResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadMaterialWithTaskResult> callback) {
                dispatch.execute(new LoadMaterialWithTaskAction((BasePagingLoadConfig) loadConfig, currentQuarter.getCode(), currentYear), callback);
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

    private void updateGrid(MaterialIn materialIn) {
        boolean isNotFound = true;
        BeanModelFactory factory = BeanModelLookup.get().getFactory(MaterialIn.class);
        BeanModel updateModel = factory.createModel(materialIn);
        for (BeanModel model : view.getGird().getStore().getModels()) {
            if (materialIn.getId().equals(model.<MaterialIn>getBean().getId())) {
                int index = view.getGird().getStore().indexOf(model);
                view.getGird().getStore().remove(model);
                view.getGird().getStore().insert(updateModel, index);
                isNotFound = false;
            }
        }
        if (isNotFound) {
            view.getGird().getStore().add(updateModel);
            view.getGird().getView().ensureVisible(view.getGird().getStore().getCount() - 1, 1, false);
        }
        view.getGird().getSelectionModel().select(updateModel, false);
    }

    private void resetFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getGird().getStore().getLoadConfig();
        loadConfig.set("hasFilter", false);
        loadConfig.set("filters", null);
        view.getTxtCodeSearch().clear();
        view.getTxtNameSearch().clear();
    }

    private void resetMaterialFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getMaterialGrid().
                getStore().getLoadConfig();
        loadConfig.set("hasFilter", false);
        loadConfig.set("filters", null);
        view.getTxtMaterialSearch().clear();
    }
}
