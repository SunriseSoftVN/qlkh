/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.taskdetail;

import net.customware.gwt.dispatch.shared.Action;

import java.util.List;

/**
 * The Class DeleteTaskDetailAction.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 8:46 PM
 */
public class DeleteTaskDetailAction implements Action<DeleteTaskDetailResult> {

    private List<Long> ids;

    private long id;

    public DeleteTaskDetailAction() {
    }

    public DeleteTaskDetailAction(long id) {
        this.id = id;
    }

    public DeleteTaskDetailAction(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }

    public long getId() {
        return id;
    }
}
