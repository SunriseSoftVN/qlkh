/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.constant.TaskTypeEnum;
import com.qlkh.core.client.model.Material;
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
                String select = "SELECT `task`.`id` AS `bigId`," +
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
                    sql += "WHERE `task`.`id` IN (SELECT `taskid` FROM `material_limit`) ";
                } else if (hasNoLimit && !hasLimit) {
                    sql += "WHERE `task`.`id` NOT IN (SELECT `taskid` FROM `material_limit`) ";
                }

                if (!sql.contains("WHERE")) {
                    sql += "WHERE ";
                } else {
                    sql += "AND ";
                }

                sql += "`task`.`taskTypeCode` !=  " + TaskTypeEnum.NAM.getCode() +
                        " AND `task`.`taskTypeCode` != " + TaskTypeEnum.SUBSUM.getCode() +
                        " AND `task`.`taskTypeCode` != " + TaskTypeEnum.SUM.getCode();

                sql = createFilter(config, "task", sql);

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

    @Override
    public BasePagingLoadResult<Material> getMaterials(final int year, final QuarterEnum quarter, final BasePagingLoadConfig config) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<BasePagingLoadResult<Material>>() {
            @Override
            public BasePagingLoadResult<Material> doInHibernate(Session session) throws HibernateException, SQLException {
                String select = "SELECT `id` AS `bigId`," +
                        "`name` ," +
                        "`code` ," +
                        "`unit` ," +
                        "`note` " +
                        "FROM  `material` ";

                String count = "SELECT COUNT(*) FROM `material` ";

                String sql = "WHERE `id` NOT IN (SELECT `materialId` FROM `material_price` WHERE `year` = " + year + " AND `quarter` = " + quarter.getCode() + ")";
                sql = createFilter(config, "material", sql);

                SQLQuery selectQuery = session.createSQLQuery(select += sql);
                selectQuery.setResultTransformer(new AliasToBeanResultTransformer(Material.class));
                selectQuery.setMaxResults(config.getLimit());
                selectQuery.setFirstResult(config.getOffset());

                SQLQuery countQuery = session.createSQLQuery(count += sql);

                int total = Integer.valueOf(countQuery.list().get(0).toString());

                return new BasePagingLoadResult<Material>(selectQuery.list(), config.getOffset(), total);
            }
        });
    }

    @Override
    public BasePagingLoadResult<Material> getMaterials(final long taskId, final BasePagingLoadConfig config) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<BasePagingLoadResult<Material>>() {
            @Override
            public BasePagingLoadResult<Material> doInHibernate(Session session) throws HibernateException, SQLException {
                String select = "SELECT `id` AS `bigId`," +
                        "`name` ," +
                        "`code` ," +
                        "`unit` ," +
                        "`note` " +
                        "FROM  `material` ";

                String count = "SELECT COUNT(*) FROM `material` ";

                String sql = "WHERE `id` NOT IN (SELECT `materialId` FROM `material_limit` WHERE `taskId` = " + taskId + ")";
                sql = createFilter(config, "material", sql);

                SQLQuery selectQuery = session.createSQLQuery(select += sql);
                selectQuery.setResultTransformer(new AliasToBeanResultTransformer(Material.class));
                selectQuery.setMaxResults(config.getLimit());
                selectQuery.setFirstResult(config.getOffset());

                SQLQuery countQuery = session.createSQLQuery(count += sql);

                int total = Integer.valueOf(countQuery.list().get(0).toString());

                return new BasePagingLoadResult<Material>(selectQuery.list(), config.getOffset(), total);
            }
        });
    }

    private String createFilter(BasePagingLoadConfig config, String tableName, String query) {
        String sql = query;
        if (config.get("hasFilter") != null && (Boolean) config.get("hasFilter")) {
            Map<String, Object> filters = config.get("filters");
            if (filters != null) {
                if (!sql.contains("WHERE")) {
                    sql += " WHERE ";
                } else {
                    sql += " AND ";
                }
                sql += "(";
                for (String filter : filters.keySet()) {
                    sql += "`" + tableName + "`.`" + filter + "` LIKE '%" + filters.get(filter) + "%' OR ";
                }
                sql = sql.substring(0, sql.length() - 3) + ")";
            }
        }
        return sql;
    }
}
