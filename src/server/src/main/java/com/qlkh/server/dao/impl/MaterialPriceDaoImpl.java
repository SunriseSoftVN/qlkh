package com.qlkh.server.dao.impl;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.model.MaterialLimit;
import com.qlkh.core.client.model.MaterialPrice;
import com.qlkh.core.client.model.core.AbstractEntity;
import com.qlkh.server.dao.MaterialPriceDao;
import com.qlkh.server.dao.core.AbstractDao;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The Class MaterialPriceDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 4/5/13 11:44 AM
 */
public class MaterialPriceDaoImpl extends AbstractDao<MaterialPrice> implements MaterialPriceDao {

    @Override
    public List<MaterialPrice> findByMaterialId(long materialId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(MaterialPrice.class)
                .add(Restrictions.eq("material.id", materialId));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<MaterialPrice> findByTime(QuarterEnum quarter, int year) {
        assert quarter != null;
        DetachedCriteria criteria = DetachedCriteria.forClass(MaterialPrice.class)
                .add(Restrictions.eq("year", year))
                .add(Restrictions.eq("quarter", quarter.getCode()));
        return getHibernateTemplate().findByCriteria(criteria);
    }
}
