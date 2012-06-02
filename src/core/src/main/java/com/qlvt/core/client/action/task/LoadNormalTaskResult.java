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

package com.qlvt.core.client.action.task;

import com.qlvt.core.client.model.Task;
import net.customware.gwt.dispatch.shared.Result;

import java.util.List;

/**
 * The Class LoadNormalTaskResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/2/12, 12:19 PM
 */
public class LoadNormalTaskResult implements Result {

    private List<Task> tasks;

    public LoadNormalTaskResult() {
    }

    public LoadNormalTaskResult(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}