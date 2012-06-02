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
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.content.place.ReportPlace;
import com.qlvt.client.client.module.content.view.ReportView;
import com.qlvt.client.client.service.ReportService;
import com.qlvt.client.client.service.ReportServiceAsync;
import com.qlvt.client.client.service.StationService;
import com.qlvt.client.client.service.StationServiceAsync;
import com.qlvt.client.client.utils.LoadingUtils;
import com.qlvt.core.client.constant.ReportFileTypeEnum;
import com.qlvt.core.client.model.Station;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

import java.util.List;

/**
 * The Class ReportPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 2/19/12, 3:45 PM
 */
@Presenter(view = ReportView.class, place = ReportPlace.class)
public class ReportPresenter extends AbstractPresenter<ReportView> {

    private ReportServiceAsync reportService = ReportService.App.getInstance();

    private StationServiceAsync stationService = StationService.App.getInstance();

    private Window reportWindow;

    private ListStore<BeanModel> stationListStore;

    @Override
    public void onActivate() {
        view.show();
        if (stationListStore != null) {
            //reload stations list.
            stationListStore = createStationListStore();
            view.getCbbReportStation().setStore(stationListStore);
        }
    }

    @Override
    protected void doBind() {
        stationListStore = createStationListStore();
        view.getCbbReportStation().setStore(stationListStore);
        view.getBtnPlanReportPdf().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (view.getCbbReportStation().getValue() != null
                        && view.getCbbYear().getValue() != null && view.getCbbReportType().getValue() != null) {
                    Station station = view.getCbbReportStation().getValue().getBean();
                    view.setEnableReportButton(false);
                    LoadingUtils.showLoading();
                    reportService.reportForCompany(view.getCbbReportType().getSimpleValue(),
                            ReportFileTypeEnum.PDF, station.getId(), new AbstractAsyncCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            LoadingUtils.hideLoading();
                            view.setEnableReportButton(true);
                            reportWindow = view.createReportWindow(result);
                            reportWindow.show();
                        }
                    });

                }
            }
        });
        view.getBtnPlanReportXls().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                if (view.getCbbReportStation().getValue() != null
                        && view.getCbbYear().getValue() != null && view.getCbbReportType().getValue() != null) {
                    Station station = view.getCbbReportStation().getValue().getBean();
                    view.setEnableReportButton(false);
                    LoadingUtils.showLoading();
                    reportService.reportForCompany(view.getCbbReportType().getSimpleValue(),
                            ReportFileTypeEnum.EXCEL, station.getId(), new AbstractAsyncCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            LoadingUtils.hideLoading();
                            view.setEnableReportButton(true);
                            reportWindow = view.createReportWindow(result);
                            reportWindow.show();
                        }
                    });

                }
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
