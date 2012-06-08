/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.exception;

import java.io.Serializable;

/**
 * The Class CodeExistException.
 *
 * @author Nguyen Duc Dung
 * @since 1/12/12, 12:48 PM
 */
public class CodeExistException extends RuntimeException implements Serializable {
    
    public CodeExistException() {
        
    }
    
    public CodeExistException(String msg) {
        super(msg);
    }
    
}
