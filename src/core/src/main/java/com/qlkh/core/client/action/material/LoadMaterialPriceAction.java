package com.qlkh.core.client.action.material;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class MaterialPriceAction.
 *
 * @author Nguyen Duc Dung
 * @since 4/5/13 11:26 AM
 */
public class LoadMaterialPriceAction implements Action<LoadMaterialPriceResult> {

    private BasePagingLoadConfig loadConfig;
    private long materialId;

    public LoadMaterialPriceAction() {
    }

    public LoadMaterialPriceAction(BasePagingLoadConfig loadConfig, long materialId) {
        this.loadConfig = loadConfig;
        this.materialId = materialId;
    }

    public BasePagingLoadConfig getLoadConfig() {
        return loadConfig;
    }

    public void setLoadConfig(BasePagingLoadConfig loadConfig) {
        this.loadConfig = loadConfig;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }
}
