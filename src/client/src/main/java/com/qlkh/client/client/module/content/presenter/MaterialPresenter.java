package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.constant.ExceptionConstant;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.MaterialPlace;
import com.qlkh.client.client.module.content.view.MaterialView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.action.material.DeleteMaterialAction;
import com.qlkh.core.client.action.material.DeleteMaterialResult;
import com.qlkh.core.client.action.material.LoadMaterialAction;
import com.qlkh.core.client.action.material.LoadMaterialResult;
import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.client.model.MaterialPrice;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.shared.ServiceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class MaterialPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 3/25/13 1:34 PM
 */
@Presenter(view = MaterialView.class, place = MaterialPlace.class)
public class MaterialPresenter extends AbstractPresenter<MaterialView> {

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;

    private Window materialWindow;
    private Material currentMaterial;
    private int currentQuarter;
    private int currentYear;

    @Override
    public void onActivate() {
        view.show();
        if (currentYear > 0 && currentQuarter > 0) {
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

                view.setPriceRender(new GridCellRenderer<BeanModel>() {
                    @Override
                    public Object render(BeanModel beanModel, String s, ColumnData columnData, int i, int i2, ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
                        Material material = beanModel.getBean();
                        if (material.getCurrentPrice() != null) {
                            return material.getCurrentPrice().getPriceByQuarter(currentQuarter);
                        } else {
                            return null;
                        }
                    }
                });

                view.createGrid(createListStore());
                view.getPagingToolBar().bind((PagingLoader<?>) view.getMaterialGird().getStore().getLoader());
                view.getPagingToolBar().refresh();
            }
        });


        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                materialWindow = view.createMaterialEditWindow();
                materialWindow.show();
                materialWindow.layout(true);
            }
        });
        view.getBtnTaskEditOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                if (view.getMaterialEditPanel().isValid() && currentQuarter > 0 && currentYear > 0) {
                    if (currentMaterial == null) {
                        currentMaterial = new Material();
                        currentMaterial.setCreateBy(1l);
                        currentMaterial.setUpdateBy(1l);
                    }

                    if (currentMaterial.getCurrentPrice() == null) {
                        MaterialPrice price = new MaterialPrice();
                        price.setMaterial(currentMaterial);
                        price.setYear(currentYear);
                        currentMaterial.setCurrentPrice(price);
                    }

                    currentMaterial.getCurrentPrice().setPrice(view.getTxtPrice().getValue().doubleValue(), currentQuarter);

                    currentMaterial.setCode(view.getTxtCode().getValue());
                    currentMaterial.setName(view.getTxtName().getValue());
                    currentMaterial.setUnit(view.getTxtUnit().getValue());
                    currentMaterial.setNote(view.getTxtNote().getValue());

                    dispatch.execute(new SaveAction(currentMaterial), new AbstractAsyncCallback<SaveResult>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            if (caught instanceof ServiceException) {
                                String causeClassName = ((ServiceException) caught).getCauseClassname();
                                if (causeClassName.contains(ExceptionConstant.DATA_EXCEPTION)) {
                                    DiaLogUtils.showMessage(view.getConstant().existCodeMessage());
                                }
                            } else {
                                super.onFailure(caught);
                            }
                        }

                        @Override
                        public void onSuccess(SaveResult result) {
                            if (result.getEntity() != null) {
                                updateGrid(result.<Material>getEntity());
                                materialWindow.hide();
                                DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                            }
                        }
                    });
                }
            }
        });
        view.getBtnTaskEditCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                materialWindow.hide();
            }
        });
        view.getBtnEdit().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                materialWindow = view.createMaterialEditWindow();
                Material selectedMaterial = view.getMaterialGird().getSelectionModel().getSelectedItem().getBean();
                view.getTxtCode().setValue(selectedMaterial.getCode());
                view.getTxtName().setValue(selectedMaterial.getName());
                view.getTxtUnit().setValue(selectedMaterial.getUnit());
                view.getTxtNote().setValue(selectedMaterial.getNote());
                currentMaterial = selectedMaterial;
                materialWindow.show();
                materialWindow.layout(true);
            }
        });
        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());

        view.getTxtNameSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                String st = view.getTxtNameSearch().getValue();
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    if (StringUtils.isNotBlank(st)) {
                        view.getTxtCodeSearch().clear();
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getMaterialGird().
                                getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put("name", view.getTxtNameSearch().getValue());
                        loadConfig.set("filters", filters);
                    } else {
                        resetFilter();
                    }
                    view.getPagingToolBar().refresh();
                } else if (event.getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    resetFilter();
                    com.google.gwt.user.client.Timer timer = new Timer() {
                        @Override
                        public void run() {
                            view.getPagingToolBar().refresh();
                        }
                    };
                    timer.schedule(100);
                }
            }
        });

        view.getTxtCodeSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    String st = view.getTxtCodeSearch().getValue();
                    view.getTxtNameSearch().clear();
                    if (StringUtils.isNotBlank(st)) {
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getMaterialGird().
                                getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put("code", view.getTxtCodeSearch().getValue());
                        loadConfig.set("filters", filters);
                    } else {
                        resetFilter();
                    }
                    view.getPagingToolBar().refresh();
                } else if (event.getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    resetFilter();
                    com.google.gwt.user.client.Timer timer = new Timer() {
                        @Override
                        public void run() {
                            view.getPagingToolBar().refresh();
                        }
                    };
                    timer.schedule(100);
                }
            }
        });

        view.getBtnRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                resetFilter();
                view.getPagingToolBar().refresh();
            }
        });
    }

    private ListStore<BeanModel> createListStore() {
        RpcProxy<LoadMaterialResult> rpcProxy = new RpcProxy<LoadMaterialResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadMaterialResult> callback) {
                LoadMaterialAction loadAction = new LoadMaterialAction((BasePagingLoadConfig) loadConfig);
                StandardDispatchAsync.INSTANCE
                        .execute(loadAction, callback);
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

    private void updateGrid(Material material) {
        boolean isNotFound = true;
        BeanModelFactory factory = BeanModelLookup.get().getFactory(Material.class);
        BeanModel updateModel = factory.createModel(material);
        for (BeanModel model : view.getMaterialGird().getStore().getModels()) {
            if (material.getId().equals(model.<Material>getBean().getId())) {
                int index = view.getMaterialGird().getStore().indexOf(model);
                view.getMaterialGird().getStore().remove(model);
                view.getMaterialGird().getStore().insert(updateModel, index);
                isNotFound = false;
            }
        }
        if (isNotFound) {
            view.getMaterialGird().getStore().add(updateModel);
            view.getMaterialGird().getView().ensureVisible(view.getMaterialGird()
                    .getStore().getCount() - 1, 1, false);
        }
        view.getMaterialGird().getSelectionModel().select(updateModel, false);
    }

    private void resetFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getMaterialGird().
                getStore().getLoadConfig();
        loadConfig.set("hasFilter", false);
        loadConfig.set("filters", null);
        view.getTxtNameSearch().clear();
        view.getTxtCodeSearch().clear();
    }

    private class DeleteButtonEventListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            final List<BeanModel> models = view.getMaterialGird().getSelectionModel().getSelectedItems();
            if (CollectionsUtils.isNotEmpty(models)) {
                if (models.size() == 1) {
                    final Material material = (Material) models.get(0).getBean();
                    showDeleteTagConform(material.getId(), material.getName());
                } else {
                    List<Long> materialIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        final Material material = (Material) model.getBean();
                        materialIds.add(material.getId());
                    }
                    showDeleteTagConform(materialIds, null);
                }
            }
        }
    }

    private void showDeleteTagConform(long materialId, String tagName) {
        List<Long> materialIds = new ArrayList<Long>(1);
        materialIds.add(materialId);
        showDeleteTagConform(materialIds, tagName);
    }

    private void showDeleteTagConform(final List<Long> materialIds, String tagName) {
        assert materialIds != null;
        String deleteMessage;
        final AsyncCallback<DeleteMaterialResult> callback = new AbstractAsyncCallback<DeleteMaterialResult>() {
            @Override
            public void onSuccess(DeleteMaterialResult result) {
                if (result.isDeleted()) {
                    //Reload grid.
                    view.getPagingToolBar().refresh();
                    DiaLogUtils.notify(view.getConstant().deleteMessageSuccess());
                } else {
                    DiaLogUtils.conform(view.getConstant().deleteMessageError(), new Listener<MessageBoxEvent>() {
                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                            if (be.getButtonClicked().getText().equals("Yes")) {
                                dispatch.execute(new DeleteMaterialAction(materialIds, true), new AbstractAsyncCallback<DeleteMaterialResult>() {
                                    @Override
                                    public void onSuccess(DeleteMaterialResult result) {
                                        view.getPagingToolBar().refresh();
                                        DiaLogUtils.notify(view.getConstant().deleteMessageSuccess());
                                    }
                                });
                            }
                        }
                    });
                }
            }
        };
        final boolean hasManyTag = materialIds.size() > 1;
        if (hasManyTag) {
            deleteMessage = view.getConstant().deleteAllMessage();
        } else {
            deleteMessage = StringUtils.substitute(view.getConstant().deleteMessage(), tagName);
        }

        DiaLogUtils.conform(deleteMessage, new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked().getText().equals("Yes")) {
                    dispatch.execute(new DeleteMaterialAction(materialIds), callback);
                }
            }
        });
    }
}
