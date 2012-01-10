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

package com.qlvt.client.client.service;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.qlvt.client.client.utils.ServiceUtils;
import com.qlvt.core.client.exception.DeleteException;
import com.qlvt.core.client.model.Station;
import com.smvp4g.mvp.client.core.service.RemoteService;

import java.util.List;

/**
 * The Class StationService.
 *
 * @author Nguyen Duc Dung
 * @since 12/29/11, 1:32 PM
 */
@RemoteServiceRelativePath("Station")
public interface StationService extends RemoteService<StationService> {

    Station getStationAndBranchByUserName(String userName);
    void deleteStationById(long stationId) throws DeleteException;
    void deleteStationByIds(List<Long> stationIds);
    void updateStations(List<Station> stations);
    List<Station> getAllStation();
    BasePagingLoadResult<List<Station>> getStationsForGrid(BasePagingLoadConfig config);

    public static class App {
        private static final StationServiceAsync ourInstance = (StationServiceAsync) GWT.create(StationService.class);

        public static StationServiceAsync getInstance() {
            ServiceUtils.configureServiceEntryPoint(StationService.class, ourInstance);
            return ourInstance;
        }
    }
}
