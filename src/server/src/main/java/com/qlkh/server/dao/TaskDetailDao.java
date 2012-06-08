/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao;

import com.qlkh.core.client.model.TaskDetail;
import com.qlkh.server.dao.core.Dao;

import java.util.List;

/**
 * The Class TaskDetailDao.
 *
 * @author Nguyen Duc Dung
 * @since 1/1/12, 3:49 PM
 */
public interface TaskDetailDao extends Dao<TaskDetail> {
    List<TaskDetail> findByStationId(long stationId);

    /**
     * Find TaskDetail in current year.
     * @param stationId Station Id
     * @param taskId Task Id
     * @return null if not found.
     */
    TaskDetail findCurrentByStationIdAndTaskId(long stationId, long taskId);
}
