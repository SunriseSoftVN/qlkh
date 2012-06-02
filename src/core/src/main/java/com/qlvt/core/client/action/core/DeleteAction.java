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

import com.qlvt.core.client.model.core.AbstractEntity;
import net.customware.gwt.dispatch.shared.Action;

import java.util.List;

/**
 * The Class DeleteAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 2:03 PM
 */
public class DeleteAction implements Action<DeleteResult> {

    private AbstractEntity entity;
    private String entityName;
    private List<Long> ids;
    private long id;
    private String[] relateEntityNames;
    private DeleteActionType actionType;

    public DeleteAction() {
    }

    public <E extends AbstractEntity> DeleteAction(E entity, String... relateEntityNames) {
        this.entity = entity;
        this.relateEntityNames = relateEntityNames;
        this.actionType = DeleteActionType.BY_ENTITY;
    }

    public DeleteAction(String entityName, long id, String... relateEntityNames) {
        this.entityName = entityName;
        this.id = id;
        this.relateEntityNames = relateEntityNames;
        this.actionType = DeleteActionType.BY_ID;
    }

    public DeleteAction(String entityName, List<Long> ids, String... relateEntityNames) {
        this.entityName = entityName;
        this.ids = ids;
        this.relateEntityNames = relateEntityNames;
        this.actionType = DeleteActionType.BY_IDS;
    }

    public AbstractEntity getEntity() {
        return entity;
    }

    public String getEntityName() {
        return entityName;
    }

    public long getId() {
        return id;
    }

    public DeleteActionType getActionType() {
        return actionType;
    }

    public List<Long> getIds() {
        return ids;
    }

    public String[] getRelateEntityNames() {
        return relateEntityNames;
    }

    public enum DeleteActionType {
        BY_ENTITY, BY_ID, BY_IDS
    }
}
