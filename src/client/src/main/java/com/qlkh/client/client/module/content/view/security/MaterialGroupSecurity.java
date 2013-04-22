package com.qlkh.client.client.module.content.view.security;

import com.qlkh.core.client.constant.UserRoleEnum;
import com.smvp4g.mvp.client.core.security.HasRole;
import com.smvp4g.mvp.client.core.security.ViewSecurityConfigurator;

/**
 * The Class MaterialGroupSercurity.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 3:43 AM
 */
public class MaterialGroupSecurity implements ViewSecurityConfigurator {
    @Override
    public HasRole[] getRoles() {
        return new HasRole[]{UserRoleEnum.MATERIAL_MANAGER};
    }
}
