package com.qlkh.server.dao;

import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.model.MaterialLimit;
import com.qlkh.core.client.model.MaterialPrice;
import com.qlkh.server.dao.core.Dao;

import java.util.List;

/**
 * The Class MaterialDao.
 *
 * @author Nguyen Duc Dung
 * @since 3/21/13 10:27 AM
 */
public interface MaterialPriceDao extends Dao<MaterialPrice> {

    public List<MaterialPrice> findByMaterialId(long materialId);

    public List<MaterialPrice> findByTime(QuarterEnum quarter, int year);

}
