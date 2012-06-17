/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.StationLockPlace;
import com.qlkh.client.client.module.content.view.StationLockView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.core.client.action.station.LoadStationAction;
import com.qlkh.core.client.action.station.LoadStationResult;
import com.qlkh.core.client.action.station.LockStationAction;
import com.qlkh.core.client.action.station.LockStationResult;
import com.qlkh.core.client.constant.StationLockTypeEnum;
import com.qlkh.core.client.model.Station;
import com.qlkh.core.client.model.StationLock;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.List;

/**
 * The Class StationLockPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/12, 9:45 PM
 */
@Presenter(view = StationLockView.class, place = StationLockPlace.class)
public class StationLockPresenter extends AbstractPresenter<StationLockView> {

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;

    @Override
    public void onActivate() {
        view.show();
    }

    @Override
    protected void doBind() {
        dispatch.execute(new LoadStationAction(), new AbstractAsyncCallback<LoadStationResult>() {
            @Override
            public void onSuccess(LoadStationResult result) {
                for (final Station station : result.getStations()) {
                    view.addStationName(station.getName());
                    view.addAnnualButton(createAnnualLock(station), station.isCompany());
                    view.addNormalButton(createNormalLock(station, StationLockTypeEnum.KDK_Q1),
                            createNormalLock(station, StationLockTypeEnum.KDK_Q2),
                            createNormalLock(station, StationLockTypeEnum.KDK_Q3),
                            createNormalLock(station, StationLockTypeEnum.KDK_Q4), station.isCompany());
                }
                view.checkCompanyCb(view.getDkCompanyCb(), view.getDkCbs());
                view.checkCompanyCb(view.getKdkQ1CompanyCb(), view.getKdkQ1Cbs());
                view.checkCompanyCb(view.getKdkQ2CompanyCb(), view.getKdkQ2Cbs());
                view.checkCompanyCb(view.getKdkQ3CompanyCb(), view.getKdkQ3Cbs());
                view.checkCompanyCb(view.getKdkQ4CompanyCb(), view.getKdkQ4Cbs());
                view.layout();
            }
        });
    }

    private CheckBox createNormalLock(final Station station, StationLockTypeEnum lockType) {
        final CheckBox checkBox = new CheckBox();
        checkBox.setValue(true);
        if (CollectionsUtils.isNotEmpty(station.getStationLocks())) {
            for (StationLock stationLock : station.getStationLocks()) {
                if (lockType.getCode() == stationLock.getCode()) {
                    checkBox.setValue(false);
                    break;
                }
            }
        }
        checkBox.addListener(Events.Change, new LockListener(station, lockType));
        return checkBox;
    }

    private CheckBox createAnnualLock(final Station station) {
        final CheckBox checkBox = new CheckBox();
        checkBox.setValue(true);
        if (CollectionsUtils.isNotEmpty(station.getStationLocks())) {
            for (StationLock stationLock : station.getStationLocks()) {
                if (StationLockTypeEnum.DK.getCode() == stationLock.getCode()) {
                    checkBox.setValue(false);
                    break;
                }
            }
        }
        checkBox.addListener(Events.Change, new LockListener(station, StationLockTypeEnum.DK));
        return checkBox;
    }


    private class LockListener implements Listener<BaseEvent> {

        private Station currentStation;
        private StationLockTypeEnum lockType;

        private LockListener(Station currentStation, StationLockTypeEnum lockType) {
            this.currentStation = currentStation;
            this.lockType = lockType;
        }

        @Override
        public void handleEvent(BaseEvent be) {
            final CheckBox checkBox = (CheckBox) be.getSource();
            checkBox.setEnabled(false);
            if (!currentStation.isCompany()) {
                getCheckBox(lockType).setEnabled(false);
                dispatch.execute(new LockStationAction(currentStation, lockType, !checkBox.getValue()),
                        new AbstractAsyncCallback<LockStationResult>() {
                            @Override
                            public void onSuccess(LockStationResult result) {
                                if (checkBox.getValue()) {
                                    DiaLogUtils.notify(view.getConstant().unLockSuccessful());
                                } else {
                                    DiaLogUtils.notify(view.getConstant().lockSuccessful());
                                }
                                checkBox.setEnabled(true);
                                getCheckBox(lockType).setEnabled(true);
                                view.checkCompanyCb(getCheckBox(lockType), getCheckBoxes(lockType));
                            }
                        });

            } else {
                view.enableAllLock(getCheckBoxes(lockType), false);
                dispatch.execute(new LockStationAction(lockType, !checkBox.getValue()),
                        new AbstractAsyncCallback<LockStationResult>() {
                            @Override
                            public void onSuccess(LockStationResult result) {
                                if (checkBox.getValue()) {
                                    DiaLogUtils.notify(view.getConstant().unLockSuccessful());
                                } else {
                                    DiaLogUtils.notify(view.getConstant().lockSuccessful());
                                }
                                checkBox.setEnabled(true);
                                view.checkAllLock(getCheckBoxes(lockType), checkBox.getValue());
                                view.enableAllLock(getCheckBoxes(lockType), true);
                            }
                        });
            }
        }
    }

    private CheckBox getCheckBox(StationLockTypeEnum typeEnum) {
        if (typeEnum == StationLockTypeEnum.DK) {
            return view.getDkCompanyCb();
        } else if (typeEnum == StationLockTypeEnum.KDK_Q1) {
            return view.getKdkQ1CompanyCb();
        } else if (typeEnum == StationLockTypeEnum.KDK_Q2) {
            return view.getKdkQ2CompanyCb();
        } else if (typeEnum == StationLockTypeEnum.KDK_Q3) {
            return view.getKdkQ3CompanyCb();
        } else if (typeEnum == StationLockTypeEnum.KDK_Q4) {
            return view.getKdkQ4CompanyCb();
        }
        return null;
    }

    private List<CheckBox> getCheckBoxes(StationLockTypeEnum typeEnum) {
        if (typeEnum == StationLockTypeEnum.DK) {
            return view.getDkCbs();
        } else if (typeEnum == StationLockTypeEnum.KDK_Q1) {
            return view.getKdkQ1Cbs();
        } else if (typeEnum == StationLockTypeEnum.KDK_Q2) {
            return view.getKdkQ2Cbs();
        } else if (typeEnum == StationLockTypeEnum.KDK_Q3) {
            return view.getKdkQ3Cbs();
        } else if (typeEnum == StationLockTypeEnum.KDK_Q4) {
            return view.getKdkQ4Cbs();
        }
        return null;
    }
}
