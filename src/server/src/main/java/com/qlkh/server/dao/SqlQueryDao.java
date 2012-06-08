/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao;

import com.qlkh.core.client.model.view.SubAnnualTaskDetailDataView;
import com.qlkh.core.client.model.view.SubTaskDetailDataView;
import com.qlkh.core.client.model.view.TaskDetailDataView;
import com.qlkh.server.dao.core.Dao;

import java.util.List;

/**
 * The Class SqlQueryDao.
 *
 * @author Nguyen Duc Dung
 * @since 6/6/12, 9:58 PM
 */
public interface SqlQueryDao extends Dao {
    List<SubTaskDetailDataView> getSubTaskDetailDataViews(List<Long> stationIds, int year);
    List<SubAnnualTaskDetailDataView> getSubAnnualTaskDetailDataViews(List<Long> stationIds, int year);
    List<TaskDetailDataView> getTaskDetailViews(int year);
}
