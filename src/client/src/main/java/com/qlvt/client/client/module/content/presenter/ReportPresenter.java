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
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.qlvt.client.client.core.dispatch.StandardDispatchAsync;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.content.place.ReportPlace;
import com.qlvt.client.client.module.content.view.ReportView;
import com.qlvt.client.client.utils.GridUtils;
import com.qlvt.core.client.action.report.ReportAction;
import com.qlvt.core.client.action.report.ReportResult;
import com.qlvt.core.client.constant.ReportFileTypeEnum;
import com.qlvt.core.client.model.Station;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
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

    @Override
    public void onActivate() {
        view.show();
        if (stationListStore != null) {
            //reload stations list.
            stationListStore = GridUtils.getListStoreForCb(Station.class, dispatch);
            view.getCbbReportStation().setStore(stationListStore);
        }
    }

    @Override
    protected void doBind() {
        stationListStore = GridUtils.getListStoreForCb(Station.class, dispatch);
        view.getCbbReportStation().setStore(stationListStore);
        view.getBtnPlanReportPdf().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (view.getCbbReportStation().getValue() != null
                        && view.getCbbYear().getValue() != null && view.getCbbReportType().getValue() != null) {
                    Station station = view.getCbbReportStation().getValue().getBean();
                    view.setEnableReportButton(false);
                    dispatch.execute(new ReportAction(view.getCbbReportType().getSimpleValue(),
                            ReportFileTypeEnum.PDF, station.getId()), new AbstractAsyncCallback<ReportResult>() {
                        @Override
                        public void onSuccess(ReportResult result) {
                            view.setEnableReportButton(true);
                            reportWindow = view.createReportWindow(result.getReportUrl());
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
                    dispatch.execute(new ReportAction(view.getCbbReportType().getSimpleValue(),
                            ReportFileTypeEnum.EXCEL, station.getId()), new AbstractAsyncCallback<ReportResult>() {
                        @Override
                        public void onSuccess(ReportResult result) {
                            view.setEnableReportButton(true);
                            reportWindow = view.createReportWindow(result.getReportUrl());
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
}
