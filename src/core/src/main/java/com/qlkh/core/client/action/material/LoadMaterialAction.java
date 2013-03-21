package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadLimitJobAction.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 2:25 PM
 */
public class LoadMaterialAction implements Action<LoadMaterialResult> {

    private BasePagingLoadConfig loadConfig;

    public LoadMaterialAction() {
    }

    public LoadMaterialAction(BasePagingLoadConfig loadConfig) {
        this.loadConfig = loadConfig;
    }

    public BasePagingLoadConfig getLoadConfig() {
        return loadConfig;
    }
}
