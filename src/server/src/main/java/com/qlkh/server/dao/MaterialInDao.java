package com.qlkh.server.dao;

import com.qlkh.core.client.model.MaterialIn;
import com.qlkh.server.dao.core.Dao;

/**
 * The Class MaterialDao.
 *
 * @author Nguyen Duc Dung
 * @since 3/21/13 10:27 AM
 */
public interface MaterialInDao extends Dao<MaterialIn> {

    public long getNextCode();

}
