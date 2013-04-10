package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadMaterialWithoutLimitAction.
 *
 * @author Nguyen Duc Dung
 * @since 4/11/13 12:26 AM
 */
public class LoadMaterialWithoutLimitAction implements Action<LoadMaterialWithoutLimitResult> {

    private BasePagingLoadConfig loadConfig;
    private long taskId;

    public LoadMaterialWithoutLimitAction() {
    }

    public LoadMaterialWithoutLimitAction(BasePagingLoadConfig loadConfig, long taskId) {
        this.loadConfig = loadConfig;
        this.taskId = taskId;
    }

    public BasePagingLoadConfig getLoadConfig() {
        return loadConfig;
    }

    public long getTaskId() {
        return taskId;
    }
}
