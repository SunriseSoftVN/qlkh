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

package com.qlvt.core.client.criterion;

import com.allen_sauer.gwt.log.client.Log;

import java.util.Date;

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
        ClientCriteria criteria = new ClientCriteria(propertyName, ClientCriteria.Operation.EQ);
        setValue(criteria, value);
        return criteria;
   	}

    public static ClientCriteria ne(String propertyName, Object value) {
        ClientCriteria criteria = new ClientCriteria(propertyName, ClientCriteria.Operation.NE);
        setValue(criteria, value);
        return criteria;
   	}

    private static void setValue(ClientCriteria criteria, Object value) {
        if (value instanceof String) {
            criteria.setStringValue(String.valueOf(value));
        } else if (value instanceof Integer) {
            criteria.setIntValue((Integer) value);
        } else if (value instanceof  Long) {
            criteria.setLongValue((Long) value);
        } else if (value instanceof Date) {
            criteria.setDateValue((Date) value);
        } else if (value instanceof Boolean) {
            criteria.setBoolValue((Boolean) value);
        } else {
            Log.error("Unsupported data type");
        }
    }
}
