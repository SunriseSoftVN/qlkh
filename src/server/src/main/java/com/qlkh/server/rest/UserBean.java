package com.qlkh.server.rest;

/**
 * The Class UserBean.
 *
 * @author Nguyen Duc Dung
 * @since 4/20/14 4:03 PM
 */
public class UserBean {

    public String name;

    public UserBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
