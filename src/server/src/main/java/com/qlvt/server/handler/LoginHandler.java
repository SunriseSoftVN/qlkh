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

package com.qlvt.server.handler;

import com.qlvt.core.client.action.LoginAction;
import com.qlvt.core.client.action.LoginResult;
import com.qlvt.server.dao.TestDao;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * The Class LoginHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 1:58 AM
 */
public class LoginHandler implements ActionHandler<LoginAction, LoginResult> {

    private TestDao testDao;

    @Override
    public Class<LoginAction> getActionType() {
        return LoginAction.class;
    }

    @Override
    public LoginResult execute(LoginAction action, ExecutionContext context) throws DispatchException {
        System.out.println("dung ne");
        System.out.println(testDao);
        testDao.test();
        return new LoginResult(true);
    }

    @Override
    public void rollback(LoginAction action, LoginResult result, ExecutionContext context) throws DispatchException {
    }

    public void setTestDao(TestDao testDao) {
        this.testDao = testDao;
    }
}
