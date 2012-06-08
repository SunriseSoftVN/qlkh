/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao;

import com.qlkh.core.client.model.SubTaskAnnualDetail;
import com.qlkh.server.dao.core.Dao;

import java.util.List;

/**
 * The Class SubTaskAnnualDetailDao.
 *
 * @author Nguyen Duc Dung
 * @since 1/4/12, 9:08 PM
 */
public interface SubTaskAnnualDetailDao extends Dao<SubTaskAnnualDetail> {
    SubTaskAnnualDetail findByTaskDetaiIdAndBranchId(long taskDetailId, long branchId);
    void deleteSubAnnualTaskByTaskDetaiIdAndBrandIds(long taskDetailId, List<Long> branchIds);
    List<SubTaskAnnualDetail> findByBrandId(long brandId);
    List<SubTaskAnnualDetail> findByTaskDetailId(long taskDetailId);
}
