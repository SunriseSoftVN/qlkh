/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.user.view.security;

import com.qlkh.core.client.constant.UserRoleEnum;
import com.smvp4g.mvp.client.core.security.HasRole;
import com.smvp4g.mvp.client.core.security.ViewSecurityConfigurator;

/**
 * The Class UserManagerSecurity.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 4:51 PM
 */
public class UserManagerSecurity implements ViewSecurityConfigurator {
    @Override
    public HasRole[] getRoles() {
        return new HasRole[]{UserRoleEnum.ADMIN};
    }
}
