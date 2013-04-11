package com.qlkh.server.dao.impl;

import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.model.MaterialPrice;
import com.qlkh.server.dao.MaterialPriceDao;
import com.qlkh.server.dao.core.AbstractDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
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

    @Override
    public void copyData(QuarterEnum form, QuarterEnum to, int fromYear, int toYear) {
        List<MaterialPrice> fromMaterialPrices = findByTime(form, fromYear);
        List<MaterialPrice> toMaterialPrices = findByTime(to, toYear);

        List<MaterialPrice> copyData = new ArrayList<MaterialPrice>();

        for (MaterialPrice fromMaterialPrice : fromMaterialPrices) {
            boolean isFound = false;
            for (MaterialPrice toMaterialPrice : toMaterialPrices) {
                if(fromMaterialPrice.getMaterial().getId().equals(toMaterialPrice.getMaterial().getId())) {
                    isFound = true;
                }
            }

            if(!isFound) {
                copyData.add(fromMaterialPrice);
            }
        }

        if(!copyData.isEmpty()) {
            List<MaterialPrice> newPrices = new ArrayList<MaterialPrice>();
            for (MaterialPrice copyPrice : copyData) {
                //Set id to null make it

                MaterialPrice newPrice = new MaterialPrice();
                newPrice.setMaterial(copyPrice.getMaterial());
                newPrice.setPrice(copyPrice.getPrice());
                newPrice.setQuarter(to.getCode());
                newPrice.setYear(toYear);
                newPrice.setCreateBy(1l);
                newPrice.setUpdateBy(1l);
                newPrices.add(newPrice);
            }

            getHibernateTemplate().saveOrUpdateAll(newPrices);
        }
    }
}
