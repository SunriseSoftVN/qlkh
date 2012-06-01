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

package com.qlvt.server.service;

import com.qlvt.core.client.action.LoadAction;
import com.qlvt.core.client.action.LoadResult;
import com.qlvt.server.dao.core.GeneralDao;
import com.qlvt.server.service.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class LoadHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 11:41 AM
 */
public class LoadHandler extends AbstractHandler<LoadAction, LoadResult> {

    @Autowired
    private GeneralDao generalDaoImpl;

    @Override
    public Class<LoadAction> getActionType() {
        return LoadAction.class;
    }

    @Override
    public LoadResult execute(LoadAction action, ExecutionContext context) throws DispatchException {
        if (action.getLoadType() == LoadAction.LoadActionType.ALL) {
            return new LoadResult(generalDaoImpl.getAll(action.getEntityName()));
        } else if (action.getLoadType() == LoadAction.LoadActionType.BY_ID) {
            return new LoadResult(generalDaoImpl.findById(action.getEntityName(), action.getId()));
        }
        return null;
    }
}
