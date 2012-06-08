/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao;

import com.qlkh.core.client.model.Branch;
import com.qlkh.server.dao.core.Dao;

import java.util.List;

/**
 * The Class BranchDao.
 *
 * @author Nguyen Duc Dung
 * @since 1/2/12, 12:53 PM
 */
public interface BranchDao extends Dao<Branch> {
    List<Branch> findByStationId(long stationId);
}
