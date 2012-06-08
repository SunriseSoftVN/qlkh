/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.station;

import com.qlkh.core.client.model.Station;
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
