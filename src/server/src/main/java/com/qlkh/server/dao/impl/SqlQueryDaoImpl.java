/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
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
import java.util.Map;

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
                        "WHERE year = :year AND stationId IN (:stationIds)");
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
                        "WHERE year = :year AND stationId IN (:stationIds)");
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
                        "WHERE year = :year AND stationId IN (:stationIds)");
                sqlQuery.setParameter("year", year);
                sqlQuery.setParameterList("stationIds", stationIds);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(TaskDetailKDKDataView.class));
                return sqlQuery.list();
            }
        });
    }

    @Override
    public BasePagingLoadResult<Task> getTasks(final boolean hasLimit, final boolean hasNoLimit, final BasePagingLoadConfig config) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<BasePagingLoadResult<Task>>() {
            @Override
            public BasePagingLoadResult<Task> doInHibernate(Session session) throws HibernateException, SQLException {
                String select = "SELECT  `task`.`id` AS `bigId`," +
                        "`task`.`name` ," +
                        "`task`.`code`, " +
                        "`task`.`defaultValue`," +
                        "`task`.`unit`," +
                        "`task`.`quota`," +
                        "`task`.`dynamicQuota`," +
                        "`task`.`taskTypeCode`," +
                        "`task`.`childTasks`" +
                        "FROM  `task` ";

                String count = "SELECT COUNT(*) FROM `task` ";

                String sql = "";

                if (hasLimit && !hasNoLimit) {
                    sql += "INNER JOIN  `material` " +
                            "WHERE  `task`.`id` =  `material`.`taskid` ";
                } else if (hasNoLimit && !hasLimit) {
                    sql += "INNER JOIN  `material` " +
                            "WHERE  `task`.`id` !=  `material`.`taskid` ";
                }

                if (config.get("hasFilter") != null && (Boolean) config.get("hasFilter")) {
                    Map<String, Object> filters = config.get("filters");
                    if (filters != null) {
                        if (!sql.contains("WHERE")) {
                            sql += "WHERE ";
                        } else {
                            sql += "AND ";
                        }
                        sql += "(";
                        for (String filter : filters.keySet()) {
                            sql += "`task`.`" + filter + "` LIKE '%" + filters.get(filter) + "%' OR ";
                        }
                        sql = sql.substring(0, sql.length() - 3) + ")";
                    }
                }

                SQLQuery selectQuery = session.createSQLQuery(select += sql);
                selectQuery.setResultTransformer(new AliasToBeanResultTransformer(Task.class));
                selectQuery.setMaxResults(config.getLimit());
                selectQuery.setFirstResult(config.getOffset());

                SQLQuery countQuery = session.createSQLQuery(count += sql);

                int total = Integer.valueOf(countQuery.list().get(0).toString());

                return new BasePagingLoadResult<Task>(selectQuery.list(), config.getOffset(), total);
            }
        });
    }
}
