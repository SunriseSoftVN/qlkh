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

package com.qlvt.core.client.action.station;

import com.qlvt.core.client.model.Station;
import net.customware.gwt.dispatch.shared.Result;

import java.util.List;

/**
 * The Class LoadStationAndBranchResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 8:08 PM
 */
public class LoadStationResult implements Result {

    private Station station;
    private List<Station> stations;

    public LoadStationResult() {
    }

    public LoadStationResult(Station station) {
        this.station = station;
    }

    public LoadStationResult(List<Station> stations) {
        this.stations = stations;
    }

    public Station getStation() {
        return station;
    }

    public List<Station> getStations() {
        return stations;
    }
}
