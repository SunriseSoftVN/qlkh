/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Class ExceptionHandler.
 *
 * @author Nguyen Duc Dung
 * @since 3/13/12, 10:05 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionHandler {
    Class<? extends Exception> expectException() default Exception.class;
    Class<? extends Exception> wrapperException();
}
