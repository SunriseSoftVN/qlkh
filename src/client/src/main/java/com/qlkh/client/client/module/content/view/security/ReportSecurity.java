/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.view.security;

import com.qlkh.core.client.constant.UserRoleEnum;
import com.smvp4g.mvp.client.core.security.HasRole;
import com.smvp4g.mvp.client.core.security.ViewSecurityConfigurator;

/**
 * The Class ReportSecurity.
 *
 * @author Nguyen Duc Dung
 * @since 2/19/12, 3:46 PM
 */
public class ReportSecurity implements ViewSecurityConfigurator {
    @Override
    public HasRole[] getRoles() {
        return new HasRole[]{UserRoleEnum.MANAGER, UserRoleEnum.USER};
    }

    public HasRole[] cbbReportStation() {
        return new HasRole[]{UserRoleEnum.MANAGER};
    }

    public HasRole[] cbbReportBranch() {
        return new HasRole[]{UserRoleEnum.USER};
    }
}
