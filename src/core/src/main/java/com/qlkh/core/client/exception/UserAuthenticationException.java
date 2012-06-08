/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class UserAuthenticationException.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 10:16 AM
 */
public class UserAuthenticationException extends RuntimeException implements IsSerializable {

    public UserAuthenticationException() {

    }

    public UserAuthenticationException(String message) {
        super(message);
    }

}