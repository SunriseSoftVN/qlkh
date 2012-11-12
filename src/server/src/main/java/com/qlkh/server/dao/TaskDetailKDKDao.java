/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao;

import com.qlkh.core.client.model.TaskDetailKDK;
import com.qlkh.server.dao.core.Dao;

import java.util.List;

/**
 * The Class SubTaskDetailDao.
 *
 * @author Nguyen Duc Dung
 * @since 1/3/12, 8:38 PM
 */
public interface TaskDetailKDKDao extends Dao<TaskDetailKDK> {
    TaskDetailKDK findByTaskIdAndBranchId(long taskDetailId, long branchId, int year);
}
