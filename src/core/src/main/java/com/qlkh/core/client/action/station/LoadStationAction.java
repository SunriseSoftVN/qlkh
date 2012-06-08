/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.station;

import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadStationAndBranchAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 8:09 PM
 */
public class LoadStationAction implements Action<LoadStationResult> {

    private String userName;

    public LoadStationAction() {
    }

    public LoadStationAction(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
