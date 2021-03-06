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
        return new HasRole[]{
                UserRoleEnum.MANAGER,
                UserRoleEnum.MATERIAL_MANAGER,
                UserRoleEnum.WAREHOUSE_MANAGER,
                UserRoleEnum.USER
        };
    }

    public HasRole[] cbbTaskReportStation() {
        return new HasRole[]{UserRoleEnum.MANAGER};
    }

    public HasRole[] cbbPriceReportStation() {
        return new HasRole[]{UserRoleEnum.MANAGER};
    }

    public HasRole[] cbbTaskReportBranch() {
        return new HasRole[]{UserRoleEnum.USER};
    }

    public HasRole[] cbbPriceReportBranch() {
        return new HasRole[]{UserRoleEnum.USER};
    }

    public HasRole[] materialReportPanel() {
        return new HasRole[]{UserRoleEnum.MATERIAL_MANAGER};
    }

    public HasRole[] taskDefaultValueReportPanel() {
        return new HasRole[]{UserRoleEnum.USER, UserRoleEnum.MANAGER};
    }

    public HasRole[] planReportPanel() {
        return new HasRole[]{UserRoleEnum.MANAGER, UserRoleEnum.USER};
    }

    public HasRole[] priceReportPanel() {
        return new HasRole[]{UserRoleEnum.MANAGER, UserRoleEnum.USER};
    }

    public HasRole[] wareHouseReportPanel() {
        return new HasRole[]{UserRoleEnum.WAREHOUSE_MANAGER};
    }

}
