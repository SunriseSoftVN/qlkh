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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.qlvt.client.client.service.BranchService;
import com.qlvt.core.client.model.Branch;
import com.qlvt.server.dao.BranchDao;
import com.qlvt.server.service.core.AbstractService;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import java.util.List;

/**
 * The Class BranchServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 1/2/12, 12:59 PM
 */
@Singleton
public class BranchServiceImpl extends AbstractService implements BranchService {

    @Inject
    private BranchDao branchDao;

    @Override
    public void deleteBranchById(long branchId) {
        branchDao.deleteById(Branch.class, branchId);
    }

    @Override
    public void deleteBranchByIds(List<Long> branchIds) {
        branchDao.deleteByIds(Branch.class, branchIds);
    }

    @Override
    public void updateBranchs(List<Branch> branchs) {
        branchDao.saveOrUpdate(branchs);
    }

    @Override
    public List<Branch> getAllBranch() {
        return branchDao.getAll(Branch.class);
    }

    @Override
    public PagingLoadResult<List<Branch>> getBranchsForGrid(PagingLoadConfig config) {
        List<Branch> branches = branchDao.getByBeanConfig(Branch.class, config);
        return new PagingLoadResultBean(branches, config.getOffset(), branchDao.count(Branch.class));
    }

    @Override
    public List<Branch> getBranchByStationId(long stationId) {
        return branchDao.getBranchsByStationId(stationId);
    }
}
