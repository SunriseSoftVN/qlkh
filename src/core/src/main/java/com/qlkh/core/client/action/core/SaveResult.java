/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.core;

import com.qlkh.core.client.model.core.AbstractEntity;
import net.customware.gwt.dispatch.shared.Result;

import java.util.List;

/**
 * The Class SaveResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 1:50 PM
 */
public class SaveResult implements Result {


    private AbstractEntity entity;
    private List<? extends AbstractEntity> entities;

    public SaveResult() {
    }

    public <E extends AbstractEntity> SaveResult(E entity) {
        this.entity = entity;
    }

    public SaveResult(List<? extends AbstractEntity> entities) {
        this.entities = entities;
    }

    @SuppressWarnings("unchecked")
    public <E extends AbstractEntity> E getEntity() {
        return (E) entity;
    }

    public <E extends AbstractEntity> List<E> getEntities() {
        return (List<E>) entities;
    }
}
