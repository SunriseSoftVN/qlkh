/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.constant;

import com.smvp4g.mvp.client.core.security.HasRole;

/**
 * The Class UserRoleEnum.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 9:44 AM
 */
public enum UserRoleEnum implements HasRole {
    /**
     * The Admin.
     */
    ADMIN("ADMIN"),

    /**
     * The Manager
     */
    MANAGER("MANAGER"),

    /**
     * The User.
     */
    USER("USER"),

    /**
     * The Manager of material.
     */
    MATERIAL_MANAGER("MATERIAL_MANAGER"),

    /**
     * The manager of warehouse.
     */
    WAREHOUSE_MANAGER("WAREHOUSE_MANAGER"),

    /**
     * The developer can access the application to get the system log.
     */
    DEVELOPER("DEVELOPER");

    private String role;

    UserRoleEnum() {
        //For Serializable.
    }

    UserRoleEnum(String role) {
        this.role = role;
    }

    @Override
    public String getRole() {
        return role;
    }
}