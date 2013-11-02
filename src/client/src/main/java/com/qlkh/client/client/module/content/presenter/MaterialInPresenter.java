package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.constant.ExceptionConstant;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.MaterialInPlace;
import com.qlkh.client.client.module.content.view.MaterialInView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.core.*;
import com.qlkh.core.client.action.grid.LoadGridDataAction;
import com.qlkh.core.client.action.grid.LoadGridDataResult;
import com.qlkh.core.client.action.material.*;
import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.criterion.ClientCriteria;
import com.qlkh.core.client.criterion.ClientRestrictions;
import com.qlkh.core.client.dto.GroupStationDto;
import com.qlkh.core.client.model.*;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.shared.ServiceException;

import java.util.*;

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
    private Group currentGroup;
    private List<Station> stations = new ArrayList<Station>();
    private List<Group> groups = new ArrayList<Group>();

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
                final BeanModelFactory factory = BeanModelLookup.get().getFactory(GroupStationDto.class);
                final ListStore<BeanModel> store = new ListStore<BeanModel>();
                view.getCbStation().setStore(store);
                dispatch.execute(new LoadAction(Station.class.getName()),
                        new AbstractAsyncCallback<LoadResult>() {
                            @Override
                            public void onSuccess(LoadResult result) {
                                for (Station entity : result.<Station>getList()) {
                                    GroupStationDto dto = new GroupStationDto();
                                    dto.setName(entity.getName());
                                    dto.setId(entity.getId());
                                    store.add(factory.createModel(dto));
                                    stations.add(entity);
                                }
                                dispatch.execute(new LoadAction(Group.class.getName()), new AbstractAsyncCallback<LoadResult>() {
                                    @Override
                                    public void onSuccess(LoadResult result) {
                                        for (Group group : result.<Group>getList()) {
                                            GroupStationDto dto = new GroupStationDto();
                                            dto.setName(group.getName());
                                            dto.setId(group.getId());
                                            dto.setStation(false);
                                            store.add(factory.createModel(dto));
                                            groups.add(group);
                                        }

                                        if (!stations.isEmpty()) {
                                            view.getCbStation().setValue(store.getAt(0));
                                            currentStation = getCurrentStation();
                                            view.getBtnCopy().setEnabled(false);
                                            view.createGrid(createGridStore());
                                            view.getPagingToolBar().bind((PagingLoader<?>) view.getGird().getStore().getLoader());
                                            view.getPagingToolBar().refresh();
                                            view.getCbQuarter().setSimpleValue(currentQuarter);
                                            view.getCbYear().setSimpleValue(currentYear);

                                            view.getCbStation().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
                                                @Override
                                                public void selectionChanged(SelectionChangedEvent<BeanModel> beanModelSelectionChangedEvent) {
                                                    currentStation = getCurrentStation();
                                                    currentGroup = getCurrentGroup();
                                                    if (currentStation != null) {
                                                        if (currentStation.isCompany()) {
                                                            view.getBtnCopy().setEnabled(false);
                                                        } else {
                                                            view.getBtnCopy().setEnabled(true);
                                                        }
                                                    }
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

                                            view.getCbYear().addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<Integer>>() {
                                                @Override
                                                public void selectionChanged(SelectionChangedEvent<SimpleComboValue<Integer>> simpleComboValueSelectionChangedEvent) {
                                                    currentYear = view.getCbYear().getSimpleValue();
                                                    view.getPagingToolBar().refresh();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
            }
        });

        view.getCbGroup().setStore(GridUtils.createListStoreForCb(MaterialGroup.class));

        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {

                view.getCbPerson().clearSelections();
                ClientCriteria criteria = createCriteria();
                if (criteria == null) {
                    view.getCbPerson().setStore(GridUtils.createListStoreForCb(MaterialPerson.class));
                } else {
                    view.getCbPerson().setStore(GridUtils.createListStoreForCb(MaterialPerson.class, criteria));
                }

                currentMaterial = new MaterialIn();
                currentMaterial.setCreateBy(1l);
                currentMaterial.setUpdateBy(1l);

                editWindow = view.createEditWindow(GridUtils.createListStore(Material.class));
                view.resetEditPanel();

                view.getMaterialPagingToolBar().bind((PagingLoader<?>) view.getMaterialGrid().getStore().getLoader());
                if (view.getMaterialGrid().getStore().getLoadConfig() != null) {
                    resetMaterialFilter();
                }
                view.getMaterialPagingToolBar().refresh();
                view.getMaterialGrid().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
                    @Override
                    public void selectionChanged(SelectionChangedEvent<BeanModel> event) {
                        if (event.getSelectedItem() != null && currentStation != null && !currentStation.isCompany()) {
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

                dispatch.execute(new MaterialInGetNextCodeAction(), new AbstractAsyncCallback<MaterialInGetNextCodeResult>() {
                    @Override
                    public void onSuccess(MaterialInGetNextCodeResult result) {
                        view.getTxtCode().setValue(result.getCode());
                        editWindow.show();
                    }
                });
            }
        });

        view.getBtnEdit().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                if (view.getGird().getSelectionModel().getSelectedItem() != null) {

                    ClientCriteria criteria = createCriteria();
                    if (criteria == null) {
                        view.getCbPerson().setStore(GridUtils.createListStoreForCb(MaterialPerson.class));
                    } else {
                        view.getCbPerson().setStore(GridUtils.createListStoreForCb(MaterialPerson.class, criteria));
                    }

                    dispatch.execute(new MaterialInGetNextCodeAction(), new AbstractAsyncCallback<MaterialInGetNextCodeResult>() {
                        @Override
                        public void onSuccess(MaterialInGetNextCodeResult result) {
                            MaterialIn selectedMaterial = view.getGird().getSelectionModel().getSelectedItem().getBean();
                            editWindow = view.createEditWindow(null);
                            currentMaterial = selectedMaterial;

                            if (currentMaterial.getCode() != null) {
                                view.getTxtCode().setValue(currentMaterial.getCode());
                            } else {
                                view.getTxtCode().setValue(result.getCode());
                            }

                            view.getTxtTotal().setValue(currentMaterial.getTotal());
                            view.getExportDate().setValue(currentMaterial.getExportDate());

                            if (currentMaterial.getWeight() != null) {
                                view.getTxtWeight().setValue(currentMaterial.getWeight());
                            }

                            BeanModelFactory groupFactory = BeanModelLookup.get().getFactory(MaterialGroup.class);
                            BeanModelFactory personFactory = BeanModelLookup.get().getFactory(MaterialPerson.class);

                            if (currentMaterial.getMaterialGroup() != null) {
                                BeanModel group = groupFactory.createModel(currentMaterial.getMaterialGroup());
                                view.getCbGroup().setValue(group);
                            } else {
                                view.getCbGroup().clear();
                            }

                            if (currentMaterial.getMaterialPerson() != null) {
                                BeanModel person = personFactory.createModel(currentMaterial.getMaterialPerson());
                                view.getCbPerson().setValue(person);
                                view.getCbPerson().clearInvalid();
                            } else {
                                view.getCbPerson().clear();
                            }

                            editWindow.show();
                            editWindow.layout(true);
                        }
                    });
                }
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
                view.resetEditPanel();
                editWindow.hide();
            }
        });

        view.getBtnEditOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                if (view.getEditPanel().isValid()) {
                    Material material = null;
                    if (currentMaterial != null && currentMaterial.getMaterial() != null) {
                        material = currentMaterial.getMaterial();
                    } else if (view.getMaterialGrid().getSelectionModel().getSelectedItem() != null) {
                        material = view.getMaterialGrid().getSelectionModel().getSelectedItem().getBean();
                    }

                    if (material != null) {
                        double total = view.getTxtTotal().getValue().doubleValue();

                        MaterialPerson materialPerson = view.getCbPerson().getValue().getBean();
                        MaterialGroup materialGroup = view.getCbGroup().getValue().getBean();

                        if (currentMaterial != null) {
                            currentMaterial.setMaterial(material);
                            currentMaterial.setMaterialGroup(materialGroup);
                            currentMaterial.setMaterialPerson(materialPerson);
                            currentMaterial.setTotal(total);

                            if (view.getTxtWeight().getValue() != null) {
                                double weight = view.getTxtWeight().getValue().doubleValue();
                                currentMaterial.setWeight(weight);
                            }

                            currentMaterial.setCreatedDate(new Date());
                            currentMaterial.setStation(materialPerson.getStation());
                            if (materialPerson.getGroup() != null) {
                                currentMaterial.setGroup(materialPerson.getGroup());
                            }
                            currentMaterial.setYear(currentYear);
                            currentMaterial.setQuarter(currentQuarter.getCode());
                            currentMaterial.setCode(view.getTxtCode().getValue().intValue());
                            currentMaterial.setExportDate(view.getExportDate().getValue());

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
                                public void onSuccess(SaveResult saveResult) {
                                    editWindow.hide();
                                    view.resetEditPanel();
                                    view.getPagingToolBar().refresh();
                                }
                            });
                        }

                    } else {
                        DiaLogUtils.showMessage(view.getConstant().materialError());
                    }
                }
            }
        });

        view.getTxtCodeSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    view.getTxtNameSearch().clear();
                    if (view.getTxtCodeSearch().getValue() != null) {
                        Integer code = view.getTxtCodeSearch().getValue().intValue();
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getGird().getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put("code", code);
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

        view.getTxtNameSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    String st = view.getTxtNameSearch().getValue();
                    view.getTxtCodeSearch().clear();
                    if (StringUtils.isNotBlank(st)) {
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getGird().getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put("material.name", st);
                        filters.put("material.code", st);
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

        view.getTxtMaterialSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    String st = view.getTxtMaterialSearch().getValue();
                    if (StringUtils.isNotBlank(st)) {
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getMaterialGrid().getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put("name", st);
                        filters.put("code", st);
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

        view.getBtnDelete().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                final List<BeanModel> models = view.getGird().getSelectionModel().getSelectedItems();
                if (!models.isEmpty()) {
                    final List<Long> materialIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        MaterialIn material = model.getBean();
                        materialIds.add(material.getId());
                    }

                    DiaLogUtils.conform(view.getConstant().deleteMessage(), new Listener<MessageBoxEvent>() {
                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                            if (be.getButtonClicked().getText().equals("Yes")) {
                                dispatch.execute(new DeleteAction(MaterialIn.class.getName(), materialIds), new AbstractAsyncCallback<DeleteResult>() {
                                    @Override
                                    public void onSuccess(DeleteResult deleteResult) {
                                        DiaLogUtils.notify(view.getConstant().deleteMessageSuccess());
                                        view.getPagingToolBar().refresh();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        view.getBtnCopy().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                if (currentStation != null) {
                    dispatch.execute(new CopyMaterialInAction(currentYear, currentQuarter.getCode(), currentStation.getId()),
                            new AbstractAsyncCallback<CopyMaterialInResult>() {
                                @Override
                                public void onSuccess(CopyMaterialInResult copyMaterialInResult) {
                                    view.getPagingToolBar().refresh();
                                }
                            });
                }
            }
        });
    }

    private ListStore<BeanModel> createGridStore() {
        RpcProxy<LoadGridDataResult> rpcProxy = new RpcProxy<LoadGridDataResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadGridDataResult> callback) {
                LoadGridDataAction loadAction;

                ClientCriteria criteria = createCriteria();

                if (criteria != null) {
                    loadAction = new LoadGridDataAction(MaterialIn.class.getName(),
                            ClientRestrictions.eq("year", currentYear),
                            ClientRestrictions.eq("quarter", currentQuarter.getCode()), criteria);
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


    private ClientCriteria createCriteria() {
        ClientCriteria criteria = null;
        if (currentStation != null) {
            if (!currentStation.isCompany()) {
                criteria = ClientRestrictions.eq("station.id", currentStation.getId());
            }
        }
        if (currentGroup != null) {
            criteria = ClientRestrictions.eq("group.id", currentGroup.getId());
        }
        return criteria;
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

    private Station getCurrentStation() {
        if (view.getCbStation().getValue() != null) {
            for (Station station : stations) {
                GroupStationDto dto = view.getCbStation().getValue().getBean();
                if (dto.isStation() && station.getId().equals(dto.getId())) {
                    return station;
                }
            }
        }
        return null;
    }

    private Group getCurrentGroup() {
        if (view.getCbStation().getValue() != null) {
            for (Group group : groups) {
                GroupStationDto dto = view.getCbStation().getValue().getBean();
                if (!dto.isStation() && group.getId().equals(dto.getId())) {
                    return group;
                }
            }
        }
        return null;
    }
}
