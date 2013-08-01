package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.MaterialIn;
import com.qlkh.server.dao.MaterialInDao;
import com.qlkh.server.dao.core.AbstractDao;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;

/**
 * The Class MaterialInDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 6/4/13 4:21 PM
 */
public class MaterialInDaoImpl extends AbstractDao<MaterialIn> implements MaterialInDao {

    @Override
    public long getNextCode() {
        return getHibernateTemplate().execute(new HibernateCallback<Long>() {
            @Override
            public Long doInHibernate(Session session) throws HibernateException, SQLException {
                String select = "SELECT MAX(code) FROM `material_in`";
                Query query  = session.createSQLQuery(select);
                if (query.list().get(0) != null) {
                    return Long.valueOf(query.list().get(0).toString()) + 1;
                }
                return 1l;
            }
        });
    }
}
