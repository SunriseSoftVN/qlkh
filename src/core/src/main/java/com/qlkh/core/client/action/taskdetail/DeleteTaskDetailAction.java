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

package com.qlkh.core.client.action.taskdetail;

import net.customware.gwt.dispatch.shared.Action;

import java.util.List;

/**
 * The Class DeleteTaskDetailAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 8:46 PM
 */
public class DeleteTaskDetailAction implements Action<DeleteTaskDetailResult> {

    private List<Long> ids;

    private long id;

    public DeleteTaskDetailAction() {
    }

    public DeleteTaskDetailAction(long id) {
        this.id = id;
    }

    public DeleteTaskDetailAction(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }

    public long getId() {
        return id;
    }
}
