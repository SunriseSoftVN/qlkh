package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadMaterialAction.
 *
 * @author Nguyen Duc Dung
 * @since 4/8/13 4:40 PM
 */
public class LoadMaterialAction implements Action<LoadMaterialResult> {

    private BasePagingLoadConfig config;

    public LoadMaterialAction() {
    }

    public LoadMaterialAction(BasePagingLoadConfig config) {
        this.config = config;
    }

    public BasePagingLoadConfig getConfig() {
        return config;
    }
}
