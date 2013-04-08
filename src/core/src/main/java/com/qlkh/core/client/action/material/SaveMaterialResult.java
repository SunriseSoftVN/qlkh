package com.qlkh.core.client.action.material;

import com.qlkh.core.client.model.Material;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class SaveMaterialAction.
 *
 * @author Nguyen Duc Dung
 * @since 4/9/13 1:07 AM
 */
public class SaveMaterialResult implements Result {

    private Material material;

    public SaveMaterialResult() {
    }

    public SaveMaterialResult(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
