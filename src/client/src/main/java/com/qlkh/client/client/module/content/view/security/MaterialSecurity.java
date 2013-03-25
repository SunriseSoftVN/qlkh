package com.qlkh.client.client.module.content.view.security;

import com.qlkh.core.client.constant.UserRoleEnum;
import com.smvp4g.mvp.client.core.security.HasRole;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurityConfigurator;

import java.lang.annotation.Annotation;

/**
 * The Class MaterialSecurity.
 *
 * @author Nguyen Duc Dung
 * @since 3/25/13 1:38 PM
 */
public class MaterialSecurity implements ViewSecurityConfigurator {
    @Override
    public HasRole[] getRoles() {
        return new HasRole[]{UserRoleEnum.MATERIAL_MANAGER};
    }
}
