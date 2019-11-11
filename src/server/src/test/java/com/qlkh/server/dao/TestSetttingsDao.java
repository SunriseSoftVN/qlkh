package com.qlkh.server.dao;

import com.qlkh.core.client.constant.SettingEnum;
import com.qlkh.core.client.model.Settings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration
@Transactional
public class TestSetttingsDao extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private SettingDao settingDao;

    @Test
    public void testFindByName() {
        Settings settings = settingDao.findByName(SettingEnum.COMPANY_NAME.getName());
        assertNotNull(settings);
    }

}
