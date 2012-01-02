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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.content.place.StationManagerPlace;
import com.qlvt.client.client.module.content.view.StationManagerView;
import com.qlvt.client.client.service.StationService;
import com.qlvt.client.client.service.StationServiceAsync;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.client.client.utils.LoadingUtils;
import com.qlvt.core.client.model.Station;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Class StationManagerPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 12/29/11, 7:00 AM
 */
@Presenter(view = StationManagerView.class, place = StationManagerPlace.class)
public class StationManagerPresenter extends AbstractPresenter<StationManagerView> {

    private StationServiceAsync stationService = StationService.App.getInstance();

    @Override
    public void onActivate() {
        view.show();
        view.getPagingToolBar().refresh();
    }

    @Override
    protected void doBind() {
        view.createGrid(createUserListStore());
        view.getPagingToolBar().bind((PagingLoader<?>) view.getStationsGird().getStore().getLoader());
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                Station station = new Station();
                station.setCreatedDate(new Date());
                station.setUpdatedDate(new Date());
                station.setCreateBy(1l);
                station.setUpdateBy(1l);
                BeanModelFactory factory = BeanModelLookup.get().getFactory(Station.class);
                BeanModel model = factory.createModel(station);
                view.getStationsGird().getStore().insert(model, view.getStationsGird().getStore().getCount());
            }
        });
        view.getBtnSave().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                List<Station> stations = new ArrayList<Station>();
                for (Record record : view.getStationsGird().getStore().getModifiedRecords()) {
                    BeanModel model = (BeanModel) record.getModel();
                    stations.add(model.<Station>getBean());
                }
                if (CollectionsUtils.isNotEmpty(stations)) {
                    LoadingUtils.showLoading();
                    stationService.updateStations(stations, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            super.onSuccess(result);
                            view.getStationsGird().getStore().commitChanges();
                            DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                        }
                    });
                }
            }
        });
        view.getBtnCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                view.getPagingToolBar().refresh();
            }
        });
        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
    }

    private ListStore<BeanModel> createUserListStore() {
        RpcProxy<BasePagingLoadResult<List<Station>>> rpcProxy = new RpcProxy<BasePagingLoadResult<List<Station>>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<BasePagingLoadResult<List<Station>>> callback) {
                stationService.getStationsForGrid((BasePagingLoadConfig) loadConfig, callback);
            }
        };

        PagingLoader<PagingLoadResult<Station>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<Station>>(rpcProxy, new BeanModelReader()) {
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
            final List<BeanModel> models = view.getStationsGird().getSelectionModel().getSelectedItems();
            if (CollectionsUtils.isNotEmpty(models)) {
                if (models.size() == 1) {
                    final Station station = (Station) models.get(0).getBean();
                    showDeleteTagConform(station.getId(), station.getName());
                } else {
                    List<Long> stationIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        final Station station = (Station) model.getBean();
                        stationIds.add(station.getId());
                    }
                    showDeleteTagConform(stationIds, null);
                }
            }
        }
    }

    private void showDeleteTagConform(long stationId, String tagName) {
        List<Long> stationIds = new ArrayList<Long>(1);
        stationIds.add(stationId);
        showDeleteTagConform(stationIds, tagName);
    }

    private void showDeleteTagConform(final List<Long> stationIds, String tagName) {
        assert stationIds != null;
        String deleteMessage;
        final AsyncCallback<Void> callback = new AbstractAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                super.onSuccess(result);
                //Reload grid.
                view.getPagingToolBar().refresh();
                DiaLogUtils.notify(view.getConstant().deleteStationMessageSuccess());
            }
        };
        final boolean hasManyTag = stationIds.size() > 1;
        if (hasManyTag) {
            deleteMessage = view.getConstant().deleteAllStationMessage();
        } else {
            deleteMessage = StringUtils.substitute(view.getConstant().deleteStationMessage(), tagName);
        }

        DiaLogUtils.conform(deleteMessage, new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked().getText().equals("Yes")) {
                    LoadingUtils.showLoading();
                    if (hasManyTag) {
                        stationService.deleteStationByIds(stationIds, callback);
                    } else {
                        stationService.deleteStationById(stationIds.get(0), callback);
                    }
                }
            }
        });
    }
}
