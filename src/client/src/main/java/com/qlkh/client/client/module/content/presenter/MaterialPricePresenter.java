package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Timer;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.MaterialPricePlace;
import com.qlkh.client.client.module.content.view.MaterialPriceView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.criterion.ClientRestrictions;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.client.model.MaterialPrice;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

                view.createGrid(GridUtils.createListStore(MaterialPrice.class, ClientRestrictions.eq("year", currentYear), ClientRestrictions.eq("quarter", currentQuarter.getCode())));
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
                    resetFilter();
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
                        filters.put(MaterialPriceView.MATERIAL_NAME_COLUMN, view.getTxtMaterialSearch().getValue());
                        filters.put(MaterialPriceView.MATERIAL_CODE_COLUMN, view.getTxtMaterialSearch().getValue());
                        loadConfig.set("filters", filters);
                    } else {
                        resetFilter();
                    }
                    view.getMaterialPagingToolBar().refresh();
                } else if (event.getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    resetFilter();
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
                List<BeanModel> materialModels = view.getMaterialGrid().getSelectionModel().getSelectedItems();

                for (BeanModel materialModel : materialModels) {
                    boolean isFound = false;
                    Material material = materialModel.getBean();
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
                        materialPrice.setQuarter(currentQuarter.getCode());
                        materialPrice.setYear(currentYear);
                        materialPrice.setUpdateBy(1l);
                        materialPrice.setCreateBy(1l);

                        BeanModelFactory factory = BeanModelLookup.get().getFactory(MaterialPrice.class);
                        BeanModel insertModel = factory.createModel(materialPrice);
                        view.getMaterialPriceGird().getStore().add(insertModel);
                        view.getMaterialPriceGird().getSelectionModel().select(insertModel, false);
                    }
                }
                DiaLogUtils.notify(view.getConstant().addSuccess());
            }
        });

        view.getBtnMaterialEditCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                materialEditWindow.hide();
            }
        });

        view.getBtnEdit().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                List<MaterialPrice> materialPrices = new ArrayList<MaterialPrice>();
                for (BeanModel model : view.getMaterialPriceGird().getStore().getModels()) {
                    MaterialPrice materialPrice = model.getBean();
                    materialPrices.add(materialPrice);
                }

                if(!materialPrices.isEmpty()) {
                    dispatch.execute(new SaveAction(materialPrices), new AbstractAsyncCallback<SaveResult>() {
                        @Override
                        public void onSuccess(SaveResult saveResult) {
                            DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                        }
                    });
                }
                view.getMaterialPricePagingToolBar().refresh();
            }
        });

        view.getBtnRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                view.getMaterialPricePagingToolBar().refresh();
            }
        });

        view.getTxtSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                String st = view.getTxtSearch().getValue();
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    if (StringUtils.isNotBlank(st)) {
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getMaterialPriceGird().
                                getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put(MaterialPriceView.NAME_COLUMN, view.getTxtSearch().getValue());
                        filters.put(MaterialPriceView.CODE_COLUMN, view.getTxtSearch().getValue());
                        loadConfig.set("filters", filters);
                    } else {
                        resetFilter();
                    }
                    view.getMaterialPricePagingToolBar().refresh();
                } else if (event.getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    resetFilter();
                    com.google.gwt.user.client.Timer timer = new Timer() {
                        @Override
                        public void run() {
                            view.getMaterialPricePagingToolBar().refresh();
                        }
                    };
                    timer.schedule(100);
                }
            }
        });
    }

    private void resetFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getMaterialPriceGird().
                getStore().getLoadConfig();
        loadConfig.set("hasFilter", false);
        loadConfig.set("filters", null);
        view.getTxtSearch().clear();
    }
}
