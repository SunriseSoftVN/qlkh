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

package com.qlvt.server.handler.task;

import com.qlvt.core.client.action.task.LoadUnusedTaskGridAction;
import com.qlvt.core.client.action.task.LoadUnusedTaskGridResult;
import com.qlvt.server.dao.TaskDao;
import com.qlvt.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class LoadUnusedTaskGridHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/12, 4:53 PM
 */
public class LoadUnusedTaskGridHandler extends AbstractHandler<LoadUnusedTaskGridAction, LoadUnusedTaskGridResult> {

    @Autowired
    private TaskDao taskDao;

    @Override
    public Class<LoadUnusedTaskGridAction> getActionType() {
        return LoadUnusedTaskGridAction.class;
    }

    @Override
    public LoadUnusedTaskGridResult execute(LoadUnusedTaskGridAction action, ExecutionContext context) throws DispatchException {
        return new LoadUnusedTaskGridResult(taskDao.getUnusedTask(action.getStationId(),
                action.getTypeEnum(), action.getConfig()));
    }
}
