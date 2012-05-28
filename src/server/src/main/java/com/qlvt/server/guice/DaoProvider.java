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

import com.qlvt.server.dao.*;
import com.qlvt.server.dao.impl.*;

/**
 * The Class UserDaoProvider.
 *
 * @author Nguyen Duc Dung
 * @since 5/28/12, 1:21 PM
 */
public class DaoProvider extends AbstractDaoProvider {

    public UserDao getUserDao() {
        return new UserDaoImpl();
    }

    public BranchDao getBranchDao() {
        return new BranchDaoImpl();
    }

    public StationDao getStationDao() {
        return new StationDaoImpl();
    }

    public TaskDao getTaskDao() {
        return new TaskDaoImpl();
    }

    public TaskDetailDao getTaskDetailDao() {
        return new TaskDetailDaoImpl();
    }

    public SubTaskAnnualDetailDao getSubTaskAnnualDetailDao() {
        return new SubTaskAnnualDetailDaoImpl();
    }

    public SubTaskDetailDao getSubTaskDetailDao() {
        return new SubTaskDetailDaoImpl();
    }
}
