/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.transaction;

import com.qlkh.core.client.exception.ExceptionHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Session;

/**
 * The Class TransactionInterceptor.
 *
 * @author Nguyen Duc Dung
 * @since 1/29/12, 11:23 AM
 */
public class TransactionInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //Open Session and begin transaction.
        Session session = null;
        session.beginTransaction();
        Object result = null;

        //Commit and close session.
        try {
            result = invocation.proceed();
            session.flush();
        } catch (Exception ex) {
            ExceptionHandler handler = invocation.getMethod().getAnnotation(ExceptionHandler.class);
            if (handler != null) {
                Class<? extends Exception> expectException = handler.expectException();
                Class<? extends Exception> wrapperException = handler.wrapperException();
                if (ex.getClass() == expectException) {
                    throw wrapperException.newInstance();
                }
            } else {
                //Throw original exception, if exception handler wasn't define.
                throw ex;
            }
        } finally {
            session.close();
        }
        return result;
    }
}
