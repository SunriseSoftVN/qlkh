/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

/**
 * The Class User.
 *
 * @author Nguyen Duc Dung
 * @since 12/27/11, 11:18 AM
 */
public class User extends AbstractEntity {

    private String userName;
    private String passWord;
    private String userRole;
    private Station station;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
