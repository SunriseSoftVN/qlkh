/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.core

import com.qlkh.core.client.action.core.LoginAction
import net.customware.gwt.dispatch.server.Dispatch
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional

import static groovy.util.GroovyTestCase.assertEquals
import com.smvp4g.mvp.client.core.utils.LoginUtils

import static com.smvp4g.mvp.client.core.utils.LoginUtils.md5hash
import static groovy.util.GroovyTestCase.assertEquals
import com.qlkh.core.client.constant.UserRoleEnum

import static com.qlkh.core.client.constant.UserRoleEnum.ADMIN

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
class LoginHandlerTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private Dispatch dispatch;

    @Test
    void testLoginWithNotExistUser() {
        def result = dispatch.execute(new LoginAction("unknown", "i don't know"));
        assertEquals(result, null);
    }

    @Test
    void testUserWithWrongPassWord() {
        def result = dispatch.execute(new LoginAction("admin", "i don't know"));
        assertEquals(result, null);
    }

    @Test
    void testLoginWithAdmin() {
        def result = dispatch.execute(new LoginAction("admin", md5hash("admin")));
        assertEquals(result.user.userName, "admin");
        assertEquals(result.user.userRole, ADMIN.role);
    }

}
