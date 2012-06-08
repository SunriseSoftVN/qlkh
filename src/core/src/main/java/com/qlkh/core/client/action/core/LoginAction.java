/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.core;

import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoginAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 1:56 AM
 */
public class LoginAction implements Action<LoginResult> {

    private String userName;

    private String passWord;

    public LoginAction() {
    }

    public LoginAction(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

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
}
