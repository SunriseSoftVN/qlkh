package com.qlkh.server.handler.task;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.task.LoadTaskHasLimitAction;
import com.qlkh.core.client.action.task.LoadTaskHasLimitResult;
import com.qlkh.core.client.model.Task;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * The Class LoadTaskHasLimitHandler.
 *
 * @author Nguyen Duc Dung
 * @since 3/21/13 12:40 PM
 */
public class LoadTaskHasLimitHandler extends AbstractHandler<LoadTaskHasLimitAction, LoadTaskHasLimitResult> {

    @Autowired
    private SqlQueryDao sqlQueryDao;

    @Override
    public Class<LoadTaskHasLimitAction> getActionType() {
        return LoadTaskHasLimitAction.class;
    }

    @Override
    public LoadTaskHasLimitResult execute(LoadTaskHasLimitAction loadTaskHasLimitAction, ExecutionContext executionContext) throws DispatchException {
        BasePagingLoadResult<Task> result = sqlQueryDao.getTasks(true, true, loadTaskHasLimitAction.getConfig());
        return new LoadTaskHasLimitResult(result);
    }
}
