/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
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
import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.core.client.constant.ReportFileTypeEnum;
import com.qlkh.core.client.constant.UserRoleEnum;
import com.qlkh.core.client.criterion.ClientRestrictions;
import com.qlkh.core.client.model.Branch;
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
    private ListStore<BeanModel> branchListStore;
    private Station currentStation;

    @Override
    public void onActivate() {
        view.show();
    }

    @Override
    protected void doBind() {
        if (UserRoleEnum.USER.getRole().equals(LoginUtils.getRole())) {
            dispatch.execute(new LoadStationAction(LoginUtils.getUserName()), new AbstractAsyncCallback<LoadStationResult>() {
                @Override
                public void onSuccess(LoadStationResult result) {
                    currentStation = result.getStation();
                    branchListStore = GridUtils.createListStoreForCb(Branch.class,
                            ClientRestrictions.eq("station.id", currentStation.getId()));
                    //Add default model.
                    Branch fakeBranch = new Branch();
                    fakeBranch.setName(view.getConstant().fakeBranchName());
                    BeanModel fakeModel = BeanModelLookup.get().getFactory(Branch.class).createModel(fakeBranch);
                    branchListStore.insert(fakeModel, 0);
                    view.getCbbTaskReportBranch().setStore(branchListStore);
                    view.getCbbTaskReportBranch().setValue(fakeModel);

                    view.getCbbPriceReportBranch().setStore(branchListStore);
                    view.getCbbPriceReportBranch().setValue(fakeModel);

                }
            });
        } else {
            stationListStore = GridUtils.createListStoreForCb(Station.class);
            view.getCbbTaskReportStation().setStore(stationListStore);
            view.getCbbPriceReportStation().setStore(stationListStore);
        }
        dispatch.execute(new GetServerTimeAction(), new AbstractAsyncCallback<GetServerTimeResult>() {
            @Override
            public void onSuccess(GetServerTimeResult result) {
                view.getCbbTaskYear().setSimpleValue(result.getYear());
                view.getCbbPriceYear().setSimpleValue(result.getYear());
            }
        });
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
        if (view.getCbbTaskYear().getValue() != null && view.getCbbTaskReportType().getValue() != null) {
            Station station = null;
            Long branchId = null;
            if (UserRoleEnum.USER.getRole().equals(LoginUtils.getRole())) {
                station = currentStation;
                Branch branch = view.getCbbTaskReportBranch().getValue().getBean();
                if (branch != null) {
                    branchId = branch.getId();
                }
            } else if (view.getCbbTaskReportStation().getValue() != null) {
                station = view.getCbbTaskReportStation().getValue().getBean();
            }
            if (station != null) {
                view.setEnableReportButton(false);
                dispatch.execute(new ReportAction(view.getCbbTaskReportType().getSimpleValue(), view.getCbbTaskReportForm().getSimpleValue(),
                        fileTypeEnum, station.getId(), branchId, view.getCbbTaskYear().getSimpleValue()), new AbstractAsyncCallback<ReportResult>() {
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
