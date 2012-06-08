/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao;

import com.qlkh.core.client.model.Station;
import com.qlkh.core.client.model.view.SubAnnualTaskDetailDataView;
import com.qlkh.core.client.model.view.SubTaskDetailDataView;
import com.qlkh.core.client.model.view.TaskDetailDataView;
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
 * The Class TestDao.
 *
 * @author Nguyen Duc Dung
 * @since 6/6/12, 8:29 PM
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
    public void testGetSubAnnualTaskDetailDataViews() {
        List<Station> stations = generalDao.getAll(Station.class);
        List<Long> stationIds = extract(stations, on(Station.class).getId());
        List<SubAnnualTaskDetailDataView> list = sqlQueryDao.getSubAnnualTaskDetailDataViews(stationIds, 2012);
        assertEquals(list.size() > 0, true);
    }

    @Test(timeout = 1000)
    public void testGetTaskDetailViews() {
        List<TaskDetailDataView> taskDetailDataViews = sqlQueryDao.getTaskDetailViews(2012);
        assertEquals(taskDetailDataViews.size() > 0, true);
    }

    @Test(timeout = 1000)
    public void testGetSubTaskDetailDataViews() {
        List<Station> stations = generalDao.getAll(Station.class);
        List<Long> stationIds = extract(stations, on(Station.class).getId());
        List<SubTaskDetailDataView> list = sqlQueryDao.getSubTaskDetailDataViews(stationIds, 2012);
        assertEquals(list.size() > 0, true);
    }

}
