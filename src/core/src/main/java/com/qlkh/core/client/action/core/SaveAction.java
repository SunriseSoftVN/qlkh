/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.core;

import com.qlkh.core.client.model.core.AbstractEntity;
import net.customware.gwt.dispatch.shared.Action;

import java.util.List;

/**
 * The Class SaveAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 1:50 PM
 */
public class SaveAction implements Action<SaveResult> {

    private List<AbstractEntity> entities;
    private AbstractEntity entity;

    public SaveAction() {
    }

    public <E extends AbstractEntity> SaveAction(E entity) {
        this.entity = entity;
    }

    public <E extends AbstractEntity> SaveAction(List<E> entities) {
        this.entities = (List<AbstractEntity>) entities;
    }

    public AbstractEntity getEntity() {
        return entity;
    }

    public List<AbstractEntity> getEntities() {
        return entities;
    }
}
