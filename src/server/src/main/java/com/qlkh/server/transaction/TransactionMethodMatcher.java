/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.transaction;

import com.google.inject.matcher.AbstractMatcher;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * The Class TransactionMethodMatcher.
 *
 * @author Nguyen Duc Dung
 * @since 1/29/12, 11:52 AM
 */
public class TransactionMethodMatcher extends AbstractMatcher<Method> {
    @Override
    public boolean matches(Method method) {
        if (method != null && Modifier.isPublic(method.getModifiers())) {
            Class clazz = method.getDeclaringClass();
//            if (clazz.getSuperclass() != null
//                    && clazz.getSuperclass().equals(AbstractService.class)) {
//                return true;
//            }
        }
        return false;
    }
}
