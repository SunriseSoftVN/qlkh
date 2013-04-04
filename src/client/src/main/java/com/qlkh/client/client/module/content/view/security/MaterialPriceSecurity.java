package com.qlkh.client.client.module.content.view.security;

import com.qlkh.core.client.constant.UserRoleEnum;
import com.smvp4g.mvp.client.core.security.HasRole;
import com.smvp4g.mvp.client.core.security.ViewSecurityConfigurator;

/**
 * The Class MaterialPriceSecurity.
 *
 * @author Nguyen Duc Dung
 * @since 4/4/13 11:44 PM
 */
public class MaterialPriceSecurity implements ViewSecurityConfigurator {
    @Override
    public HasRole[] getRoles() {
        return new HasRole[]{UserRoleEnum.MATERIAL_MANAGER};
    }
}
