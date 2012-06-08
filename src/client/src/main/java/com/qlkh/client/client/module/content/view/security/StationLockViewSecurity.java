/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.view.security;

import com.qlkh.core.client.constant.UserRoleEnum;
import com.smvp4g.mvp.client.core.security.HasRole;
import com.smvp4g.mvp.client.core.security.ViewSecurityConfigurator;

/**
 * The Class LockViewSecurity.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/12, 9:41 PM
 */
public class StationLockViewSecurity implements ViewSecurityConfigurator {
    @Override
    public HasRole[] getRoles() {
        return new HasRole[]{UserRoleEnum.MANAGER};
    }
}
