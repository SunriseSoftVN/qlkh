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

package com.qlvt.server.handler.station;

import com.qlvt.core.client.action.station.LockStationAction;
import com.qlvt.core.client.action.station.LockStationResult;
import com.qlvt.core.client.model.Station;
import com.qlvt.core.client.model.StationLock;
import com.qlvt.server.dao.core.GeneralDao;
import com.qlvt.server.handler.core.AbstractHandler;
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
