package com.qlkh.client.client.module.content.view.security;

import com.qlkh.core.client.constant.UserRoleEnum;
import com.smvp4g.mvp.client.core.security.HasRole;
import com.smvp4g.mvp.client.core.security.ViewSecurityConfigurator;

/**
 * The Class MaterialInViewSercurity.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 4:05 AM
 */
public class MaterialInSecurity implements ViewSecurityConfigurator {
    @Override
    public HasRole[] getRoles() {
        return new HasRole[]{UserRoleEnum.WAREHOUSE_MANAGER};
    }
}
