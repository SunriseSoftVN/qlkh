package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.MaterialLimitPlace;
import com.qlkh.client.client.module.content.presenter.share.AbstractTaskDetailPresenter;
import com.qlkh.client.client.module.content.view.MaterialLimitView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.core.client.action.core.DeleteAction;
import com.qlkh.core.client.action.core.DeleteResult;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.action.material.LoadMaterialLimitAction;
import com.qlkh.core.client.action.material.LoadMaterialLimitResult;
import com.qlkh.core.client.action.material.LoadMaterialWithoutLimitAction;
import com.qlkh.core.client.action.material.LoadMaterialWithoutLimitResult;
import com.qlkh.core.client.action.task.LoadTaskHasLimitAction;
import com.qlkh.core.client.action.task.LoadTaskHasLimitResult;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.client.model.MaterialLimit;
import com.qlkh.core.client.model.Task;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class LimitJobPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 10:10 AM
 */
@Presenter(view = MaterialLimitView.class, place = MaterialLimitPlace.class)
public class MaterialLimitPresenter extends AbstractTaskDetailPresenter<MaterialLimitView> {

    private Window materialEditWindow;

    @Override
    protected void doBind() {
        super.doBind();
        view.getBtnSubTaskSave().removeAllListeners();
        view.getBtnSubTaskSave().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                List<MaterialLimit> entities = new ArrayList<MaterialLimit>();
                boolean isError = false;
                for (BeanModel model : view.getSubTaskDetailGird().getStore().getModels()) {
                    MaterialLimit materialLimit = model.getBean();
                    //validation
                    if (materialLimit.getQuantity() != null) {
                        entities.add(materialLimit);
                    } else {
                        isError = true;
                        break;
                    }
                }

                if (isError) {
                    DiaLogUtils.showMessage("Có lỗi xãy ra trong việc nhập dữ liệu, số luợng vật tư không đuợc bỏ trống");
                } else if (!entities.isEmpty()) {
                    dispatch.execute(new SaveAction(entities), new AbstractAsyncCallback<SaveResult>() {
                        @Override
                        public void onSuccess(SaveResult result) {
                            DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                            view.getSubTaskPagingToolBar().refresh();
                        }
                    });
                }
            }
        });

        view.getBtnMaterialTaskAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                if (currentTask != null) {
                    materialEditWindow = view.createMaterialEditWindow(createMaterialStore());
                    view.getMaterialPagingToolBar().bind((PagingLoader<?>) view.getMaterialGrid().getStore().getLoader());
                    if (view.getMaterialGrid().getStore().getLoadConfig() != null) {
                        resetMaterialFilter();
                    }
                    view.getMaterialPagingToolBar().refresh();
                    materialEditWindow.show();
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
                    for (BeanModel model : view.getSubTaskDetailGird().getStore().getModels()) {
                        MaterialLimit materialLimit = model.getBean();
                        if (materialLimit.getMaterial().getId().equals(material.getId())) {
                            isFound = true;
                            break;
                        }
                    }

                    if (!isFound) {
                        if (currentTask != null) {
                            MaterialLimit materialLimit = new MaterialLimit();
                            materialLimit.setMaterial(material);
                            materialLimit.setTask(currentTask);
                            materialLimit.setUpdateBy(1l);
                            materialLimit.setCreateBy(1l);

                            BeanModelFactory factory = BeanModelLookup.get().getFactory(MaterialLimit.class);
                            BeanModel insertModel = factory.createModel(materialLimit);
                            view.getSubTaskDetailGird().getStore().add(insertModel);
                            view.getSubTaskDetailGird().getSelectionModel().select(insertModel, false);
                        }
                    }
                }
                DiaLogUtils.notify(view.getConstant().addSuccess());
            }
        });

        view.getBtnDeleteTaskMaterial().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                final BeanModel model = view.getSubTaskDetailGird().getSelectionModel().getSelectedItem();
                if (model != null) {
                    final MaterialLimit materialLimit = model.getBean();
                    if (materialLimit.getId() == null) {
                        view.getSubTaskDetailGird().getStore().remove(model);
                    } else {
                        DiaLogUtils.conform(StringUtils.substitute(view.getConstant().deleteMaterial(), materialLimit.getMaterial().getName()), new Listener<MessageBoxEvent>() {
                            @Override
                            public void handleEvent(MessageBoxEvent event) {
                                if (event.getButtonClicked().getText().equals("Yes")) {
                                    dispatch.execute(new DeleteAction(materialLimit), new AbstractAsyncCallback<DeleteResult>() {
                                        @Override
                                        public void onSuccess(DeleteResult deleteResult) {
                                            DiaLogUtils.notify(view.getConstant().deleteSuccess());
                                            view.getSubTaskDetailGird().getStore().remove(model);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });

        view.getBtnMaterialEditCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                materialEditWindow.hide();
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

    }

    private void resetMaterialFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getMaterialGrid().
                getStore().getLoadConfig();
        loadConfig.set("hasFilter", false);
        loadConfig.set("filters", null);
        view.getTxtMaterialSearch().clear();
    }

    @Override
    protected ListStore<BeanModel> createSubTaskListStore() {
        RpcProxy<LoadMaterialLimitResult> rpcProxy = new RpcProxy<LoadMaterialLimitResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadMaterialLimitResult> callback) {
                long currentTaskId = -1;
                if (currentTask != null) {
                    currentTaskId = currentTask.getId();
                }
                dispatch.execute(new LoadMaterialLimitAction((BasePagingLoadConfig) loadConfig, currentTaskId), callback);
            }
        };

        PagingLoader<PagingLoadResult<LoadMaterialLimitResult>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<LoadMaterialLimitResult>>(rpcProxy, new LoadGridDataReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<BeanModel>(pagingLoader);
    }

    @Override
    protected void createTaskGrid() {
        RpcProxy<LoadTaskHasLimitResult> rpcProxy = new RpcProxy<LoadTaskHasLimitResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadTaskHasLimitResult> callback) {
                dispatch.execute(new LoadTaskHasLimitAction(view.getCbShowTaskHasLimit().getValue(),
                        view.getCbShowTaskHasNoLimit().getValue(), (BasePagingLoadConfig) loadConfig), callback);
            }
        };

        PagingLoader<PagingLoadResult<Task>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<Task>>(rpcProxy, new LoadGridDataReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        view.createTaskGrid(new ListStore<BeanModel>(pagingLoader));
        view.getTaskPagingToolBar().bind((PagingLoader<?>) view.getTaskGird().getStore().getLoader());

        resetView();

        view.getTaskGird().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
                int index = se.getSelection().size() - 1;
                if (index >= 0) {
                    Task task = se.getSelection().get(index).getBean();
                    if (task.getId() != null) {
                        currentTask = task;
                        view.getSubTaskPagingToolBar().refresh();
                    } else {
                        emptySubGird();
                    }
                }
            }
        });
        view.getTaskGird().focus();

        createSubTaskGrid();
    }

    private ListStore<BeanModel> createMaterialStore() {
        RpcProxy<LoadMaterialWithoutLimitResult> rpcProxy = new RpcProxy<LoadMaterialWithoutLimitResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadMaterialWithoutLimitResult> callback) {
                long currentTaskId = -1;
                if (currentTask != null) {
                    currentTaskId = currentTask.getId();
                }
                dispatch.execute(new LoadMaterialWithoutLimitAction((BasePagingLoadConfig) loadConfig, currentTaskId), callback);
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

    @Override
    protected void createSubTaskGrid() {
        view.createSubTaskGrid(createSubTaskListStore());
        view.getSubTaskPagingToolBar().bind((PagingLoader<?>) view.getSubTaskDetailGird().getStore().getLoader());
    }

}
