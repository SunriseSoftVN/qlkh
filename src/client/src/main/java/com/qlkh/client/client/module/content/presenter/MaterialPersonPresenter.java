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
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.MaterialPersonPlace;
import com.qlkh.client.client.module.content.view.MaterialPersonView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.core.DeleteAction;
import com.qlkh.core.client.action.core.DeleteResult;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.criterion.ClientRestrictions;
import com.qlkh.core.client.model.MaterialPerson;
import com.qlkh.core.client.model.Station;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MaterialPersonPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 3:20 AM
 */
@Presenter(view = MaterialPersonView.class, place = MaterialPersonPlace.class)
public class MaterialPersonPresenter extends AbstractPresenter<MaterialPersonView> {

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;
    private Window editWindow;
    private MaterialPerson currentMaterial;

    @Override
    public void onActivate() {
        view.show();
        view.getPagingToolBar().refresh();
    }

    @Override
    protected void doBind() {
        view.createGrid(GridUtils.createListStore(MaterialPerson.class));
        view.getPagingToolBar().bind((PagingLoader<?>) view.getMaterialPersonGird().getStore().getLoader());
        view.getCbStation().setStore(GridUtils.createListStoreForCb(Station.class, ClientRestrictions.ne("id", 27l)));

        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                editWindow = view.createEditWindow();
                editWindow.show();
                editWindow.layout(true);
                currentMaterial = null;
            }
        });

        view.getBtnEdit().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                editWindow = view.createEditWindow();
                MaterialPerson selectedMaterial = view.getMaterialPersonGird().getSelectionModel().getSelectedItem().getBean();
                BeanModelFactory factory = BeanModelLookup.get().getFactory(Station.class);
                view.getCbStation().setValue(factory.createModel(selectedMaterial.getStation()));
                view.getTxtPersonName().setValue(selectedMaterial.getPersonName());
                currentMaterial = selectedMaterial;
                editWindow.show();
                editWindow.layout(true);
            }
        });

        view.getBtnRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                view.getPagingToolBar().refresh();
            }
        });

        view.getBtnEditOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                if (view.getEditPanel().isValid() && view.getCbStation().getValue() != null) {
                    if (currentMaterial == null) {
                        currentMaterial = new MaterialPerson();
                        currentMaterial.setCreateBy(1l);
                        currentMaterial.setUpdateBy(1l);
                    }

                    currentMaterial.setPersonName(view.getTxtPersonName().getValue());
                    currentMaterial.setStation(view.getCbStation().getValue().<Station>getBean());

                    dispatch.execute(new SaveAction(currentMaterial), new AbstractAsyncCallback<SaveResult>() {
                        @Override
                        public void onSuccess(SaveResult result) {
                            if (result.getEntity() != null) {
                                updateGrid(result.<MaterialPerson>getEntity());
                                editWindow.hide();
                                DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                            }
                        }
                    });
                }
            }
        });
        view.getBtnEditCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                editWindow.hide();
            }
        });

        view.getBtnDelete().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                final List<BeanModel> models = view.getMaterialPersonGird().getSelectionModel().getSelectedItems();
                if (!models.isEmpty()) {
                    final List<Long> materialIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        MaterialPerson material = model.getBean();
                        materialIds.add(material.getId());
                    }

                    DiaLogUtils.conform(view.getConstant().deleteMessage(), new Listener<MessageBoxEvent>() {
                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                            if (be.getButtonClicked().getText().equals("Yes")) {
                                dispatch.execute(new DeleteAction(MaterialPerson.class.getName(), materialIds), new AbstractAsyncCallback<DeleteResult>() {
                                    @Override
                                    public void onSuccess(DeleteResult deleteResult) {
                                        DiaLogUtils.notify(view.getConstant().deleteSuccessMessage());
                                        view.getPagingToolBar().refresh();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }

    private void updateGrid(MaterialPerson material) {
        boolean isNotFound = true;
        BeanModelFactory factory = BeanModelLookup.get().getFactory(MaterialPerson.class);
        BeanModel updateModel = factory.createModel(material);
        for (BeanModel model : view.getMaterialPersonGird().getStore().getModels()) {
            if (material.getId().equals(model.<MaterialPerson>getBean().getId())) {
                int index = view.getMaterialPersonGird().getStore().indexOf(model);
                view.getMaterialPersonGird().getStore().remove(model);
                view.getMaterialPersonGird().getStore().insert(updateModel, index);
                isNotFound = false;
            }
        }
        if (isNotFound) {
            view.getMaterialPersonGird().getStore().add(updateModel);
            view.getMaterialPersonGird().getView().ensureVisible(view.getMaterialPersonGird()
                    .getStore().getCount() - 1, 1, false);
        }
        view.getMaterialPersonGird().getSelectionModel().select(updateModel, false);
    }


}
