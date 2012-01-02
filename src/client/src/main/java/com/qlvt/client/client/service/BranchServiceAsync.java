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

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.core.client.model.Branch;
import com.smvp4g.mvp.client.core.service.RemoteServiceAsync;

import java.util.List;

public interface BranchServiceAsync extends RemoteServiceAsync<BranchServiceAsync> {
    void deleteBranchById(long branchId, AsyncCallback<Void> async);

    void deleteBranchByIds(List<Long> branchIds, AsyncCallback<Void> async);

    void updateBranchs(List<Branch> branchs, AsyncCallback<Void> async);

    void getAllBranch(AsyncCallback<List<Branch>> async);

    void getBranchsForGrid(BasePagingLoadConfig config, AsyncCallback<BasePagingLoadResult<List<Branch>>> async);

    void getBranchByStationId(long stationId, AsyncCallback<List<Branch>> async);
}
