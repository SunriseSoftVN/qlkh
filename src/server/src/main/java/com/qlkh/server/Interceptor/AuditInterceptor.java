/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.Interceptor;

import com.qlkh.core.client.model.core.AbstractEntity;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class AuditInterceptor.
 *
 * @author Nguyen Duc Dung
 * @since 1/29/12, 8:06 PM
 */
public class AuditInterceptor extends EmptyInterceptor {

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return updateEntity(entity, currentState, propertyNames);
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] currentState, String[] propertyNames, Type[] types) {
        return updateEntity(entity, currentState, propertyNames);
    }

    private boolean updateEntity(Object entity, Object[] currentState, String[] propertyNames) {
        if (entity instanceof AbstractEntity) {
            for (int i = 0; i < propertyNames.length; i++) {
                if ("createdDate".equals(propertyNames[i])) {
                    if (currentState[i] == null) {
                        currentState[i] = new Date();
                    }
                }
                if ("updatedDate".equals(propertyNames[i])) {
                    currentState[i] = new Date();
                }
            }
            return true;
        }
        return false;
    }
}
