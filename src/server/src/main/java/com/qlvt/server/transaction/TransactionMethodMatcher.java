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

package com.qlvt.server.transaction;

import com.google.inject.matcher.AbstractMatcher;
import com.qlvt.server.service.core.AbstractService;

import java.lang.reflect.Method;

/**
 * The Class TransactionMethodMatcher.
 *
 * @author Nguyen Duc Dung
 * @since 1/29/12, 11:52 AM
 */
public class TransactionMethodMatcher extends AbstractMatcher<Method> {
    @Override
    public boolean matches(Method method) {
        if (method != null) {
            Class clazz = method.getDeclaringClass();
            if (clazz.getSuperclass() != null
                    && clazz.getSuperclass().equals(AbstractService.class)) {
                return true;
            }
        }
        return false;
    }
}
