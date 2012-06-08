/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.core;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.DispatchException;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class AbstractHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 6:26 AM
 */
public abstract class AbstractHandler<A extends Action<R>, R extends Result>
        implements ActionHandler<A, R> {
    @Override
    public void rollback(A action, R result, ExecutionContext context) throws DispatchException {
        //Default do nothing.
    }
}
