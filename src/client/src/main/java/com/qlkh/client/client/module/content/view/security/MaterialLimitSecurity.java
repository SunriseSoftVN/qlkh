package com.qlkh.client.client.module.content.view.security;

import com.qlkh.core.client.constant.UserRoleEnum;
import com.smvp4g.mvp.client.core.security.HasRole;
import com.smvp4g.mvp.client.core.security.ViewSecurityConfigurator;

/**
 * The Class LimitJobSecurity.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 10:12 AM
 */
public class MaterialLimitSecurity implements ViewSecurityConfigurator {
    @Override
    public HasRole[] getRoles() {
        return new HasRole[]{UserRoleEnum.MANAGER, UserRoleEnum.USER};
    }
}
