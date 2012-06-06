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

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.qlvt.client.client.core.dispatch.StandardDispatchAsync;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.client.client.module.content.place.StationLockPlace;
import com.qlvt.client.client.module.content.view.StationLockView;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.core.client.action.station.LoadStationAction;
import com.qlvt.core.client.action.station.LoadStationResult;
import com.qlvt.core.client.action.station.LockStationAction;
import com.qlvt.core.client.action.station.LockStationResult;
import com.qlvt.core.client.constant.StationLockTypeEnum;
import com.qlvt.core.client.model.Station;
import com.qlvt.core.client.model.StationLock;
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
    private List<Station> stations;

    @Override
    public void onActivate() {
        view.show();
    }

    @Override
    protected void doBind() {
        dispatch.execute(new LoadStationAction(), new AbstractAsyncCallback<LoadStationResult>() {
            @Override
            public void onSuccess(LoadStationResult result) {
                stations = result.getStations();
                for (final Station station : result.getStations()) {
                    view.addStationName(station.getName());
                    view.addAnnualButton(createAnnualLock(station), station.isCompany());
                    view.addNormalButton(createNormalLock(station, StationLockTypeEnum.KDK_Q1),
                            createNormalLock(station, StationLockTypeEnum.KDK_Q2),
                            createNormalLock(station, StationLockTypeEnum.KDK_Q3),
                            createNormalLock(station, StationLockTypeEnum.KDK_Q4));
                }
                view.checkAnnualCompanyCb();
                view.layout();
            }
        });
    }

    private CheckBox createNormalLock(final Station station, StationLockTypeEnum lockTypeEnum) {
        final CheckBox checkBox = new CheckBox();
        checkBox.setValue(true);
        if (CollectionsUtils.isNotEmpty(station.getStationLocks())) {
            for (StationLock stationLock : station.getStationLocks()) {
                if (lockTypeEnum.getCode() == stationLock.getCode()) {
                    checkBox.setValue(false);
                    break;
                }
            }
        }
        checkBox.addListener(Events.Change, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                DiaLogUtils.notify(checkBox.getValue());
            }
        });
        return checkBox;
    }

    private CheckBox createAnnualLock(final Station station) {
        final CheckBox checkBox = new CheckBox();
        StationLock currentStationLock = null;
        checkBox.setValue(true);
        if (CollectionsUtils.isNotEmpty(station.getStationLocks())) {
            for (StationLock stationLock : station.getStationLocks()) {
                if (StationLockTypeEnum.DK.getCode() == stationLock.getCode()) {
                    checkBox.setValue(false);
                    currentStationLock = stationLock;
                    break;
                }
            }
        }
        checkBox.addListener(Events.Change, new LockAnnualListener(station));
        return checkBox;
    }

    private StationLock createStationLock(Station station) {
        StationLock stationLock = new StationLock();
        stationLock.setStation(station);
        stationLock.setCreateBy(1l);
        stationLock.setUpdateBy(1l);
        stationLock.setCode(StationLockTypeEnum.DK.getCode());
        return stationLock;
    }

    private class LockAnnualListener implements Listener<BaseEvent> {

        private Station currentStation;

        private LockAnnualListener(Station currentStation) {
            this.currentStation = currentStation;
        }

        @Override
        public void handleEvent(BaseEvent be) {
            final CheckBox checkBox = (CheckBox) be.getSource();
            checkBox.setEnabled(false);
            if (!currentStation.isCompany()) {
                view.getAnnualCompanyCb().setEnabled(false);
                dispatch.execute(new LockStationAction(currentStation, StationLockTypeEnum.DK, !checkBox.getValue()),
                        new AbstractAsyncCallback<LockStationResult>() {
                            @Override
                            public void onSuccess(LockStationResult result) {
                                if (checkBox.getValue()) {
                                    DiaLogUtils.notify(view.getConstant().unLockSuccessful());
                                } else {
                                    DiaLogUtils.notify(view.getConstant().lockSuccessful());
                                }
                                checkBox.setEnabled(true);
                                view.getAnnualCompanyCb().setEnabled(true);
                                view.checkAnnualCompanyCb();
                            }
                        });

            } else {
                view.enableAllAnnualLock(false);
                dispatch.execute(new LockStationAction(StationLockTypeEnum.DK, !checkBox.getValue()),
                        new AbstractAsyncCallback<LockStationResult>() {
                            @Override
                            public void onSuccess(LockStationResult result) {
                                if (checkBox.getValue()) {
                                    DiaLogUtils.notify(view.getConstant().unLockSuccessful());
                                } else {
                                    DiaLogUtils.notify(view.getConstant().lockSuccessful());
                                }
                                checkBox.setEnabled(true);
                                view.checkAllAnnualLock(checkBox.getValue());
                                view.enableAllAnnualLock(true);
                            }
                        });
            }
        }
    }
}
