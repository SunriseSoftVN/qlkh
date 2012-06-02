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
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.client.client.core.dispatch.StandardDispatchAsync;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.content.place.StationManagerPlace;
import com.qlvt.client.client.module.content.view.StationManagerView;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.client.client.utils.GridUtils;
import com.qlvt.client.client.utils.LoadingUtils;
import com.qlvt.core.client.action.DeleteAction;
import com.qlvt.core.client.action.DeleteResult;
import com.qlvt.core.client.action.SaveAction;
import com.qlvt.core.client.action.SaveResult;
import com.qlvt.core.client.model.Branch;
import com.qlvt.core.client.model.Station;
import com.qlvt.core.client.model.TaskDetail;
import com.qlvt.core.client.model.User;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class StationManagerPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 12/29/11, 7:00 AM
 */
@Presenter(view = StationManagerView.class, place = StationManagerPlace.class)
public class StationManagerPresenter extends AbstractPresenter<StationManagerView> {

    private static final String[] RELATE_ENTITY_NAMES = {User.class.getName(), Branch.class.getName(), TaskDetail.class.getName()};
    private DispatchAsync dispatch = new StandardDispatchAsync(new DefaultExceptionHandler());

    private Station currentStation;
    private Window stationEditWindow;

    @Override
    public void onActivate() {
        view.show();
        view.getPagingToolBar().refresh();
        view.getStationsGird().focus();
    }

    @Override
    protected void doBind() {
        view.createGrid(GridUtils.createListStore(Station.class, dispatch));
        view.getPagingToolBar().bind((PagingLoader<?>) view.getStationsGird().getStore().getLoader());
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                stationEditWindow = view.createStationEditWindow();
                currentStation = new Station();
                stationEditWindow.show();
            }
        });
        view.getBtnEdit().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (view.getStationsGird().getSelectionModel().getSelectedItem() != null) {
                    currentStation = view.getStationsGird().getSelectionModel().getSelectedItem().getBean();
                    view.getTxtStationName().setValue(currentStation.getName());
                    stationEditWindow = view.createStationEditWindow();
                    stationEditWindow.show();
                }
            }
        });
        view.getBtnRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                view.getPagingToolBar().refresh();
            }
        });
        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
        view.getBtnStationEditOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (currentStation != null && view.getStationEditPanel().isValid()) {
                    currentStation.setName(view.getTxtStationName().getValue());
                    currentStation.setCreateBy(1l);
                    currentStation.setUpdateBy(1l);
                    dispatch.execute(new SaveAction(currentStation), new AbstractAsyncCallback<SaveResult>() {
                        @Override
                        public void onSuccess(SaveResult result) {
                            super.onSuccess(result);
                            DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                            stationEditWindow.hide();
                            updateGrid(result.<Station>getEntity());
                        }
                    });
                }
            }
        });
        view.getBtnStationEditCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                stationEditWindow.hide();
            }
        });
    }

    private void updateGrid(Station station) {
        boolean isNotFound = true;
        BeanModelFactory factory = BeanModelLookup.get().getFactory(Station.class);
        BeanModel updateModel = factory.createModel(station);
        for (BeanModel model : view.getStationsGird().getStore().getModels()) {
            if (station.getId().equals(model.<Station>getBean().getId())) {
                int index = view.getStationsGird().getStore().indexOf(model);
                view.getStationsGird().getStore().remove(model);
                view.getStationsGird().getStore().insert(updateModel, index);
                isNotFound = false;
            }
        }
        if (isNotFound) {
            view.getStationsGird().getStore().add(updateModel);
            view.getStationsGird().getView().ensureVisible(view.getStationsGird()
                    .getStore().getCount() - 1, 1, false);
        }
        view.getStationsGird().getSelectionModel().select(updateModel, false);
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
        final AsyncCallback<DeleteResult> callback = new AbstractAsyncCallback<DeleteResult>() {
            @Override
            public void onSuccess(DeleteResult result) {
                super.onSuccess(result);
                if (result.isResult()) {
                    //Reload grid.
                    view.getPagingToolBar().refresh();
                    DiaLogUtils.notify(view.getConstant().deleteStationMessageSuccess());
                } else {
                    DiaLogUtils.showMessage(view.getConstant().deleteErrorMessage());
                }
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
                        dispatch.execute(new DeleteAction(Station.class.getName(), stationIds,
                                RELATE_ENTITY_NAMES), callback);
                    } else {
                        dispatch.execute(new DeleteAction(Station.class.getName(), stationIds.get(0),
                                RELATE_ENTITY_NAMES), callback);
                    }
                }
            }
        });
    }

    @Override
    public String mayStop() {
        if (stationEditWindow != null && stationEditWindow.isVisible()) {
            return view.getConstant().conformExitMessage();
        }
        return null;
    }

    @Override
    public void onCancel() {
        if (stationEditWindow != null && stationEditWindow.isVisible()) {
            stationEditWindow.hide();
        }
    }
}
