/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.task;

import net.customware.gwt.dispatch.shared.Action;

import java.util.List;

/**
 * The Class DeleteTaskAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/14/12, 10:49 AM
 */
public class DeleteTaskAction implements Action<DeleteTaskResult> {

    private List<Long> ids;
    private long id;
    private boolean forceDelete;

    public DeleteTaskAction() {
    }

    public DeleteTaskAction(long id) {
        this.id = id;
    }

    public DeleteTaskAction(long id, boolean forceDelete) {
        this.id = id;
        this.forceDelete = forceDelete;
    }

    public DeleteTaskAction(List<Long> ids) {
        this.ids = ids;
    }

    public DeleteTaskAction(List<Long> ids, boolean forceDelete) {
        this.ids = ids;
        this.forceDelete = forceDelete;
    }

    public List<Long> getIds() {
        return ids;
    }

    public long getId() {
        return id;
    }

    public boolean isForceDelete() {
        return forceDelete;
    }
}
