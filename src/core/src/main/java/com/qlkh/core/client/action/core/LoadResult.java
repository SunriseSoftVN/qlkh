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

package com.qlkh.core.client.action.core;

import com.qlkh.core.client.model.core.AbstractEntity;
import net.customware.gwt.dispatch.shared.Result;

import java.util.List;

/**
 * The Class LoadResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 11:38 AM
 */
public class LoadResult implements Result {

    private List<? extends AbstractEntity> list;

    private AbstractEntity result;

    public LoadResult() {
    }

    public LoadResult(List<? extends AbstractEntity> list) {
        this.list = list;
    }

    public LoadResult(AbstractEntity result) {
        this.result = result;
    }

    @SuppressWarnings("unchecked")
    public <E extends AbstractEntity> E getResult() {
        return (E) result;
    }

    @SuppressWarnings("unchecked")
    public <E extends AbstractEntity> List<E> getList() {
        return (List<E>) list;
    }
}
