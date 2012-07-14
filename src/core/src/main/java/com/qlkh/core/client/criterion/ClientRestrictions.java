/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.criterion;

import com.allen_sauer.gwt.log.client.Log;

import java.util.Date;

import static com.qlkh.core.client.criterion.ClientCriteria.Operation.*;

/**
 * The Class ClientRestrictions.
 *
 * @author Nguyen Duc Dung
 * @since 6/2/12, 9:50 PM
 */
public final class ClientRestrictions {

    private ClientRestrictions() {

    }

    public static ClientCriteria eq(String propertyName, Object value) {
        ClientCriteria criteria = new ClientCriteria(propertyName, EQ);
        setValue(criteria, value);
        return criteria;
    }

    public static ClientCriteria ne(String propertyName, Object value) {
        ClientCriteria criteria = new ClientCriteria(propertyName, NE);
        setValue(criteria, value);
        return criteria;
    }

    public static ClientCriteria in(String propertyName, Object value) {
        ClientCriteria criteria = new ClientCriteria(propertyName, IN);
        setValue(criteria, value);
        return criteria;
    }

    private static void setValue(ClientCriteria criteria, Object value) {
        if (value instanceof String) {
            criteria.setStringValue(String.valueOf(value));
        } else if (value instanceof Integer) {
            criteria.setIntValue((Integer) value);
        } else if (value instanceof Long) {
            criteria.setLongValue((Long) value);
        } else if (value instanceof Date) {
            criteria.setDateValue((Date) value);
        } else if (value instanceof Boolean) {
            criteria.setBoolValue((Boolean) value);
        } else if (value instanceof String[]) {
            criteria.setStringValues((String[]) value);
        } else if (value instanceof Integer[]) {
            criteria.setIntValues((Integer[]) value);
        }else {
            Log.error("Unsupported data type");
        }
    }
}
