package com.qlkh.server.dao;

import com.qlkh.core.client.model.Material;
import com.qlkh.server.dao.core.Dao;

import java.util.List;

/**
 * The Class MaterialDao.
 *
 * @author Nguyen Duc Dung
 * @since 3/21/13 10:27 AM
 */
public interface MaterialDao extends Dao<Material> {

    public List<Material> findByTaskId(long taskId);

}
