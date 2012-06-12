/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.task;

import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class CanEditAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/12/12, 1:39 PM
 */
public class CanEditAction implements Action<CanEditResult> {

    private long id;
    private String[] relateEntityNames;

    public CanEditAction() {
    }

    public CanEditAction(long id, String[] relateEntityNames) {
        this.id = id;
        this.relateEntityNames = relateEntityNames;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String[] getRelateEntityNames() {
        return relateEntityNames;
    }

    public void setRelateEntityNames(String[] relateEntityNames) {
        this.relateEntityNames = relateEntityNames;
    }
}
