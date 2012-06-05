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

import com.qlvt.core.client.action.station.LoadStationAction;
import com.qlvt.core.client.action.station.LoadStationResult;
import com.qlvt.core.client.model.Branch;
import com.qlvt.core.client.model.Station;
import com.qlvt.core.client.model.User;
import com.qlvt.server.dao.BranchDao;
import com.qlvt.server.dao.UserDao;
import com.qlvt.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
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
    private BranchDao branchDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Class<LoadStationAction> getActionType() {
        return LoadStationAction.class;
    }

    @Override
    public LoadStationResult execute(LoadStationAction action, ExecutionContext context) throws DispatchException {
        User user = userDao.findByUserName(action.getUserName());
        if (user != null) {
            Station station = user.getStation();
            List<Branch> branches = branchDao.findByStationId(station.getId());
            station.setBranches(branches);
            return new LoadStationResult(station);
        }
        return null;
    }
}
