/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
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

    @SuppressWarnings("JpaAttributeMemberSignatureInspection")
    public String getShortName() {
        String prefix = "Xí nghiệp ";
        String shortName = name.replace(prefix, "");
        if (shortName.equals("XL và DV TTTH Điện")) {
            return "XN & XL";
        }
        return shortName;
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
