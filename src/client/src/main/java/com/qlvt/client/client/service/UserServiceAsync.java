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

package com.qlvt.client.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.core.client.model.User;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.smvp4g.mvp.client.core.service.RemoteServiceAsync;

import java.util.List;

public interface UserServiceAsync extends RemoteServiceAsync<UserServiceAsync> {
    void getUsersForGrid(PagingLoadConfig config, AsyncCallback<PagingLoadResult<List<User>>> async);
    void deleteUserById(long userId, AsyncCallback<Void> async);
    void deleteUserByIds(List<Long> userIds, AsyncCallback<Void> async);
    void updateUser(User user, AsyncCallback<Void> async);
    void updateUsers(List<User> users, AsyncCallback<Void> async);
}
