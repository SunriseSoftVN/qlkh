/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
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
