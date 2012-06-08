/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.core;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.model.core.AbstractEntity;
import org.hibernate.criterion.Criterion;

/**
 * The Class Dao.
 *
 * @author Nguyen Duc Dung
 * @since 8/16/11, 10:35 AM
 */
public interface Dao<E extends AbstractEntity> {
    <E extends AbstractEntity> BasePagingLoadResult<E> getByBeanConfig(String entityName,
                                                                       BasePagingLoadConfig config, Criterion... criterions);
}
