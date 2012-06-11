/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao;

import com.qlkh.core.client.model.TaskDetailNam;
import com.qlkh.server.dao.core.Dao;

/**
 * The Class TaskDetailNamDao.
 *
 * @author Nguyen Duc Dung
 * @since 6/11/12, 10:07 PM
 */
public interface TaskDetailNamDao extends Dao<TaskDetailNam> {
    TaskDetailNam findByTaskIdAndBranchId(long taskId, long branchId);
}
