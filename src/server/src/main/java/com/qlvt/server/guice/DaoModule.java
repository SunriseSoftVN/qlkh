/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.qlvt.server.guice;

import com.google.inject.AbstractModule;
import com.qlvt.server.dao.StationDao;
import com.qlvt.server.dao.TaskDao;
import com.qlvt.server.dao.TaskDetailDao;
import com.qlvt.server.dao.UserDao;
import com.qlvt.server.dao.impl.StationDaoImpl;
import com.qlvt.server.dao.impl.TaskDaoImpl;
import com.qlvt.server.dao.impl.TaskDetailDaoImpl;
import com.qlvt.server.dao.impl.UserDaoImpl;

/**
 * The Class DaoModule.
 *
 * @author Nguyen Duc Dung
 * @since 8/16/11, 9:41 AM
 */
public class DaoModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserDao.class).to(UserDaoImpl.class);
        bind(StationDao.class).to(StationDaoImpl.class);
        bind(TaskDao.class).to(TaskDaoImpl.class);
        bind(TaskDetailDao.class).to(TaskDetailDaoImpl.class);
    }
}