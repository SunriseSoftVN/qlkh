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

package com.qlkh.server.handler.station;

import com.qlkh.core.client.action.station.LoadStationAction;
import com.qlkh.core.client.action.station.LoadStationResult;
import com.qlkh.core.client.model.Branch;
import com.qlkh.core.client.model.Station;
import com.qlkh.core.client.model.StationLock;
import com.qlkh.core.client.model.User;
import com.qlkh.server.dao.BranchDao;
import com.qlkh.server.dao.UserDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * The Class LoadStationHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 8:11 PM
 */
public class LoadStationHandler extends AbstractHandler<LoadStationAction, LoadStationResult> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private BranchDao branchDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Class<LoadStationAction> getActionType() {
        return LoadStationAction.class;
    }

    @Override
    public LoadStationResult execute(LoadStationAction action, ExecutionContext context) throws DispatchException {
        if (action.getUserName() != null) {
            User user = userDao.findByUserName(action.getUserName());
            if (user != null) {
                Station station = user.getStation();
                List<Branch> branches = branchDao.findByStationId(station.getId());
                station.setBranches(branches);
                List<StationLock> stationLocks = generalDao.findCriteria(StationLock.class,
                        Restrictions.eq("station.id", station.getId()));
                station.setStationLocks(stationLocks);
                return new LoadStationResult(station);
            }
        } else {
            List<Station> stations = generalDao.getAll(Station.class);
            for (Station station : stations) {
                List<StationLock> stationLocks = generalDao.findCriteria(StationLock.class,
                        Restrictions.eq("station.id", station.getId()));
                station.setStationLocks(stationLocks);
            }
            return new LoadStationResult(stations);
        }
        return null;
    }
}
