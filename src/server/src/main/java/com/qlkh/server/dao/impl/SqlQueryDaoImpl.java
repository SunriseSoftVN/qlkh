/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.view.SubAnnualTaskDetailDataView;
import com.qlkh.core.client.model.view.SubTaskDetailDataView;
import com.qlkh.core.client.model.view.TaskDetailDataView;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.dao.core.AbstractDao;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.List;

/**
 * The Class SqlQueryDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 6/6/12, 9:59 PM
 */
public class SqlQueryDaoImpl extends AbstractDao implements SqlQueryDao {

    @Override
    public List<SubAnnualTaskDetailDataView> getSubAnnualTaskDetailDataViews(final List<Long> stationIds, final int year) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<SubAnnualTaskDetailDataView>>() {
            @Override
            public List<SubAnnualTaskDetailDataView> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM subannualtaskdetail_view " +
                        "WHERE year = :year AND stationId in (:stationIds)");
                sqlQuery.setParameter("year", year);
                sqlQuery.setParameterList("stationIds", stationIds);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(SubAnnualTaskDetailDataView.class));
                return sqlQuery.list();
            }
        });
    }

    @Override
    public List<TaskDetailDataView> getTaskDetailViews(final int year) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<TaskDetailDataView>>() {
            @Override
            public List<TaskDetailDataView> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM  taskdetail_view " +
                        "WHERE year = :year");
                sqlQuery.setParameter("year", year);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(TaskDetailDataView.class));
                return sqlQuery.list();
            }
        });
    }

    @Override
    public List<SubTaskDetailDataView> getSubTaskDetailDataViews(final List<Long> stationIds, final int year) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<SubTaskDetailDataView>>() {
            @Override
            public List<SubTaskDetailDataView> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM subtaskdetail_view " +
                        "WHERE year = :year AND stationId in (:stationIds)");
                sqlQuery.setParameter("year", year);
                sqlQuery.setParameterList("stationIds", stationIds);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(SubTaskDetailDataView.class));
                return sqlQuery.list();
            }
        });
    }
}
