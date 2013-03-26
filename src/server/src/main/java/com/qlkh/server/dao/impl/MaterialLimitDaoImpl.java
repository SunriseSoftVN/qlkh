package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.MaterialLimit;
import com.qlkh.server.dao.MaterialLimitDao;
import com.qlkh.server.dao.core.AbstractDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The Class MaterialLimitDao.
 *
 * @author Nguyen Duc Dung
 * @since 3/27/13 3:51 AM
 */
public class MaterialLimitDaoImpl extends AbstractDao<MaterialLimit> implements MaterialLimitDao {
    @Override
    public List<MaterialLimit> findByTaskId(long taskId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(MaterialLimit.class)
                .add(Restrictions.eq("task.id", taskId));
        return getHibernateTemplate().findByCriteria(criteria);
    }
}
