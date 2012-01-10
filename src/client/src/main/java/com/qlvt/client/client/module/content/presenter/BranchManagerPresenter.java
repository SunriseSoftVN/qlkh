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

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.content.place.BranchManagerPlace;
import com.qlvt.client.client.module.content.view.BranchManagerView;
import com.qlvt.client.client.module.content.view.StationManagerView;
import com.qlvt.client.client.service.BranchService;
import com.qlvt.client.client.service.BranchServiceAsync;
import com.qlvt.client.client.service.StationService;
import com.qlvt.client.client.service.StationServiceAsync;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.client.client.utils.LoadingUtils;
import com.qlvt.core.client.exception.DeleteException;
import com.qlvt.core.client.model.Branch;
import com.qlvt.core.client.model.Station;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Class BranchManagerPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 1/2/12, 1:07 PM
 */
@Presenter(view = BranchManagerView.class, place = BranchManagerPlace.class)
public class BranchManagerPresenter extends AbstractPresenter<BranchManagerView> {

    private BranchServiceAsync branchService = BranchService.App.getInstance();
    private StationServiceAsync stationService = StationService.App.getInstance();

    private ListStore<BeanModel> stationListStore;

    @Override
    public void onActivate() {
        view.show();
        if (stationListStore != null) {
            //reload stations list.
            stationListStore.removeAll();
            stationListStore.add(createUserListStore().getModels());
        }
        view.getPagingToolBar().refresh();
    }

    @Override
    protected void doBind() {
        stationListStore = createStationListStore();
        view.setStationCellEditor(createStationCellEditor());
        view.createGrid(createUserListStore());
        view.getPagingToolBar().bind((PagingLoader<?>) view.getBranchsGird().getStore().getLoader());
        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
        view.getBtnCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                view.getPagingToolBar().refresh();
            }
        });
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                Branch branch = new Branch();
                branch.setCreatedDate(new Date());
                branch.setUpdatedDate(new Date());
                branch.setCreateBy(1l);
                branch.setUpdateBy(1l);
                BeanModelFactory factory = BeanModelLookup.get().getFactory(Branch.class);
                BeanModel model = factory.createModel(branch);
                view.getBranchsGird().getStore().insert(model, view.getBranchsGird().getStore().getCount());
            }
        });
        view.getBtnSave().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                List<Branch> branches = new ArrayList<Branch>();
                for (Record record : view.getBranchsGird().getStore().getModifiedRecords()) {
                    BeanModel model = (BeanModel) record.getModel();
                    branches.add(model.<Branch>getBean());
                }
                if (CollectionsUtils.isNotEmpty(branches)) {
                    LoadingUtils.showLoading();
                    branchService.updateBranchs(branches, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            super.onSuccess(result);
                            view.getPagingToolBar().refresh();
                            DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                        }
                    });
                }
            }
        });
    }

    private ListStore<BeanModel> createUserListStore() {
        RpcProxy<BasePagingLoadResult<List<Branch>>> rpcProxy = new RpcProxy<BasePagingLoadResult<List<Branch>>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<BasePagingLoadResult<List<Branch>>> callback) {
                branchService.getBranchsForGrid((BasePagingLoadConfig) loadConfig, callback);
            }
        };

        PagingLoader<PagingLoadResult<Branch>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<Branch>>(rpcProxy, new BeanModelReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<BeanModel>(pagingLoader);
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
        final AsyncCallback<Void> callback = new AbstractAsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof DeleteException) {
                    DiaLogUtils.showMessage(view.getConstant().deleteErrorMessage());
                } else {
                    super.onFailure(caught);
                }
            }

            @Override
            public void onSuccess(Void result) {
                super.onSuccess(result);
                //Reload grid.
                view.getPagingToolBar().refresh();
                DiaLogUtils.notify(view.getConstant().deleteBranchMessageSuccess());
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
                    LoadingUtils.showLoading();
                    if (hasManyTag) {
                        branchService.deleteBranchByIds(branchIds, callback);
                    } else {
                        branchService.deleteBranchById(branchIds.get(0), callback);
                    }
                }
            }
        });
    }

    private CellEditor createStationCellEditor() {
        ComboBox<BeanModel> ccbStation = new ComboBox<BeanModel>();
        ccbStation.setStore(stationListStore);
        ccbStation.setTriggerAction(ComboBox.TriggerAction.ALL);
        ccbStation.setForceSelection(true);
        ccbStation.setDisplayField(StationManagerView.STATION_NAME_COLUMN);
        return new CellEditor(ccbStation);
    }

    private ListStore<BeanModel> createStationListStore() {
        final BeanModelFactory factory = BeanModelLookup.get().getFactory(Station.class);
        final ListStore<BeanModel> store = new ListStore<BeanModel>();
        LoadingUtils.showLoading();
        stationService.getAllStation(new AbstractAsyncCallback<List<Station>>() {
            @Override
            public void onSuccess(List<Station> result) {
                super.onSuccess(result);
                for (Station station : result) {
                    store.add(factory.createModel(station));
                }
            }
        });
        return store;
    }
}
