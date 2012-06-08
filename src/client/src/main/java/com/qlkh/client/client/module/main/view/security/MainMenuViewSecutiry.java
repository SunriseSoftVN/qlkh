/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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

    public HasRole[] mnlTaskManage() {
        return new HasRole[]{UserRoleEnum.MANAGER};
    }

    public HasRole[] mnlReport() {
        return new HasRole[]{UserRoleEnum.MANAGER, UserRoleEnum.USER};
    }

    public HasRole[] mnlTaskDetail() {
        return new HasRole[]{UserRoleEnum.USER};
    }

    public HasRole[] mnlTaskAnnualDetail() {
        return new HasRole[]{UserRoleEnum.USER};
    }

    public HasRole[] ancLogout() {
        return new HasRole[]{UserRoleEnum.USER, UserRoleEnum.ADMIN, UserRoleEnum.MANAGER};
    }

    public HasRole[] lblWelcome() {
        return new HasRole[]{UserRoleEnum.USER, UserRoleEnum.ADMIN, UserRoleEnum.MANAGER};
    }

    public HasRole[] mlLock() {
        return new HasRole[]{UserRoleEnum.MANAGER};
    }
}
