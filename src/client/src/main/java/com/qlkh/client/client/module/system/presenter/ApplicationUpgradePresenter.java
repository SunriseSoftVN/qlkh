/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.system.presenter;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.system.place.ApplicationUpgradePlace;
import com.qlkh.client.client.module.system.view.ApplicationUpgradeView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.core.client.action.system.*;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import net.customware.gwt.dispatch.client.DispatchAsync;

/**
 * The Class ApplicationUpgradePresenter.
 *
 * @author Nguyen Duc Dung
 * @since 6/16/12, 7:08 PM
 */
@Presenter(view = ApplicationUpgradeView.class, place = ApplicationUpgradePlace.class)
public class ApplicationUpgradePresenter extends AbstractPresenter<ApplicationUpgradeView> {

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;

    @Override
    public void onActivate() {
        view.show();
    }

    @Override
    protected void doBind() {
        view.getUpgradeV11().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                dispatch.execute(new UpgradeDatabaseAction(), new AbstractAsyncCallback<UpgradeDatabaseResult>() {
                    @Override
                    public void onSuccess(UpgradeDatabaseResult result) {
                        DiaLogUtils.notify("Upgrade Successful");
                    }
                });
            }
        });
        view.getUpgradeV116().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                dispatch.execute(new Upgrade116Action(), new AbstractAsyncCallback<Upgrade116Result>() {
                    @Override
                    public void onSuccess(Upgrade116Result upgrade116Result) {
                        DiaLogUtils.notify("Upgrade Successful");
                    }
                });
            }
        });

        view.getUpgradeV134().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                dispatch.execute(new Upgrade134Action(), new AbstractAsyncCallback<Upgrade134Result>() {
                    @Override
                    public void onSuccess(Upgrade134Result upgrade134Result) {
                        DiaLogUtils.notify("Upgrade Successful");
                    }
                });
            }
        });

        view.getCopyDataLastYear().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                view.getCopyDataLastYear().setEnabled(false);
                dispatch.execute(new CopyDataFormLastYearAction(), new AbstractAsyncCallback<CopyDataFormLastYearResult>() {
                    @Override
                    public void onSuccess(CopyDataFormLastYearResult result) {
                        view.getCopyDataLastYear().setEnabled(true);
                        DiaLogUtils.notify("Copying Successful");
                    }
                });
            }
        });
    }
}
