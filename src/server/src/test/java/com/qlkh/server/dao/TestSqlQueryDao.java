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
