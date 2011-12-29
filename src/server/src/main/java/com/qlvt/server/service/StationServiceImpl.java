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
import com.qlvt.core.client.model.Station;
import com.qlvt.server.dao.StationDao;
import com.qlvt.server.service.core.AbstractService;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class StationServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 12/29/11, 1:36 PM
 */
@Singleton
public class StationServiceImpl extends AbstractService implements StationService {

    @Inject
    private StationDao stationDao;

    @Override
    public BasePagingLoadResult<List<Station>> getStationsForGrid(BasePagingLoadConfig config) {
        List<Station> stations = stationDao.getAll(Station.class);

        ArrayList<Station> subList = new ArrayList<Station>();
        int start = config.getOffset();
        int limit = stations.size();
        if (config.getLimit() > 0) {
            limit = Math.min(start + config.getLimit(), limit);
        }
        for (int i = config.getOffset(); i < limit; i++) {
            subList.add(stations.get(i));
        }

        return new BasePagingLoadResult(subList, config.getOffset(), stations.size());
    }

    @Override
    public void updateStations(List<Station> stations) {
        stationDao.saveOrUpdate(stations);
    }
}
