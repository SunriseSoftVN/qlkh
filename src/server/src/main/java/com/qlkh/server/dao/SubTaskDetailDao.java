/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao;

import com.qlkh.core.client.model.SubTaskDetail;
import com.qlkh.server.dao.core.Dao;

import java.util.List;

/**
 * The Class SubTaskDetailDao.
 *
 * @author Nguyen Duc Dung
 * @since 1/3/12, 8:38 PM
 */
public interface SubTaskDetailDao extends Dao<SubTaskDetail> {
    SubTaskDetail findByTaskDetaiIdAndBranchId(long taskDetailId, long branchId);
    void deleteSubTaskByTaskDetaiIdAndBrandIds(long taskDetailId, List<Long> branchIds);
    List<SubTaskDetail> findBrandId(long brandId);
    List<SubTaskDetail> findByTaskDetailId(long taskDetailId);
}
