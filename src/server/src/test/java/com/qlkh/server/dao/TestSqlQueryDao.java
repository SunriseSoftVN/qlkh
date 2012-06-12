/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao;

import com.qlkh.core.client.model.Station;
import com.qlkh.core.client.model.view.TaskDetailDKDataView;
import com.qlkh.core.client.model.view.TaskDetailKDKDataView;
import com.qlkh.core.client.model.view.TaskDetailNamDataView;
import com.qlkh.server.dao.core.GeneralDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static org.junit.Assert.assertEquals;

/**
 * The Class TestSqlQueryDao.
 *
 * @author Nguyen Duc Dung
 * @since 6/12/12, 7:54 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration
@Transactional
public class TestSqlQueryDao extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private SqlQueryDao sqlQueryDao;

    @Test(timeout = 1000)
    public void testGetTaskDetailDK() {
        List<Station> stations = generalDao.getAll(Station.class);
        List<Long> stationIds = extract(stations, on(Station.class).getId());
        List<TaskDetailDKDataView> list = sqlQueryDao.getTaskDetailDK(stationIds, 2012);
        assertEquals(list.size() > 0, true);
    }

    @Test(timeout = 1000)
    public void testGetTaskDetailKDK() {
        List<Station> stations = generalDao.getAll(Station.class);
        List<Long> stationIds = extract(stations, on(Station.class).getId());
        List<TaskDetailKDKDataView> list = sqlQueryDao.getTaskDetailKDK(stationIds, 2012);
        assertEquals(list.size() > 0, true);
    }

    @Test(timeout = 1000)
    public void testGetTaskDetailNam() {
        List<Station> stations = generalDao.getAll(Station.class);
        List<Long> stationIds = extract(stations, on(Station.class).getId());
        List<TaskDetailNamDataView> list = sqlQueryDao.getTaskDetailNam(stationIds, 2012);
        assertEquals(list.size() > 0, true);
    }

}
