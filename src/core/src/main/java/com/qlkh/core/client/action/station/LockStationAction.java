/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.station;

import com.qlkh.core.client.constant.StationLockTypeEnum;
import com.qlkh.core.client.model.Station;
import net.customware.gwt.dispatch.shared.Action;


/**
 * The Class LockStationAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/6/12, 2:22 PM
 */
public class LockStationAction implements Action<LockStationResult> {

    private Station station;
    private StationLockTypeEnum lockType;
    private boolean lock;

    public LockStationAction() {
    }

    public LockStationAction(StationLockTypeEnum lockType, boolean lock) {
        this.lockType = lockType;
        this.lock = lock;
    }

    public LockStationAction(Station station, StationLockTypeEnum lockType, boolean lock) {
        this.station = station;
        this.lockType = lockType;
        this.lock = lock;
    }

    public StationLockTypeEnum getLockType() {
        return lockType;
    }

    public boolean isLock() {
        return lock;
    }

    public Station getStation() {
        return station;
    }
}
