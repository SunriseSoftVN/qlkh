/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.task;

import com.qlkh.core.client.action.task.SaveTaskDefaultValueAction;
import com.qlkh.core.client.action.task.SaveTaskDefaultValueResult;
import com.qlkh.core.client.model.TaskDefaultValue;
import com.qlkh.server.dao.SettingDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ch.lambdaj.Lambda.*;
import static com.qlkh.server.util.DateTimeUtils.getCurrentQuarter;
import static com.qlkh.server.util.DateTimeUtils.getCurrentYear;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * The Class SaveTaskDefaultValueHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/12, 10:29 PM
 */
public class SaveTaskDefaultValueHandler extends AbstractHandler<SaveTaskDefaultValueAction, SaveTaskDefaultValueResult> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private SettingDao settingDao;

    @Override
    public Class<SaveTaskDefaultValueAction> getActionType() {
        return SaveTaskDefaultValueAction.class;
    }

    @Override
    public SaveTaskDefaultValueResult execute(SaveTaskDefaultValueAction action, ExecutionContext context) throws DispatchException {
        List<TaskDefaultValue> taskDefaultValues = generalDao.findCriteria(TaskDefaultValue.class,
                Restrictions.eq("task.id", action.getTask().getId()),
                Restrictions.eq("quarter", getCurrentQuarter(settingDao).getCode()),
                Restrictions.eq("year", getCurrentYear(settingDao)));
        TaskDefaultValue taskDefaultValue;
        if (CollectionUtils.isNotEmpty(taskDefaultValues)) {
            taskDefaultValue = selectUnique(taskDefaultValues,
                    having(on(TaskDefaultValue.class).getYear(), equalTo(getCurrentYear(settingDao))).
                            and(having(on(TaskDefaultValue.class).getQuarter(), equalTo(getCurrentQuarter(settingDao).getCode()))));
        } else {
            taskDefaultValue = new TaskDefaultValue();
            taskDefaultValue.setTask(action.getTask());
            taskDefaultValue.setQuarter(getCurrentQuarter(settingDao).getCode());
            taskDefaultValue.setYear(getCurrentYear(settingDao));
            taskDefaultValue.setCreateBy(1l);
            taskDefaultValue.setUpdateBy(1l);
            taskDefaultValue.setDefaultValue(action.getTask().getDefaultValue());
            generalDao.saveOrUpdate(taskDefaultValue);
        }

        taskDefaultValue.getTask().setDefaultValue(action.getDefaultValue());
        generalDao.saveOrUpdate(taskDefaultValue.getTask());
        return new SaveTaskDefaultValueResult(taskDefaultValue.getTask());
    }
}
