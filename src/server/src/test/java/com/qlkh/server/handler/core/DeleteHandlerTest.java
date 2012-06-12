/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.core;

import com.qlkh.core.client.action.core.DeleteAction;
import com.qlkh.core.client.action.core.DeleteResult;
import com.qlkh.core.client.model.User;
import com.qlkh.server.dao.core.GeneralDao;
import net.customware.gwt.dispatch.server.Dispatch;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.qlkh.core.client.constant.UserRoleEnum.ADMIN;
import static org.junit.Assert.assertEquals;

/**
 * The Class DeleteHandlerTest.
 *
 * @author Nguyen Duc Dung
 * @since 6/10/12, 9:30 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration
@Transactional
public class DeleteHandlerTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private Dispatch dispatch;

    @Autowired
    private GeneralDao generalDao;

    private User dummyUser;

    @Before
    public void init() {
        dummyUser = new User();
        dummyUser.setUserName("dummy");
        dummyUser.setPassWord("dummy");
        dummyUser.setUserRole(ADMIN.getRole());
        dummyUser.setCreateBy(1l);
        dummyUser.setUpdateBy(1l);
        generalDao.saveOrUpdate(dummyUser);
    }

    private void wasUserDeleted() {
        //Reload to check whether user is still exist on the database or not.
        User reloadUser = generalDao.findById(User.class.getName(), dummyUser.getId());
        assertEquals(reloadUser, null);
    }

    @Test
    public void testDeleteByEntity() throws DispatchException {
        DeleteResult result = dispatch.execute(new DeleteAction(dummyUser));
        assertEquals(result.isDelete(), true);
        wasUserDeleted();
    }

    @Test
    public void testDeleteById() throws DispatchException {
        DeleteResult result = dispatch.execute(new DeleteAction(User.class.getName(), dummyUser.getId()));
        assertEquals(result.isDelete(), true);
        wasUserDeleted();
    }

    @Test
    public void testDeleteByIds() throws DispatchException {
        List<Long> userIds = new ArrayList<Long>();
        userIds.add(dummyUser.getId());
        DeleteResult result = dispatch.execute(new DeleteAction(User.class.getName(), userIds));
        assertEquals(result.isDelete(), true);
        wasUserDeleted();
    }
}
