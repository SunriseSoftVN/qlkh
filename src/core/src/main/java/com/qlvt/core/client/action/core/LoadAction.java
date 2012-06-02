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

package com.qlvt.core.client.action.core;

import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 11:37 AM
 */
public class LoadAction implements Action<LoadResult> {

    private String entityName;
    private Long id;
    private LoadActionType loadType;

    public LoadAction() {
    }

    public LoadAction(String entityName) {
        this.entityName = entityName;
        this.loadType = LoadActionType.ALL;
    }

    public LoadAction(String entityName, Long id) {
        this.entityName = entityName;
        this.id = id;
        this.loadType = LoadActionType.BY_ID;
    }

    public String getEntityName() {
        return entityName;
    }

    public LoadActionType getLoadType() {
        return loadType;
    }

    public Long getId() {
        return id;
    }

    public enum LoadActionType {
        ALL, BY_ID
    }
}
