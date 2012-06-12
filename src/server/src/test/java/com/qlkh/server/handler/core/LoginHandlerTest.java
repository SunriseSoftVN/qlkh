/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.core;

import com.qlkh.core.client.action.core.LoginAction;
import com.qlkh.core.client.action.core.LoginResult;
import net.customware.gwt.dispatch.server.Dispatch;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static com.qlkh.core.client.constant.UserRoleEnum.ADMIN;
import static com.smvp4g.mvp.client.core.utils.LoginUtils.md5hash;
import static org.junit.Assert.assertEquals;

/**
 * The Class LoginHandlerTest.
 *
 * @author Nguyen Duc Dung
 * @since 6/10/12, 8:23 AM
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration
@Transactional
public class LoginHandlerTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private Dispatch dispatch;

    @Test
    public void testLoginWithNotExistUser() throws DispatchException {
        LoginResult result = dispatch.execute(new LoginAction("unknown", "i don't know"));
        assertEquals(result, null);
    }

    @Test
    public void testUserWithWrongPassWord() throws DispatchException {
        LoginResult result = dispatch.execute(new LoginAction("admin", "i don't know"));
        assertEquals(result, null);
    }

    @Test
    public void testLoginWithAdmin() throws DispatchException {
        LoginResult result = dispatch.execute(new LoginAction("admin", md5hash("admin")));
        assertEquals(result.getUser().getUserName(), "admin");
        assertEquals(result.getUser().getUserRole(), ADMIN.getRole());
    }

}
