/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.core;

import com.qlkh.core.client.model.core.AbstractEntity;
import net.customware.gwt.dispatch.shared.Result;

import java.util.List;

/**
 * The Class LoadResult.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 11:38 AM
 */
public class LoadResult implements Result {

    private List<? extends AbstractEntity> list;

    private AbstractEntity result;

    public LoadResult() {
    }

    public LoadResult(List<? extends AbstractEntity> list) {
        this.list = list;
    }

    public LoadResult(AbstractEntity result) {
        this.result = result;
    }

    @SuppressWarnings("unchecked")
    public <E extends AbstractEntity> E getResult() {
        return (E) result;
    }

    @SuppressWarnings("unchecked")
    public <E extends AbstractEntity> List<E> getList() {
        return (List<E>) list;
    }
}
