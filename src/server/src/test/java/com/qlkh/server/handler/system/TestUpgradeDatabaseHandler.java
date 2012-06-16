/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.system;

import com.qlkh.core.client.action.system.UpgradeDatabaseAction;
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

/**
 * The Class TestUpgradeDatabaseHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/16/12, 7:34 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration
@Transactional
public class TestUpgradeDatabaseHandler extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private Dispatch dispatch;

    @Test
    public void testUpgrade() throws DispatchException {
        dispatch.execute(new UpgradeDatabaseAction());
    }

}
