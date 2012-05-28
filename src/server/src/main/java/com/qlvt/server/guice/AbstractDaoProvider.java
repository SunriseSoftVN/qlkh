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

package com.qlvt.server.guice;

import com.google.inject.Provider;
import com.qlvt.core.client.model.core.AbstractEntity;
import com.qlvt.server.dao.core.Dao;

/**
 * The Class AbstractDaoProvider.
 *
 * @author Nguyen Duc Dung
 * @since 5/28/12, 2:38 PM
 */
public abstract class AbstractDaoProvider implements Provider<Dao<? extends AbstractEntity>> {

    /**
     * We don't use this method, in this project, it's always return null.
     * @return null
     */
    @Deprecated
    @Override
    public Dao<? extends AbstractEntity> get() {
        return null;
    }
}
