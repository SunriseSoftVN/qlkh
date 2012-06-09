/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler.time;

import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import java.util.Date;

/**
 * The Class GetServerTimeHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/2/12, 4:01 PM
 */
public class GetServerTimeHandler extends AbstractHandler<GetServerTimeAction, GetServerTimeResult> {
    @Override
    public Class<GetServerTimeAction> getActionType() {
        return GetServerTimeAction.class;
    }

    @Override
    public GetServerTimeResult execute(GetServerTimeAction action, ExecutionContext context) throws DispatchException {
        return new GetServerTimeResult(1900 + new Date().getYear());
    }
}