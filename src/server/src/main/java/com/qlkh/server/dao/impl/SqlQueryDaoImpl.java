/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.view.TaskDetailDKDataView;
import com.qlkh.core.client.model.view.TaskDetailKDKDataView;
import com.qlkh.core.client.model.view.TaskDetailNamDataView;
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
    public List<TaskDetailNamDataView> getTaskDetailNam(final List<Long> stationIds, final int year) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<TaskDetailNamDataView>>() {
            @Override
            public List<TaskDetailNamDataView> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM taskdetail_nam_view " +
                        "WHERE year = :year AND stationId in (:stationIds)");
                sqlQuery.setParameter("year", year);
                sqlQuery.setParameterList("stationIds", stationIds);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(TaskDetailNamDataView.class));
                return sqlQuery.list();
            }
        });
    }

    @Override
    public List<TaskDetailDKDataView> getTaskDetailDK(final List<Long> stationIds, final int year) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<TaskDetailDKDataView>>() {
            @Override
            public List<TaskDetailDKDataView> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM taskdetail_dk_view " +
                        "WHERE year = :year AND stationId in (:stationIds)");
                sqlQuery.setParameter("year", year);
                sqlQuery.setParameterList("stationIds", stationIds);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(TaskDetailDKDataView.class));
                return sqlQuery.list();
            }
        });
    }

    @Override
    public List<TaskDetailKDKDataView> getTaskDetailKDK(final List<Long> stationIds, final int year) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<TaskDetailKDKDataView>>() {
            @Override
            public List<TaskDetailKDKDataView> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM taskdetail_kdk_view " +
                        "WHERE year = :year AND stationId in (:stationIds)");
                sqlQuery.setParameter("year", year);
                sqlQuery.setParameterList("stationIds", stationIds);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(TaskDetailKDKDataView.class));
                return sqlQuery.list();
            }
        });
    }

    @Override
    public List<Task> getTasksHasLimit() {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<Task>>() {
            @Override
            public List<Task> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM `task` INNER JOIN `material` where `task`.`id` = `material`.`taskid`");
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(Task.class));
                return sqlQuery.list();
            }
        });
    }
}
