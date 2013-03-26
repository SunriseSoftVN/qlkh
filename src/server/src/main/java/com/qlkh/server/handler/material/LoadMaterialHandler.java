package com.qlkh.server.handler.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.action.material.LoadMaterialAction;
import com.qlkh.core.client.action.material.LoadMaterialResult;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.client.model.Task;
import com.qlkh.server.dao.MaterialDao;
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
public class LoadMaterialHandler extends AbstractHandler<LoadMaterialAction, LoadMaterialResult> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private MaterialDao materialDao;

    @Override
    public Class<LoadMaterialAction> getActionType() {
        return LoadMaterialAction.class;
    }

    @Override
    public LoadMaterialResult execute(LoadMaterialAction loadMaterialAction, ExecutionContext executionContext) throws DispatchException {
        Task task = generalDao.findById(Task.class, loadMaterialAction.getTaskId());
        List<Material> materials = new ArrayList<Material>();
        if(task != null) {
            materials.addAll(materialDao.findByTaskId(loadMaterialAction.getTaskId()));
            for (int i = 0; i < 50; i++) {
                Material material = new Material();
//                material.setTask(task);
                material.setCreateBy(1l);
                material.setUpdateBy(1l);
                materials.add(material);
            }
        }
        BasePagingLoadResult<Material> result =
                new BasePagingLoadResult<Material>(materials, loadMaterialAction.getLoadConfig().getOffset(), 1);
        return new LoadMaterialResult(result);
    }
}
