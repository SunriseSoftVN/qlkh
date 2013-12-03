/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.worker;

import com.qlkh.core.client.constant.TaskTypeEnum;
import com.qlkh.core.client.model.*;
import com.qlkh.server.dao.TaskDetailDKDao;
import com.qlkh.server.dao.TaskDetailNamDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.util.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class CopyTaskDataWorker.
 *
 * @author Nguyen Duc Dung
 * @since 12/3/13 9:10 AM
 */
public class CopyTaskDataWorker implements Worker {

    private Logger logger = LoggerFactory.getLogger(CopyTaskDataWorker.class);

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private TaskDetailDKDao taskDetailDKDao;

    @Autowired
    private TaskDetailNamDao taskDetailNamDao;

    @Override
    public void workForMe() {
        List<Task> tasks = generalDao.getAll(Task.class);
        List<Station> stations = generalDao.getAll(Station.class);
        List<Branch> branches = generalDao.getAll(Branch.class);

        List<TaskDetailDK> taskDetailDKs = new ArrayList<TaskDetailDK>();
        List<TaskDetailNam> taskDetailNams = new ArrayList<TaskDetailNam>();

        long start = System.currentTimeMillis();

        for (Task task : tasks) {
            for (Station station : stations) {
                for (Branch branch : branches) {
                    if (branch.getStation().getId().equals(station.getId())) {
                        if (task.getTaskTypeCode() == TaskTypeEnum.DK.getCode()) {
                            TaskDetailDK taskDetailDK = taskDetailDKDao.
                                    findByTaskIdAndBranchId(task.getId(), branch.getId(), DateTimeUtils.getCurrentYear());
                            if (taskDetailDK == null) {
                                taskDetailDK = new TaskDetailDK();
                                taskDetailDK.setYear(DateTimeUtils.getCurrentYear());
                                taskDetailDK.setTask(task);
                                taskDetailDK.setBranch(branch);
                                taskDetailDK.setCreateBy(1l);
                                taskDetailDK.setUpdateBy(1l);

                                //Copy value from last year
                                TaskDetailDK taskDetailDKLastYear = taskDetailDKDao.
                                        findByTaskIdAndBranchId(task.getId(), branch.getId(), DateTimeUtils.getLastYear());

                                if (taskDetailDKLastYear != null) {
                                    taskDetailDK.setLastYearValue(taskDetailDKLastYear.getRealValue());
                                    taskDetailDKs.add(taskDetailDK);
                                }
                            }
                        } else if (task.getTaskTypeCode() == TaskTypeEnum.NAM.getCode()) {
                            TaskDetailNam taskDetailNam = taskDetailNamDao.
                                    findByTaskIdAndBranchId(task.getId(), branch.getId(), DateTimeUtils.getCurrentYear());
                            if (taskDetailNam == null) {
                                taskDetailNam = new TaskDetailNam();
                                taskDetailNam.setYear(DateTimeUtils.getCurrentYear());
                                taskDetailNam.setTask(task);
                                taskDetailNam.setBranch(branch);
                                taskDetailNam.setCreateBy(1l);
                                taskDetailNam.setUpdateBy(1l);

                                //Copy value from last year
                                TaskDetailNam taskDetailNamLastYear = taskDetailNamDao.
                                        findByTaskIdAndBranchId(task.getId(), branch.getId(), DateTimeUtils.getLastYear());
                                if (taskDetailNamLastYear != null) {
                                    taskDetailNam.setLastYearValue(taskDetailNamLastYear.getRealValue());
                                    taskDetailNams.add(taskDetailNam);
                                }
                            }
                        }
                    }
                }
            }
        }

        long time = System.currentTimeMillis() - start;

        logger.info("Copy data in " + time + "ms");

        generalDao.saveOrUpdate(taskDetailDKs);
        generalDao.saveOrUpdate(taskDetailNams);
    }
}
