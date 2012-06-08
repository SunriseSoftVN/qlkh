/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.core;

import com.qlkh.core.client.criterion.ClientCriteria;
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
    private ClientCriteria[] criterion;

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

    public LoadAction(String entityName, ClientCriteria... criterion) {
        this.entityName = entityName;
        this.criterion = criterion;
        this.loadType = LoadActionType.BY_CRITERIA;
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

    public ClientCriteria[] getCriterion() {
        return criterion;
    }

    public enum LoadActionType {
        ALL, BY_ID, BY_CRITERIA
    }
}
