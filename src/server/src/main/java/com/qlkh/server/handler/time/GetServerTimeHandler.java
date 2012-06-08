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

package com.qlkh.server.handler.time;

import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import java.util.Date;

/**
 * The Class GetServerTimeHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/2/12, 4:01 PM
 */
public class GetServerTimeHandler extends AbstractHandler<GetServerTimeAction, GetServerTimeResult> {
    @Override
    public Class<GetServerTimeAction> getActionType() {
        return GetServerTimeAction.class;
    }

    @Override
    public GetServerTimeResult execute(GetServerTimeAction action, ExecutionContext context) throws DispatchException {
        return new GetServerTimeResult(1900 + new Date().getYear());
    }
}
