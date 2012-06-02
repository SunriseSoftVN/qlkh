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

package com.qlvt.server.service;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.qlvt.client.client.service.StationService;
import com.qlvt.core.client.exception.DeleteException;
import com.qlvt.core.client.model.Branch;
import com.qlvt.core.client.model.Station;
import com.qlvt.core.client.model.User;
import com.qlvt.server.guice.DaoProvider;
import com.qlvt.server.service.core.AbstractService;
import com.qlvt.server.transaction.Transaction;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * The Class StationServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 12/29/11, 1:36 PM
 */
@Transaction
@Singleton
public class StationServiceImpl extends AbstractService implements StationService {

    @Inject
    private DaoProvider provider;

    @Override
    public List<Station> getAllStation() {
        return provider.getStationDao().getAll(Station.class);
    }

    @Override
    public BasePagingLoadResult<Station> getStationsForGrid(BasePagingLoadConfig config) {
        return provider.getStationDao().getByBeanConfig(Station.class, config);
    }

    @Override
    public void updateStations(List<Station> stations) {
        provider.getStationDao().saveOrUpdate(stations);
    }

    @Override
    public Station updateStation(Station station) {
        return provider.getStationDao().saveOrUpdate(station);
    }

    @Override
    public void deleteStationById(long stationId) throws DeleteException {
        if (CollectionUtils.isEmpty(provider.getUserDao().findByStationId(stationId))
                && CollectionUtils.isEmpty(provider.getBranchDao().findByStationId(stationId))
                && CollectionUtils.isEmpty(provider.getTaskDetailDao().findByStationId(stationId))) {
            provider.getStationDao().deleteById(Station.class, stationId);
        } else {
            throw new DeleteException();
        }
    }

    @Override
    public void deleteStationByIds(List<Long> stationIds) throws DeleteException {
        for (Long stationId : stationIds) {
            deleteStationById(stationId);
        }
    }

    @Override
    public Station getStationAndBranchByUserName(String userName) {
        User user = provider.getUserDao().findByUserName(userName);
        if (user != null) {
            Station station = user.getStation();
            List<Branch> branches = provider.getBranchDao().findByStationId(station.getId());
            station.setBranches(branches);
            return station;
        }
        return null;
    }
}
