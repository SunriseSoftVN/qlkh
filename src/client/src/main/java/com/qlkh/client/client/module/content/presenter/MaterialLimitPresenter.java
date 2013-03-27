package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.MaterialLimitPlace;
import com.qlkh.client.client.module.content.presenter.share.AbstractTaskDetailPresenter;
import com.qlkh.client.client.module.content.view.MaterialLimitView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.action.material.LoadMaterialLimitAction;
import com.qlkh.core.client.action.material.LoadMaterialLimitResult;
import com.qlkh.core.client.action.task.LoadTaskHasLimitAction;
import com.qlkh.core.client.action.task.LoadTaskHasLimitResult;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.client.model.MaterialLimit;
import com.qlkh.core.client.model.Task;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
                List<Material> entities = new ArrayList<Material>();
                boolean isError = false;
                for (Record record : view.getSubTaskDetailGird().getStore().getModifiedRecords()) {
                    BeanModel model = (BeanModel) record.getModel();
                    Material material = model.getBean();
                    //validation
                    if (StringUtils.isNotBlank(material.getName()) && StringUtils.isNotBlank(material.getCode())
                            && StringUtils.isNotBlank(material.getUnit()) && material.getPrice() != null) {
                        entities.add(material);
                    } else {
                        isError = true;
                        break;
                    }
                }

                if (isError) {
                    DiaLogUtils.showMessage("Có lỗi xãy ra trong việc nhập dữ liệu, các mục mã, tên đơn vị, số luợng vật tư không đuợc bỏ trống");
                }

                if (!entities.isEmpty()) {
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
                materialEditWindow = view.createMaterialEditWindow(GridUtils.createListStore(Material.class));
                view.getMaterialPagingToolBar().bind((PagingLoader<?>) view.getMaterialGrid().getStore().getLoader());
                view.getMaterialPagingToolBar().refresh();
                materialEditWindow.show();
            }
        });

        view.getBtnMaterialAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                Material material = view.getMaterialGrid().getSelectionModel().getSelectedItem().getBean();

                boolean isFound = false;
                for (BeanModel model : view.getSubTaskDetailGird().getStore().getModels()) {
                    MaterialLimit materialLimit = model.getBean();
                    if (materialLimit.getMaterial().getId().equals(material.getId())) {
                        isFound = true;
                        break;
                    }
                }

                if (!isFound) {
                    MaterialLimit materialLimit = new MaterialLimit();
                    materialLimit.setMaterial(material);
                    materialLimit.setTask(currentTask);
                    materialLimit.setUpdateBy(1l);
                    materialLimit.setCreateBy(1l);

                    BeanModelFactory factory = BeanModelLookup.get().getFactory(MaterialLimit.class);
                    BeanModel insertModel = factory.createModel(materialLimit);
                    view.getSubTaskDetailGird().getStore().add(insertModel);
                    DiaLogUtils.notify(view.getConstant().addSuccess());
                } else {
                    DiaLogUtils.notify(view.getConstant().addAlready());
                }
            }
        });

        view.getBtnDeleteTaskMaterial().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                BeanModel model = view.getSubTaskDetailGird().getSelectionModel().getSelectedItem();
                view.getSubTaskDetailGird().getStore().remove(model);
            }
        });

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

        PagingLoader<PagingLoadResult<LoadTaskHasLimitResult>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<LoadTaskHasLimitResult>>(rpcProxy, new LoadGridDataReader()) {
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

    @Override
    protected void createSubTaskGrid() {
        view.createSubTaskGrid(createSubTaskListStore());
        view.getSubTaskPagingToolBar().bind((PagingLoader<?>) view.getSubTaskDetailGird().getStore().getLoader());
    }

}
