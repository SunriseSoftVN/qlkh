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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.qlvt.client.client.utils.ServiceUtils;
import com.qlvt.core.client.model.Branch;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.smvp4g.mvp.client.core.service.RemoteService;

import java.util.List;

/**
 * The Class BranchService.
 *
 * @author Nguyen Duc Dung
 * @since 1/2/12, 12:55 PM
 */
@RemoteServiceRelativePath("Branch")
public interface BranchService extends RemoteService<BranchService> {
    
    void deleteBranchById(long branchId);
    void deleteBranchByIds(List<Long> branchIds);
    void updateBranchs(List<Branch> branchs);
    List<Branch> getBranchByStationId(long stationId);
    List<Branch> getAllBranch();
    PagingLoadResult<List<Branch>> getBranchsForGrid(PagingLoadConfig config);

    public static class App {
        private static final BranchServiceAsync ourInstance = (BranchServiceAsync) GWT.create(BranchService.class);

        public static BranchServiceAsync getInstance() {
            ServiceUtils.configureServiceEntryPoint(BranchService.class, ourInstance);
            return ourInstance;
        }
    }
}
