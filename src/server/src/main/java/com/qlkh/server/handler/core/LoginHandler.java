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

package com.qlkh.server.handler.core;

import com.qlkh.core.client.action.core.LoginAction;
import com.qlkh.core.client.action.core.LoginResult;
import com.qlkh.core.client.model.User;
import com.qlkh.server.dao.UserDao;
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
        User user = userDao.findByUserName(action.getUserName());
        if (user != null && user.getPassWord().equals(action.getPassWord())) {
            return new LoginResult(user);
        } else {
            return null;
        }
    }
}
