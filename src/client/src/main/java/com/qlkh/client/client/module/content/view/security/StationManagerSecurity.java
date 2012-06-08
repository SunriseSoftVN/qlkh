/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.view.security;

import com.qlkh.core.client.constant.UserRoleEnum;
import com.smvp4g.mvp.client.core.security.HasRole;
import com.smvp4g.mvp.client.core.security.ViewSecurityConfigurator;

/**
 * The Class StationManagerSecurity.
 *
 * @author Nguyen Duc Dung
 * @since 12/29/11, 6:59 AM
 */
public class StationManagerSecurity implements ViewSecurityConfigurator {
    @Override
    public HasRole[] getRoles() {
        return new HasRole[]{UserRoleEnum.ADMIN};
    }
}
