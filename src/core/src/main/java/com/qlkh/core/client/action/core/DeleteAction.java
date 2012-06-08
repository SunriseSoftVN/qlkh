/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.core;

import com.qlkh.core.client.model.core.AbstractEntity;
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
