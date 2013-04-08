package com.qlkh.core.client.action.material;

import com.qlkh.core.client.model.Material;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class SaveMaterialAction.
 *
 * @author Nguyen Duc Dung
 * @since 4/9/13 1:07 AM
 */
public class SaveMaterialAction implements Action<SaveMaterialResult> {

    private Material material;

    public SaveMaterialAction(Material material) {
        this.material = material;
    }

    public SaveMaterialAction() {
    }

    public Material getMaterial() {
        return material;
    }
}
