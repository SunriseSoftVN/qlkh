/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.grid;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.qlkh.core.client.criterion.ClientCriteria;

/**
 * The Class LoadGridDataAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 6:38 AM
 */
public class LoadGridDataAction implements ActionHasLoadConfig<LoadGridDataResult> {

    private BasePagingLoadConfig config;
    private String entityName;
    private ClientCriteria[] criterion;

    public LoadGridDataAction() {
    }

    public LoadGridDataAction(String entityName, ClientCriteria... criterion) {
        this.criterion = criterion;
        this.entityName = entityName;
    }

    public LoadGridDataAction(BasePagingLoadConfig config, String entityName, ClientCriteria... criterion) {
        this.config = config;
        this.entityName = entityName;
        this.criterion = criterion;
    }

    @Override
    public BasePagingLoadConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(BasePagingLoadConfig config) {
        this.config = config;
    }

    public String getEntityName() {
        return entityName;
    }

    public ClientCriteria[] getCriterion() {
        return criterion;
    }
}
