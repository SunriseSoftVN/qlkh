package com.qlkh.server.dao.impl;

import com.qlkh.core.client.model.Branch;
import com.qlkh.core.client.model.Material;
import com.qlkh.server.dao.MaterialDao;
import com.qlkh.server.dao.core.AbstractDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The Class MaterialDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 3/21/13 10:28 AM
 */
public class MaterialDaoImpl extends AbstractDao<Material> implements MaterialDao {

    @Override
    public List<Material> findByTaskId(long taskId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Material.class)
                .add(Restrictions.eq("task.id", taskId));
        return getHibernateTemplate().findByCriteria(criteria);
    }


}
