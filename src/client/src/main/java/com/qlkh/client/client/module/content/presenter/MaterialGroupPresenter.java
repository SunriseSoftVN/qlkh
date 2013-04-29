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
import com.qlkh.client.client.constant.ExceptionConstant;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.MaterialGroupPlace;
import com.qlkh.client.client.module.content.view.MaterialGroupView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.core.DeleteAction;
import com.qlkh.core.client.action.core.DeleteResult;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.model.MaterialGroup;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.shared.ServiceException;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MaterialGroupPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 3:46 AM
 */
@Presenter(view = MaterialGroupView.class, place = MaterialGroupPlace.class)
public class MaterialGroupPresenter extends AbstractPresenter<MaterialGroupView> {

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;
    private Window editWindow;
    private MaterialGroup materialGroup;

    @Override
    public void onActivate() {
        view.show();
        view.getPagingToolBar().refresh();
    }

    @Override
    protected void doBind() {
        view.createGrid(GridUtils.createListStore(MaterialGroup.class));
        view.getPagingToolBar().bind((PagingLoader<?>) view.getMaterialGroupGird().getStore().getLoader());

        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                editWindow = view.createEditWindow();
                editWindow.show();
                editWindow.layout(true);
                materialGroup = null;
            }
        });

        view.getBtnEdit().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                editWindow = view.createEditWindow();
                MaterialGroup selectedMaterial = view.getMaterialGroupGird().getSelectionModel().getSelectedItem().getBean();
                view.getTxtName().setValue(selectedMaterial.getName());
                view.getTxtCode().setValue(selectedMaterial.getCode());
                view.getTxtRegex().setValue(selectedMaterial.getRegex());
                view.getTxtCodeDisplay().setValue(selectedMaterial.getCodeDisplay());
                materialGroup = selectedMaterial;
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
                if (view.getEditPanel().isValid()) {
                    if (materialGroup == null) {
                        materialGroup = new MaterialGroup();
                        materialGroup.setCreateBy(1l);
                        materialGroup.setUpdateBy(1l);
                    }

                    materialGroup.setName(view.getTxtName().getValue());
                    materialGroup.setCode(view.getTxtCode().getValue());
                    materialGroup.setRegex(view.getTxtRegex().getValue());
                    materialGroup.setCodeDisplay(view.getTxtCodeDisplay().getValue());

                    dispatch.execute(new SaveAction(materialGroup), new AbstractAsyncCallback<SaveResult>() {
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
                                updateGrid(result.<MaterialGroup>getEntity());
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
                final List<BeanModel> models = view.getMaterialGroupGird().getSelectionModel().getSelectedItems();
                if (!models.isEmpty()) {
                    final List<Long> materialIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        MaterialGroup material = model.getBean();
                        materialIds.add(material.getId());
                    }

                    DiaLogUtils.conform(view.getConstant().deleteMessage(), new Listener<MessageBoxEvent>() {
                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                            if (be.getButtonClicked().getText().equals("Yes")) {
                                dispatch.execute(new DeleteAction(MaterialGroup.class.getName(), materialIds), new AbstractAsyncCallback<DeleteResult>() {
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

    private void updateGrid(MaterialGroup material) {
        boolean isNotFound = true;
        BeanModelFactory factory = BeanModelLookup.get().getFactory(MaterialGroup.class);
        BeanModel updateModel = factory.createModel(material);
        for (BeanModel model : view.getMaterialGroupGird().getStore().getModels()) {
            if (material.getId().equals(model.<MaterialGroup>getBean().getId())) {
                int index = view.getMaterialGroupGird().getStore().indexOf(model);
                view.getMaterialGroupGird().getStore().remove(model);
                view.getMaterialGroupGird().getStore().insert(updateModel, index);
                isNotFound = false;
            }
        }
        if (isNotFound) {
            view.getMaterialGroupGird().getStore().add(updateModel);
            view.getMaterialGroupGird().getView().ensureVisible(view.getMaterialGroupGird()
                    .getStore().getCount() - 1, 1, false);
        }
        view.getMaterialGroupGird().getSelectionModel().select(updateModel, false);
    }

}
