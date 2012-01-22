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

package com.qlvt.server.dao;

import com.qlvt.core.client.model.SubTaskAnnualDetail;
import com.qlvt.server.dao.core.Dao;

import java.util.List;

/**
 * The Class SubTaskAnnualDetailDao.
 *
 * @author Nguyen Duc Dung
 * @since 1/4/12, 9:08 PM
 */
public interface SubTaskAnnualDetailDao extends Dao<SubTaskAnnualDetail> {
    SubTaskAnnualDetail findByTaskDetaiIdAndBranchId(long taskDetailId, long branchId);
    void deleteSubAnnualTaskByTaskDetaiIdAndBrandIds(long taskDetailId, List<Long> branchIds);
    List<SubTaskAnnualDetail> findBrandId(long brandId);
}
