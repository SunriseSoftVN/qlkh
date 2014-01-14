/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.main.view.security;

import com.qlkh.core.client.constant.UserRoleEnum;
import com.smvp4g.mvp.client.core.security.HasRole;
import com.smvp4g.mvp.client.core.security.ViewSecurityConfigurator;

/**
 * The Class MainMenuViewSecutiry.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 9:48 AM
 */
@SuppressWarnings("UnusedDeclaration")
public class MainMenuViewSecutiry implements ViewSecurityConfigurator {

    @Override
    public HasRole[] getRoles() {
        return new HasRole[0];
    }

    public HasRole[] mnlUserManager() {
        return new HasRole[]{UserRoleEnum.ADMIN};
    }

    public HasRole[] mnlStationManager() {
        return new HasRole[]{UserRoleEnum.ADMIN};
    }

    public HasRole[] mnlBranchManager() {
        return new HasRole[]{UserRoleEnum.ADMIN};
    }

    public HasRole[] mnlGroupManager() {
        return new HasRole[]{UserRoleEnum.ADMIN, UserRoleEnum.MANAGER};
    }

    public HasRole[] mnlTaskManage() {
        return new HasRole[]{UserRoleEnum.MANAGER};
    }

    public HasRole[] mnlReport() {
        return new HasRole[]{
                UserRoleEnum.MANAGER,
                UserRoleEnum.MATERIAL_MANAGER,
                UserRoleEnum.USER,
                UserRoleEnum.WAREHOUSE_MANAGER
        };
    }

    public HasRole[] mnlTaskDetailKDK() {
        return new HasRole[]{UserRoleEnum.USER};
    }

    public HasRole[] mnlTaskDetailDK() {
        return new HasRole[]{UserRoleEnum.USER};
    }

    public HasRole[] mnlTaskDetailNam() {
        return new HasRole[]{UserRoleEnum.USER};
    }

    public HasRole[] ancLogout() {
        return new HasRole[]{
                UserRoleEnum.USER,
                UserRoleEnum.ADMIN,
                UserRoleEnum.MANAGER,
                UserRoleEnum.DEVELOPER,
                UserRoleEnum.MATERIAL_MANAGER,
                UserRoleEnum.WAREHOUSE_MANAGER
        };
    }

    public HasRole[] lblWelcome() {
        return new HasRole[]{
                UserRoleEnum.USER,
                UserRoleEnum.ADMIN,
                UserRoleEnum.MANAGER,
                UserRoleEnum.DEVELOPER,
                UserRoleEnum.MATERIAL_MANAGER,
                UserRoleEnum.WAREHOUSE_MANAGER
        };
    }

    public HasRole[] mlLock() {
        return new HasRole[]{UserRoleEnum.MANAGER};
    }

    public HasRole[] mlUpgrade() {
        return new HasRole[]{UserRoleEnum.DEVELOPER};
    }

    public HasRole[] mlLimitJob() {
        return new HasRole[]{UserRoleEnum.MANAGER, UserRoleEnum.USER};
    }

    public HasRole[] mlMaterial() {
        return new HasRole[]{UserRoleEnum.MATERIAL_MANAGER};
    }

    public HasRole[] mlMaterialPrice() {
        return new HasRole[]{UserRoleEnum.MATERIAL_MANAGER};
    }

    public HasRole[] mlMaterialPerson() {
        return new HasRole[]{UserRoleEnum.WAREHOUSE_MANAGER};
    }

    public HasRole[] mlMaterialGroup() {
        return new HasRole[]{UserRoleEnum.WAREHOUSE_MANAGER};
    }

    public HasRole[] mlMaterialIn() {
        return new HasRole[]{UserRoleEnum.WAREHOUSE_MANAGER};
    }
}
