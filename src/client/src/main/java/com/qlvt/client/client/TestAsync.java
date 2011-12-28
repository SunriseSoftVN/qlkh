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

package com.qlvt.client.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.core.client.model.User;
import com.smvp4g.mvp.client.core.service.RemoteServiceAsync;

/**
 * The Class TestAsync.
 *
 * @author Nguyen Duc Dung
 * @since 12/27/11, 5:42 PM
 */
public interface TestAsync extends RemoteServiceAsync<TestAsync> {
    void test(User user, AsyncCallback<String> async);

    void hello(AsyncCallback<Void> async);
}
