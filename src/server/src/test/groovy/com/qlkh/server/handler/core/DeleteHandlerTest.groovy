/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.core

import com.qlkh.core.client.action.core.DeleteAction
import com.qlkh.core.client.constant.UserRoleEnum
import com.qlkh.core.client.model.User
import com.qlkh.server.dao.core.GeneralDao
import net.customware.gwt.dispatch.server.Dispatch

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional

import static org.junit.Assert.assertEquals

/**
 * The Class DeleteHandlerTest.
 *
 * @author Nguyen Duc Dung
 * @since 6/10/12, 9:30 AM
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration
@Transactional
class DeleteHandlerTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private Dispatch dispatch;

    @Autowired
    private GeneralDao generalDao;

    private User dummyUser;

    @Before
    void init() {
        dummyUser = new User();
        dummyUser.setUserName("dummy");
        dummyUser.setPassWord("dummy");
        dummyUser.setUserRole(UserRoleEnum.ADMIN.role);
        dummyUser.setCreateBy(1);
        dummyUser.setUpdateBy(1);
        generalDao.saveOrUpdate(dummyUser);
    }

    def wasUserDeleted = {
        //Reload to check whether user is still exist on the database or not.
        def reloadUser = generalDao.findById(User.name, dummyUser.id);
        assertEquals(reloadUser, null);
    }

    @Test
    void testDeleteByEntity() {
        def result = dispatch.execute(new DeleteAction(dummyUser));
        assertEquals(result.result, true);
        wasUserDeleted();
    }

    @Test
    void testDeleteById() {
        def result = dispatch.execute(new DeleteAction(User.name, dummyUser.id));
        assertEquals(result.result, true);
        wasUserDeleted();
    }

    @Test
    void testDeleteByIds() {
        def userIds = [dummyUser.id];
        def result = dispatch.execute(new DeleteAction(User.name, userIds));
        assertEquals(result.result, true);
        wasUserDeleted();
    }
}
