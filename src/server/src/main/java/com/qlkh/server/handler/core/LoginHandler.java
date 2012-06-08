/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.core;

import com.qlkh.core.client.action.core.LoginAction;
import com.qlkh.core.client.action.core.LoginResult;
import com.qlkh.core.client.model.User;
import com.qlkh.server.dao.UserDao;
import com.qlkh.server.handler.Dung;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class LoginServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 10:18 AM
 */
public class LoginHandler extends AbstractHandler<LoginAction, LoginResult> {

    @Autowired
    private UserDao userDao;

    @Override
    public Class<LoginAction> getActionType() {
        return LoginAction.class;
    }

    @Override
    public LoginResult execute(LoginAction action, ExecutionContext context) throws DispatchException {
        Dung dung = new Dung();
        dung.test();
        User user = userDao.findByUserName(action.getUserName());
        if (user != null && user.getPassWord().equals(action.getPassWord())) {
            return new LoginResult(user);
        } else {
            return null;
        }
    }
}
