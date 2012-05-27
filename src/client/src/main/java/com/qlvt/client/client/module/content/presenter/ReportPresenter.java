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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.content.place.ReportPlace;
import com.qlvt.client.client.module.content.view.ReportView;
import com.qlvt.client.client.service.ReportService;
import com.qlvt.client.client.service.ReportServiceAsync;
import com.qlvt.client.client.utils.LoadingUtils;
import com.qlvt.core.client.constant.ReportFileTypeEnum;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

/**
 * The Class ReportPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 2/19/12, 3:45 PM
 */
@Presenter(view = ReportView.class, place = ReportPlace.class)
public class ReportPresenter extends AbstractPresenter<ReportView> {

    private ReportServiceAsync reportService = ReportService.App.getInstance();

    private Window reportWindow;

    @Override
    public void onActivate() {
        view.show();
    }

    @Override
    protected void doBind() {
        view.getBtnPlanReportPdf().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                view.setEnableReportButton(false);
                LoadingUtils.showLoading();
                reportService.reportForCompany(view.getCbbReportType().getSimpleValue(),
                        ReportFileTypeEnum.PDF, new AbstractAsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        LoadingUtils.hideLoading();
                        view.setEnableReportButton(true);
                        reportWindow = view.createReportWindow(result);
                        reportWindow.show();
                    }
                });
            }
        });
        view.getBtnPlanReportXls().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                view.setEnableReportButton(false);
                LoadingUtils.showLoading();
                reportService.reportForCompany(view.getCbbReportType().getSimpleValue(),
                        ReportFileTypeEnum.EXCEL, new AbstractAsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        LoadingUtils.hideLoading();
                        view.setEnableReportButton(true);
                        reportWindow = view.createReportWindow(result);
                        reportWindow.show();
                    }
                });
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
