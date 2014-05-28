/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.interceptor;

import com.qlkh.core.client.model.SystemLog;
import com.qlkh.core.configuration.ConfigurationServerUtil;
import com.qlkh.server.dao.core.GeneralDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ExceptionsInterceptor.class);

    @Autowired
    private GeneralDao generalDao;

    @SuppressWarnings("UnusedDeclaration")
    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) {
        if (ConfigurationServerUtil.isProductionMode()) {

            //Log to file.
           logger.error(ex.getMessage(), ex);

            SystemLog systemLog = new SystemLog();
            systemLog.setClassName(target.getClass().getName());
            systemLog.setMethodName(method.getName());
            systemLog.setExceptionClass(ex.getClass().getName());
            systemLog.setContent(ex.getMessage());
            generalDao.saveOrUpdate(systemLog);
        }
    }

}
