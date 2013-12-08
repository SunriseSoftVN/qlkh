package com.qlkh.server.handler.report;

import com.qlkh.core.client.action.report.TaskDefaultAction;
import com.qlkh.core.client.action.report.TaskDefaultResult;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * The Class TaskDefaultHandler.
 *
 * @author Nguyen Duc Dung
 * @since 12/8/13 10:00 AM
 */
public class TaskDefaultHandler extends AbstractHandler<TaskDefaultAction, TaskDefaultResult> {

    @Override
    public Class<TaskDefaultAction> getActionType() {
        return TaskDefaultAction.class;
    }

    @Override
    public TaskDefaultResult execute(TaskDefaultAction action, ExecutionContext context) throws DispatchException {
        return new TaskDefaultResult();
    }
}
