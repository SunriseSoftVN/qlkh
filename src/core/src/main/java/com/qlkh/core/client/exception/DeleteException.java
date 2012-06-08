/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.exception;

import java.io.Serializable;

/**
 * The Class DeleteException.
 *
 * @author Nguyen Duc Dung
 * @since 12/31/11, 9:25 AM
 */
public class DeleteException extends RuntimeException implements Serializable {
    
    public DeleteException() {
        
    }
    
    public DeleteException(String message) {
        super(message);
    }
    
}
