/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.station;

import com.qlkh.core.client.action.station.LockStationAction;
import com.qlkh.core.client.action.station.LockStationResult;
import com.qlkh.core.client.model.Station;
import com.qlkh.core.client.model.StationLock;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class LockStationHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/6/12, 2:24 PM
 */
public class LockStationHandler extends AbstractHandler<LockStationAction, LockStationResult> {

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<LockStationAction> getActionType() {
        return LockStationAction.class;
    }

    @Override
    public LockStationResult execute(LockStationAction action, ExecutionContext context) throws DispatchException {
        if (action.getStation() == null) {
            List<StationLock> stationLocks = generalDao.findCriteria(StationLock.class,
                    Restrictions.eq("code", action.getLockType().getCode()));
            if (CollectionUtils.isNotEmpty(stationLocks)) {
                List<Long> stationIds = new ArrayList<Long>(stationLocks.size());
                for (StationLock stationLock : stationLocks) {
                    stationIds.add(stationLock.getId());
                }
                generalDao.deleteByIds(StationLock.class, stationIds);
            }
            if (action.isLock()) {
                List<Station> stations = generalDao.getAll(Station.class);
                if (CollectionUtils.isNotEmpty(stations)) {
                    for (Station station : stations) {
                        if (!station.isCompany()) {
                            StationLock stationLock = new StationLock();
                            stationLock.setStation(station);
                            stationLock.setCode(action.getLockType().getCode());
                            stationLock.setUpdateBy(1l);
                            stationLock.setCreateBy(1l);
                            generalDao.saveOrUpdate(stationLock);
                        }
                    }
                }
            }
        } else {
            List<StationLock> stationLocks = generalDao.
                    findCriteria(StationLock.class,
                            Restrictions.eq("station.id", action.getStation().getId()),
                            Restrictions.eq("code", action.getLockType().getCode()));
            if (CollectionUtils.isNotEmpty(stationLocks)) {
                StationLock stationLock = stationLocks.get(0);
                generalDao.delete(stationLock);
            }
            if (action.isLock()) {
                if (!action.getStation().isCompany()) {
                    StationLock stationLock = new StationLock();
                    stationLock.setStation(action.getStation());
                    stationLock.setCode(action.getLockType().getCode());
                    stationLock.setUpdateBy(1l);
                    stationLock.setCreateBy(1l);
                    generalDao.saveOrUpdate(stationLock);
                }
            }
        }
        return new LockStationResult();
    }
}
