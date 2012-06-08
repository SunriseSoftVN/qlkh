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

package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.ReportPlace;
import com.qlkh.client.client.module.content.view.ReportView;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.report.ReportAction;
import com.qlkh.core.client.action.report.ReportResult;
import com.qlkh.core.client.action.station.LoadStationAction;
import com.qlkh.core.client.action.station.LoadStationResult;
import com.qlkh.core.client.constant.ReportFileTypeEnum;
import com.qlkh.core.client.constant.UserRoleEnum;
import com.qlkh.core.client.model.Station;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.LoginUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;

/**
 * The Class ReportPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 2/19/12, 3:45 PM
 */
@Presenter(view = ReportView.class, place = ReportPlace.class)
public class ReportPresenter extends AbstractPresenter<ReportView> {

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;
    private Window reportWindow;
    private ListStore<BeanModel> stationListStore;
    private Station currentStation;

    @Override
    public void onActivate() {
        view.show();
        if (stationListStore != null) {
            //reload stations list.
            stationListStore = GridUtils.createListStoreForCb(Station.class);
            view.getCbbReportStation().setStore(stationListStore);
        }
    }

    @Override
    protected void doBind() {
        if (UserRoleEnum.USER.getRole().equals(LoginUtils.getRole())) {
            dispatch.execute(new LoadStationAction(LoginUtils.getUserName()), new AbstractAsyncCallback<LoadStationResult>() {
                @Override
                public void onSuccess(LoadStationResult result) {
                    currentStation = result.getStation();
                }
            });
        } else {
            stationListStore = GridUtils.createListStoreForCb(Station.class);
            view.getCbbReportStation().setStore(stationListStore);
        }
        view.getBtnPlanReportPdf().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                report(ReportFileTypeEnum.PDF);
            }
        });
        view.getBtnPlanReportXls().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                report(ReportFileTypeEnum.EXCEL);
            }
        });
        view.getBtnReportCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (reportWindow != null) {
                    reportWindow.hide();
                }
            }
        });
    }

    private void report(ReportFileTypeEnum fileTypeEnum) {
        if (view.getCbbYear().getValue() != null && view.getCbbReportType().getValue() != null) {
            Station station = null;
            if (UserRoleEnum.USER.getRole().equals(LoginUtils.getRole())) {
                station = currentStation;
            } else if (view.getCbbReportStation().getValue() != null) {
                station = view.getCbbReportStation().getValue().getBean();
            }
            if (station != null) {
                view.setEnableReportButton(false);
                dispatch.execute(new ReportAction(view.getCbbReportType().getSimpleValue(),
                        fileTypeEnum, station.getId()), new AbstractAsyncCallback<ReportResult>() {
                    @Override
                    public void onSuccess(ReportResult result) {
                        view.setEnableReportButton(true);
                        reportWindow = view.createReportWindow(result.getReportUrl());
                        reportWindow.show();
                    }
                });
            }
        }
    }
}
