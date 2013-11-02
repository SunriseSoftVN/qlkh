package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadMaterialInAction.
 *
 * @author Nguyen Duc Dung
 * @since 11/2/13 11:59 AM
 */
public class LoadMaterialInAction implements Action<LoadMaterialInResult> {

    private BasePagingLoadConfig loadConfig;

    public LoadMaterialInAction() {
    }

    public LoadMaterialInAction(BasePagingLoadConfig loadConfig) {
        this.loadConfig = loadConfig;
    }

    public BasePagingLoadConfig getLoadConfig() {
        return loadConfig;
    }

    public void setLoadConfig(BasePagingLoadConfig loadConfig) {
        this.loadConfig = loadConfig;
    }
}
