package com.qlkh.server.handler.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.material.LoadMaterialLimitAction;
import com.qlkh.core.client.action.material.LoadMaterialLimitResult;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.client.model.MaterialLimit;
import com.qlkh.core.client.model.Task;
import com.qlkh.server.dao.MaterialDao;
import com.qlkh.server.dao.MaterialLimitDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class LoadLimitHandler.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 2:30 PM
 */
public class LoadMaterialLimitHandler extends AbstractHandler<LoadMaterialLimitAction, LoadMaterialLimitResult> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private MaterialLimitDao materialLimitDao;

    @Override
    public Class<LoadMaterialLimitAction> getActionType() {
        return LoadMaterialLimitAction.class;
    }

    @Override
    public LoadMaterialLimitResult execute(LoadMaterialLimitAction loadMaterialAction, ExecutionContext executionContext) throws DispatchException {
        List<MaterialLimit> materials = materialLimitDao.findByTaskId(loadMaterialAction.getTaskId());
        BasePagingLoadResult<MaterialLimit> result =
                new BasePagingLoadResult<MaterialLimit>(materials, loadMaterialAction.getLoadConfig().getOffset(), 1);
        return new LoadMaterialLimitResult(result);
    }
}
