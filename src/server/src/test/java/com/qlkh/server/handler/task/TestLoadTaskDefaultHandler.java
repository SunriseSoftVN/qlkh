/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.task;

import com.qlkh.core.client.action.task.LoadTaskDefaultAction;
import com.qlkh.core.client.action.task.LoadTaskDefaultResult;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.TaskDefaultValue;
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

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * The Class TestLoadTaskDefaultHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/12, 1:13 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration
@Transactional
public class TestLoadTaskDefaultHandler extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private Dispatch dispatch;

    @Autowired
    private GeneralDao generalDao;

    private Task testTask;

    @Before
    public void inti() {
        testTask = new Task();
        testTask.setName("Task 1");
        testTask.setCode("NOCODE");
        testTask.setDefaultValue(1d);
        testTask.setQuota(1);
        testTask.setUnit("NOUNIT");
        testTask.setCreateBy(1l);
        testTask.setUpdateBy(1l);
        testTask.setCreatedDate(new Date());
        testTask.setUpdatedDate(new Date());
        generalDao.saveOrUpdate(testTask);
    }

    @Test
    public void testWithNoTaskDefault() throws DispatchException {
        LoadTaskDefaultResult result = dispatch.execute(new LoadTaskDefaultAction(testTask.getId()));
        assertEquals(result.getTaskDefaultValue(), null);
    }

    @Test
    public void testWithData() throws DispatchException {
        TaskDefaultValue taskDefaultValue = new TaskDefaultValue();
        taskDefaultValue.setDefaultValue(1d);
        taskDefaultValue.setTask(testTask);
        taskDefaultValue.setCreateBy(1l);
        taskDefaultValue.setUpdateBy(1l);
        taskDefaultValue.setUpdatedDate(new Date());
        taskDefaultValue.setCreatedDate(new Date());
        generalDao.saveOrUpdate(taskDefaultValue);

        LoadTaskDefaultResult result = dispatch.execute(new LoadTaskDefaultAction(testTask.getId()));
        assertEquals(result.getTaskDefaultValue().getDefaultValue(), 1d, 0);
    }

    @Test
    public void testWithOldData() throws DispatchException {
        Calendar cal = Calendar.getInstance();
        cal.set(2012, Calendar.JUNE, 14);

        TaskDefaultValue taskDefaultValue = new TaskDefaultValue();
        taskDefaultValue.setDefaultValue(1d);
        taskDefaultValue.setTask(testTask);
        taskDefaultValue.setCreateBy(1l);
        taskDefaultValue.setUpdateBy(1l);
        taskDefaultValue.setUpdatedDate(cal.getTime());
        taskDefaultValue.setCreatedDate(cal.getTime());
        generalDao.saveOrUpdate(taskDefaultValue);

        LoadTaskDefaultResult result = dispatch.execute(new LoadTaskDefaultAction(testTask.getId()));
        assertEquals(result.getTaskDefaultValue().getDefaultValue(), 1d, 0);
    }

    @Test
    public void testWithNewData() throws DispatchException {
        Calendar cal = Calendar.getInstance();
        cal.set(2012, Calendar.JUNE, cal.getTime().getDate() + 1);

        TaskDefaultValue taskDefaultValue = new TaskDefaultValue();
        taskDefaultValue.setDefaultValue(1d);
        taskDefaultValue.setTask(testTask);
        taskDefaultValue.setCreateBy(1l);
        taskDefaultValue.setUpdateBy(1l);
        taskDefaultValue.setUpdatedDate(cal.getTime());
        taskDefaultValue.setCreatedDate(cal.getTime());
        generalDao.saveOrUpdate(taskDefaultValue);

        LoadTaskDefaultResult result = dispatch.execute(new LoadTaskDefaultAction(testTask.getId()));
        assertEquals(result.getTaskDefaultValue(), null);
    }
}
