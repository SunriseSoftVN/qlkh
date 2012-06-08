/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao;

import com.qlkh.core.client.model.User;
import com.qlkh.server.dao.core.Dao;

import java.util.List;

/**
 * The Class UserDao.
 *
 * @author Nguyen Duc Dung
 * @since 12/27/11, 6:42 PM
 */
public interface UserDao extends Dao<User> {
    User findByUserName(String userName);
    List<User> findByStationId(long stationId);
    List<User> findByStationIds(List<Long> stationIds);
}
