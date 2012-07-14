/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
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
                } else if (criteria.getOperation() == ClientCriteria.Operation.IN) {
                    criterion = Restrictions.in(criteria.getPropertyName(), (Object[]) value);
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
        } else if (criteria.getStringValues() != null) {
            return criteria.getStringValues();
        } else if (criteria.getIntValues() != null) {
            return criteria.getIntValues();
        }
        return null;
    }

}
