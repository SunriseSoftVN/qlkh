/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.core;

import com.qlkh.core.client.model.User;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class LoginResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 1:56 AM
 */
public class LoginResult implements Result {

    private User user;

    public LoginResult() {
    }

    public LoginResult(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
