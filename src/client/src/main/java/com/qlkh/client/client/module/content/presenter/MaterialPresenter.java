package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.MaterialPlace;
import com.qlkh.client.client.module.content.view.MaterialView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.client.client.utils.NumberUtils;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.client.model.Task;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import net.customware.gwt.dispatch.client.DispatchAsync;

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
        view.getPagingToolBar().bind((PagingLoader<?>) view.getTaskGird().getStore().getLoader());
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
                                updateGrid(currentMaterial);
                                materialWindow.hide();
                                DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateGrid(Material material) {
        boolean isNotFound = true;
        BeanModelFactory factory = BeanModelLookup.get().getFactory(Material.class);
        BeanModel updateModel = factory.createModel(material);
        for (BeanModel model : view.getTaskGird().getStore().getModels()) {
            if (material.getId().equals(model.<Material>getBean().getId())) {
                int index = view.getTaskGird().getStore().indexOf(model);
                view.getTaskGird().getStore().remove(model);
                view.getTaskGird().getStore().insert(updateModel, index);
                isNotFound = false;
            }
        }
        if (isNotFound) {
            view.getTaskGird().getStore().add(updateModel);
            view.getTaskGird().getView().ensureVisible(view.getTaskGird()
                    .getStore().getCount() - 1, 1, false);
        }
        view.getTaskGird().getSelectionModel().select(updateModel, false);
    }
}
