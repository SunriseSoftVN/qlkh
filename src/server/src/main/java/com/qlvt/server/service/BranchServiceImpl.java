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

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.qlvt.client.client.service.BranchService;
import com.qlvt.core.client.exception.DeleteException;
import com.qlvt.core.client.model.Branch;
import com.qlvt.server.guice.DaoProvider;
import com.qlvt.server.service.core.AbstractService;
import com.qlvt.server.transaction.Transaction;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * The Class BranchServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 1/2/12, 12:59 PM
 */
@Transaction
@Singleton
public class BranchServiceImpl extends AbstractService implements BranchService {

    @Inject
    private DaoProvider provider;

    @Override
    public void deleteBranchById(long branchId) throws DeleteException {
        if(CollectionUtils.isEmpty(provider.getSubTaskDetailDao().findBrandId(branchId))
                && CollectionUtils.isEmpty(provider.getSubTaskAnnualDetailDao().findByBrandId(branchId))) {
            provider.getBranchDao().deleteById(Branch.class, branchId);
        } else {
            throw new DeleteException();
        }
    }

    @Override
    public void deleteBranchByIds(List<Long> branchIds) throws DeleteException {
        for (Long brandId : branchIds) {
            deleteBranchById(brandId);
        }
    }

    @Override
    public void updateBranchs(List<Branch> branchs) {
        provider.getBranchDao().saveOrUpdate(branchs);
    }

    @Override
    public Branch updateBranch(Branch branch) {
        return provider.getBranchDao().saveOrUpdate(branch);
    }

    @Override
    public List<Branch> getAllBranch() {
        return provider.getBranchDao().getAll(Branch.class);
    }

    @Override
    public BasePagingLoadResult<Branch> getBranchsForGrid(BasePagingLoadConfig config) {
       return provider.getBranchDao().getByBeanConfig(Branch.class, config);
    }

    @Override
    public List<Branch> getBranchByStationId(long stationId) {
        return provider.getBranchDao().findByStationId(stationId);
    }
}
