package com.qlkh.server.handler.limitjob;

import com.qlkh.core.client.action.limitjob.LoadLimitJobAction;
import com.qlkh.core.client.action.limitjob.LoadLimitJobResult;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * The Class LoadLimitHandler.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 2:30 PM
 */
public class LoadLimitHandler extends AbstractHandler<LoadLimitJobAction,LoadLimitJobResult> {

    @Override
    public Class<LoadLimitJobAction> getActionType() {
        return LoadLimitJobAction.class;
    }

    @Override
    public LoadLimitJobResult execute(LoadLimitJobAction loadLimitJobAction, ExecutionContext executionContext) throws DispatchException {
        return new LoadLimitJobResult();
    }
}
