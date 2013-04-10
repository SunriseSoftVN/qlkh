package com.qlkh.server.handler.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.material.LoadMaterialWithoutLimitAction;
import com.qlkh.core.client.action.material.LoadMaterialWithoutLimitResult;
import com.qlkh.core.client.model.Material;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class LoadLimitHandler.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 2:30 PM
 */
public class LoadMaterialWithoutLimitHandler extends AbstractHandler<LoadMaterialWithoutLimitAction, LoadMaterialWithoutLimitResult> {

    @Autowired
    private SqlQueryDao sqlQueryDao;

    @Override
    public Class<LoadMaterialWithoutLimitAction> getActionType() {
        return LoadMaterialWithoutLimitAction.class;
    }

    @Override
    public LoadMaterialWithoutLimitResult execute(LoadMaterialWithoutLimitAction action, ExecutionContext executionContext) throws DispatchException {
        BasePagingLoadResult<Material> result = sqlQueryDao.getMaterials(action.getTaskId(), action.getLoadConfig());
        return new LoadMaterialWithoutLimitResult(result);
    }
}
