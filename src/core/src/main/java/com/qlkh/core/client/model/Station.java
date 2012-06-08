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

package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

import javax.persistence.Transient;
import java.util.List;

/**
 * The Class Station.
 *
 * @author Nguyen Duc Dung
 * @since 12/29/11, 6:52 AM
 */
public class Station extends AbstractEntity {

    private String name;
    private boolean company;

    @Transient
    private List<Branch> branches;

    @Transient
    private List<StationLock> stationLocks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompany() {
        return company;
    }

    public void setCompany(boolean company) {
        this.company = company;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public List<StationLock> getStationLocks() {
        return stationLocks;
    }

    public void setStationLocks(List<StationLock> stationLocks) {
        this.stationLocks = stationLocks;
    }
}
