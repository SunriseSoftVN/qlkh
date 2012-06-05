/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.qlvt.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.client.client.core.dispatch.StandardDispatchAsync;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.content.place.BranchManagerPlace;
import com.qlvt.client.client.module.content.view.BranchManagerView;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.client.client.utils.GridUtils;
import com.qlvt.core.client.action.core.DeleteAction;
import com.qlvt.core.client.action.core.DeleteResult;
import com.qlvt.core.client.action.core.SaveAction;
import com.qlvt.core.client.action.core.SaveResult;
import com.qlvt.core.client.model.Branch;
import com.qlvt.core.client.model.Station;
import com.qlvt.core.client.model.SubTaskAnnualDetail;
import com.qlvt.core.client.model.SubTaskDetail;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class BranchManagerPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 1/2/12, 1:07 PM
 */
@Presenter(view = BranchManagerView.class, place = BranchManagerPlace.class)
public class BranchManagerPresenter extends AbstractPresenter<BranchManagerView> {

    private static final String[] RELATE_ENTITY_NAMES = {SubTaskDetail.class.getName(), SubTaskAnnualDetail.class.getName()};

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;

    private ListStore<BeanModel> stationListStore;
    private Branch currentBranch;
    private Window branchEditWindow;

    @Override
    public void onActivate() {
        view.show();
        if (stationListStore != null) {
            //reload stations list.
            stationListStore = GridUtils.createListStoreForCb(Station.class);
            view.getCbbStation().setStore(stationListStore);
        }
        view.getPagingToolBar().refresh();
        view.getBranchsGird().focus();
    }

    @Override
    protected void doBind() {
        stationListStore = GridUtils.createListStoreForCb(Station.class);
        view.getCbbStation().setStore(stationListStore);
        view.createGrid(GridUtils.createListStore(Branch.class));
        view.getPagingToolBar().bind((PagingLoader<?>) view.getBranchsGird().getStore().getLoader());
        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
        view.getBtnRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                view.getPagingToolBar().refresh();
            }
        });
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                branchEditWindow = view.createBranchEditWindow();
                currentBranch = new Branch();
                branchEditWindow.show();
            }
        });
        view.getBtnEdit().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (view.getBranchsGird().getSelectionModel().getSelectedItem() != null) {
                    Branch selectBranch = view.getBranchsGird().getSelectionModel().getSelectedItem().getBean();
                    currentBranch = selectBranch;
                    if (selectBranch != null) {
                        view.getTxtBranchName().setValue(currentBranch.getName());
                        BeanModel station = null;
                        for (BeanModel model : view.getCbbStation().getStore().getModels()) {
                            if (currentBranch.getStation().getId().
                                    equals(model.<Station>getBean().getId())) {
                                station = model;
                            }
                        }
                        view.getCbbStation().setValue(station);

                        branchEditWindow = view.createBranchEditWindow();
                        branchEditWindow.show();
                    }
                }
            }
        });
        view.getBtnBranchEditOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (view.getBranchEditPanel().isValid() && currentBranch != null) {
                    currentBranch.setName(view.getTxtBranchName().getValue());
                    currentBranch.setStation(view.getCbbStation().getValue().<Station>getBean());
                    currentBranch.setUpdateBy(1l);
                    currentBranch.setCreateBy(1l);
                    dispatch.execute(new SaveAction(currentBranch), new AbstractAsyncCallback<SaveResult>() {
                        @Override
                        public void onSuccess(SaveResult result) {
                            DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                            branchEditWindow.hide();
                            updateGrid(result.<Branch>getEntity());
                        }
                    });
                }
            }
        });
        view.getBtnBranchEditCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                branchEditWindow.hide();
            }
        });
    }

    private void updateGrid(Branch branch) {
        boolean isNotFound = true;
        BeanModelFactory factory = BeanModelLookup.get().getFactory(Branch.class);
        BeanModel updateModel = factory.createModel(branch);
        for (BeanModel model : view.getBranchsGird().getStore().getModels()) {
            if (branch.getId().equals(model.<Branch>getBean().getId())) {
                int index = view.getBranchsGird().getStore().indexOf(model);
                view.getBranchsGird().getStore().remove(model);
                view.getBranchsGird().getStore().insert(updateModel, index);
                isNotFound = false;
            }
        }
        if (isNotFound) {
            view.getBranchsGird().getStore().add(updateModel);
            view.getBranchsGird().getView().ensureVisible(view.getBranchsGird()
                    .getStore().getCount() - 1, 1, false);
        }
        view.getBranchsGird().getSelectionModel().select(updateModel, false);
    }

    private class DeleteButtonEventListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            final List<BeanModel> models = view.getBranchsGird().getSelectionModel().getSelectedItems();
            if (CollectionsUtils.isNotEmpty(models)) {
                if (models.size() == 1) {
                    final Branch branch = (Branch) models.get(0).getBean();
                    showDeleteTagConform(branch.getId(), branch.getName());
                } else {
                    List<Long> branchIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        final Branch branch = (Branch) model.getBean();
                        branchIds.add(branch.getId());
                    }
                    showDeleteTagConform(branchIds, null);
                }
            }
        }
    }

    private void showDeleteTagConform(long branchId, String tagName) {
        List<Long> branchIds = new ArrayList<Long>(1);
        branchIds.add(branchId);
        showDeleteTagConform(branchIds, tagName);
    }

    private void showDeleteTagConform(final List<Long> branchIds, String tagName) {
        assert branchIds != null;
        String deleteMessage;
        final AsyncCallback<DeleteResult> callback = new AbstractAsyncCallback<DeleteResult>() {
            @Override
            public void onSuccess(DeleteResult result) {
                if (result.isResult()) {
                    //Reload grid.
                    view.getPagingToolBar().refresh();
                    DiaLogUtils.notify(view.getConstant().deleteBranchMessageSuccess());
                } else {
                    DiaLogUtils.showMessage(view.getConstant().deleteErrorMessage());
                }
            }
        };
        final boolean hasManyTag = branchIds.size() > 1;
        if (hasManyTag) {
            deleteMessage = view.getConstant().deleteAllBranchMessage();
        } else {
            deleteMessage = StringUtils.substitute(view.getConstant().deleteBranchMessage(), tagName);
        }

        DiaLogUtils.conform(deleteMessage, new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked().getText().equals("Yes")) {
                    if (hasManyTag) {
                        dispatch.execute(new DeleteAction(Branch.class.getName(), branchIds, RELATE_ENTITY_NAMES), callback);
                    } else {
                        dispatch.execute(new DeleteAction(Branch.class.getName(), branchIds.get(0), RELATE_ENTITY_NAMES), callback);
                    }
                }
            }
        });
    }

    @Override
    public String mayStop() {
        if (branchEditWindow != null && branchEditWindow.isVisible()) {
            return view.getConstant().conformExitMessage();
        }
        return null;
    }

    @Override
    public void onCancel() {
        if (branchEditWindow != null && branchEditWindow.isVisible()) {
            branchEditWindow.hide();
        }
    }
}
