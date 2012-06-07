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

package com.qlvt.server.Interceptor;

import com.qlvt.core.client.model.SystemLog;
import com.qlvt.core.system.SystemUtil;
import com.qlvt.server.dao.core.GeneralDao;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 * The Class ExceptionsInterceptor.
 *
 * @author Nguyen Duc Dung
 * @since 6/3/12, 10:17 AM
 */
public class ExceptionsInterceptor implements ThrowsAdvice {

    @Autowired
    private GeneralDao generalDao;

    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) {
        if (SystemUtil.isProductionMode()) {
            SystemLog systemLog = new SystemLog();
            systemLog.setClassName(target.getClass().getName());
            systemLog.setMethodName(method.getName());
            systemLog.setExceptionClass(ex.getClass().getName());
            systemLog.setContent(ex.getMessage());
            generalDao.saveOrUpdate(systemLog);
        }
    }

}
