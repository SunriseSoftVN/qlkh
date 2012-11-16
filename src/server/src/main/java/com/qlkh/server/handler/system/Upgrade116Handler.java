/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.system;

import com.qlkh.core.client.action.system.Upgrade116Action;
import com.qlkh.core.client.action.system.Upgrade116Result;
import com.qlkh.core.client.constant.TaskTypeEnum;
import com.qlkh.core.client.model.*;
import com.qlkh.server.dao.TaskDetailDKDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.util.DateTimeUtils;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * The Class Upgrade116Handler.
 *
 * @author Nguyen Duc Dung
 * @since 11/16/12, 2:27 PM
 */
public class Upgrade116Handler extends AbstractHandler<Upgrade116Action, Upgrade116Result> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private TaskDetailDKDao taskDetailDKDao;

    @Override
    public Class<Upgrade116Action> getActionType() {
        return Upgrade116Action.class;
    }

    @Override
    public Upgrade116Result execute(Upgrade116Action action, ExecutionContext context) throws DispatchException {
        List<Station> stations = generalDao.getAll(Station.class);
        List<Task> tasks = generalDao.getAll(Task.class);
        List<Branch> branches = generalDao.getAll(Branch.class);

        List<TaskDetailDK> taskDetailDKs = generalDao.getAll(TaskDetailDK.class);
        List<TaskDetailNam> taskDetailNams = generalDao.getAll(TaskDetailNam.class);

        List<TaskDetailDK> newTaskDKs = new ArrayList<TaskDetailDK>();
        List<TaskDetailNam> newTaskNams = new ArrayList<TaskDetailNam>();

        for (Task task : tasks) {
            for (Station station : stations) {
                if (!station.isCompany()) {
                    for (Branch branch : branches) {
                        if (task.getTaskTypeCode() == TaskTypeEnum.DK.getCode() && branch.getStation().getId().equals(station.getId())) {

                            TaskDetailDK taskDetailDK = selectUnique(taskDetailDKs,
                                    having(on(TaskDetailDK.class).getBranch().getId(), equalTo(branch.getId()))
                                            .and(
                                                    having(on(TaskDetailDK.class).getTask().getId(), equalTo(task.getId()))
                                            )
                                            .and(
                                                    having(on(TaskDetailDK.class).getYear(), equalTo(DateTimeUtils.getCurrentYear()))
                                            )
                            );

                            if (taskDetailDK == null) {
                                taskDetailDK = new TaskDetailDK();
                                taskDetailDK.setYear(DateTimeUtils.getCurrentYear());
                                taskDetailDK.setTask(task);
                                taskDetailDK.setBranch(branch);
                                taskDetailDK.setCreateBy(1l);
                                taskDetailDK.setUpdateBy(1l);

                                //Copy value from last year
                                TaskDetailDK taskDetailDKLastYear = selectUnique(taskDetailDKs,
                                        having(on(TaskDetailDK.class).getBranch().getId(), equalTo(branch.getId()))
                                                .and(
                                                        having(on(TaskDetailDK.class).getTask().getId(), equalTo(task.getId()))
                                                )
                                                .and(
                                                        having(on(TaskDetailDK.class).getYear(), equalTo(DateTimeUtils.getLastYear()))
                                                )
                                );

                                if (taskDetailDKLastYear != null) {
                                    taskDetailDK.setLastYearValue(taskDetailDKLastYear.getRealValue());

                                    //Ony save when it have last year value
                                    newTaskDKs.add(taskDetailDK);
                                }
                            }
                        }

                        if (task.getTaskTypeCode() == TaskTypeEnum.NAM.getCode() && branch.getStation().getId().equals(station.getId())) {

                            TaskDetailNam taskDetailNam = selectUnique(taskDetailNams,
                                    having(on(TaskDetailNam.class).getBranch().getId(), equalTo(branch.getId()))
                                            .and(
                                                    having(on(TaskDetailNam.class).getTask().getId(), equalTo(task.getId()))
                                            )
                                            .and(
                                                    having(on(TaskDetailNam.class).getYear(), equalTo(DateTimeUtils.getCurrentYear()))
                                            )
                            );

                            if (taskDetailNam == null) {
                                taskDetailNam = new TaskDetailNam();
                                taskDetailNam.setYear(DateTimeUtils.getCurrentYear());
                                taskDetailNam.setTask(task);
                                taskDetailNam.setBranch(branch);
                                taskDetailNam.setCreateBy(1l);
                                taskDetailNam.setUpdateBy(1l);

                                //Copy value from last year
                                TaskDetailNam taskDetailNamLastYear = selectUnique(taskDetailNams,
                                        having(on(TaskDetailNam.class).getBranch().getId(), equalTo(branch.getId()))
                                                .and(
                                                        having(on(TaskDetailNam.class).getTask().getId(), equalTo(task.getId()))
                                                )
                                                .and(
                                                        having(on(TaskDetailNam.class).getYear(), equalTo(DateTimeUtils.getLastYear()))
                                                )
                                );

                                if (taskDetailNamLastYear != null) {
                                    taskDetailNam.setLastYearValue(taskDetailNamLastYear.getRealValue());

                                    //Ony save when it have last year value
                                    newTaskNams.add(taskDetailNam);
                                }
                            }
                        }
                    }
                }
            }
        }

        generalDao.saveOrUpdate(newTaskDKs);
        generalDao.saveOrUpdate(newTaskNams);

        return new Upgrade116Result();
    }
}
