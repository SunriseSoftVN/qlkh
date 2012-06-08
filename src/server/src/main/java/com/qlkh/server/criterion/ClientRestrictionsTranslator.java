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

package com.qlkh.server.criterion;

import com.qlkh.core.client.criterion.ClientCriteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * The Class ClientRestrictionsTranslator.
 *
 * @author Nguyen Duc Dung
 * @since 6/2/12, 10:09 PM
 */
public final class ClientRestrictionsTranslator {

    private ClientRestrictionsTranslator() {

    }

    public static Criterion[] getCriterions(ClientCriteria... criterias) {
        Criterion[] criterions = new Criterion[criterias.length];
        int count = 0;
        for (ClientCriteria criteria : criterias) {
            Object value = getValue(criteria);
            if (value != null) {
                Criterion criterion = null;
                if (criteria.getOperation() == ClientCriteria.Operation.EQ) {
                    criterion = Restrictions.eq(criteria.getPropertyName(), value);
                } else if (criteria.getOperation() == ClientCriteria.Operation.NE) {
                    criterion = Restrictions.ne(criteria.getPropertyName(), value);
                }
                if (criterion != null) {
                    criterions[count] = criterion;
                    count += 1;
                }
            }
        }
        return criterions;
    }

    private static Object getValue(ClientCriteria criteria) {
        if (criteria.getStringValue() != null) {
            return criteria.getStringValue();
        } else if (criteria.getIntValue() != null) {
            return criteria.getIntValue();
        } else if (criteria.getLongValue() != null) {
            return criteria.getLongValue();
        } else if (criteria.getDateValue() != null) {
            return criteria.getDateValue();
        } else if (criteria.getBoolValue() != null) {
            return criteria.getBoolValue();
        }
        return null;
    }

}
