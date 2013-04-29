package com.qlkh.server.handler.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.material.LoadMaterialWithTaskAction;
import com.qlkh.core.client.action.material.LoadMaterialWithTaskResult;
import com.qlkh.core.client.model.Material;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class LoadMaterialWithTaskHandler.
 *
 * @author Nguyen Duc Dung
 * @since 4/29/13 4:25 PM
 */
public class LoadMaterialWithTaskHandler extends AbstractHandler<LoadMaterialWithTaskAction, LoadMaterialWithTaskResult> {

    @Autowired
    private SqlQueryDao sqlQueryDao;

    @Override
    public Class<LoadMaterialWithTaskAction> getActionType() {
        return LoadMaterialWithTaskAction.class;
    }

    @Override
    public LoadMaterialWithTaskResult execute(LoadMaterialWithTaskAction action, ExecutionContext context) throws DispatchException {
        BasePagingLoadResult<Material> result = sqlQueryDao.getMaterialTasks(action.getYear(), action.getQuarter(), action.getLoadConfig());
        return new LoadMaterialWithTaskResult(result);
    }
}
