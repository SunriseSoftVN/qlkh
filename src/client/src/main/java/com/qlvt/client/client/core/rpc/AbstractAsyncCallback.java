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

package com.qlvt.client.client.core.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.client.client.utils.DiaLogUtils;

/**
 * The Class AbstractAsyncCallback. Using to show dialog message and log RPC error message.
 *
 * @author dungvn3000
 * @since 6/27/11, 7:50 PM
 */
public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T> {

    @Override
    public void onFailure(Throwable caught) {
        DiaLogUtils.logAndShowRpcErrorMessage(caught);
    }

}
