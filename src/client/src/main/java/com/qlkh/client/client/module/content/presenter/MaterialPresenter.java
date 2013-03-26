package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.MaterialPlace;
import com.qlkh.client.client.module.content.view.MaterialView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.action.material.DeleteMaterialAction;
import com.qlkh.core.client.action.material.DeleteMaterialResult;
import com.qlkh.core.client.action.task.DeleteTaskAction;
import com.qlkh.core.client.action.task.DeleteTaskResult;
import com.qlkh.core.client.model.Material;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onActivate() {
        view.show();
        view.getPagingToolBar().refresh();
    }

    @Override
    protected void doBind() {
        view.createGrid(GridUtils.createListStore(Material.class));
        view.getPagingToolBar().bind((PagingLoader<?>) view.getMaterialGird().getStore().getLoader());
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
                if (view.getMaterialEditPanel().isValid()) {
                    if (currentMaterial == null) {
                        currentMaterial = new Material();
                        currentMaterial.setCreateBy(1l);
                        currentMaterial.setUpdateBy(1l);
                    }

                    currentMaterial.setCode(view.getTxtCode().getValue());
                    currentMaterial.setName(view.getTxtName().getValue());
                    currentMaterial.setUnit(view.getTxtUnit().getValue());
                    currentMaterial.setNote(view.getTxtNote().getValue());
                    currentMaterial.setPrice(view.getTxtPrice().getValue().doubleValue());

                    dispatch.execute(new SaveAction(currentMaterial), new AbstractAsyncCallback<SaveResult>() {
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
                view.getTxtPrice().setValue(selectedMaterial.getPrice());
                view.getTxtNote().setValue(selectedMaterial.getNote());
                currentMaterial = selectedMaterial;
                materialWindow.show();
                materialWindow.layout(true);
            }
        });
        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
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
                    DiaLogUtils.conform(view.getConstant().deleteMessage(), new Listener<MessageBoxEvent>() {
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
