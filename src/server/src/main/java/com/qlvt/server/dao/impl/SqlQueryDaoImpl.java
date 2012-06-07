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

package com.qlvt.server.dao.impl;

import com.qlvt.core.client.model.view.SubAnnualTaskDetailDataView;
import com.qlvt.core.client.model.view.SubTaskDetailDataView;
import com.qlvt.core.client.model.view.TaskDetailDataView;
import com.qlvt.server.dao.SqlQueryDao;
import com.qlvt.server.dao.core.AbstractDao;
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
