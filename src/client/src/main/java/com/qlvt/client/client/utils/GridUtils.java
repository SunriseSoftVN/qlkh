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

package com.qlvt.client.client.utils;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.client.client.core.dispatch.StandardDispatchAsync;
import com.qlvt.client.client.core.reader.LoadGridDataReader;
import com.qlvt.client.client.core.rpc.AbstractAsyncCallback;
import com.qlvt.core.client.action.core.LoadAction;
import com.qlvt.core.client.action.core.LoadResult;
import com.qlvt.core.client.action.grid.ActionHasLoadConfig;
import com.qlvt.core.client.action.grid.LoadGridDataAction;
import com.qlvt.core.client.action.grid.LoadGridDataResult;
import com.qlvt.core.client.criterion.ClientCriteria;
import com.qlvt.core.client.model.core.AbstractEntity;

/**
 * The Class GridUtils.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 11:14 AM
 */
public final class GridUtils {

    private GridUtils() {
    }

    /**
     * Create ListStore for GXT Grid.
     *
     * @param entityClass
     * @param <E>
     * @return
     */
    public static <E extends AbstractEntity> ListStore<BeanModel> createListStore(final Class<E> entityClass,
                                                                                  final ClientCriteria... criterion) {
        return createListStore(entityClass, new LoadGridDataAction(entityClass.getName(), criterion));
    }

    /**
     * Create ListStore for GXT Grid.
     *
     * @param entityClass using for generic
     * @param loadAction
     * @param <E>         Entity
     * @param <A>         Action
     * @param <R>         Result
     * @return
     */
    public static <E extends AbstractEntity, A extends ActionHasLoadConfig<R>,
            R extends LoadGridDataResult> ListStore<BeanModel> createListStore(Class<E> entityClass, final A loadAction) {
        RpcProxy<R> rpcProxy = new RpcProxy<R>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<R> callback) {
                loadAction.setConfig((BasePagingLoadConfig) loadConfig);
                StandardDispatchAsync.INSTANCE
                        .execute(loadAction, callback);
            }
        };
        PagingLoader<PagingLoadResult<E>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<E>>(rpcProxy, new LoadGridDataReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };
        return new ListStore<BeanModel>(pagingLoader);
    }

    /**
     * Create ListStore for GXT ComboBox.
     *
     * @param entityClass
     * @param <E>
     * @return
     */
    public static <E extends AbstractEntity> ListStore<BeanModel> createListStoreForCb(Class<E> entityClass,
                                                                                       ClientCriteria... criterion) {
        final BeanModelFactory factory = BeanModelLookup.get().getFactory(entityClass);
        final ListStore<BeanModel> store = new ListStore<BeanModel>();
        StandardDispatchAsync.INSTANCE
                .execute(new LoadAction(entityClass.getName(), criterion),
                        new AbstractAsyncCallback<LoadResult>() {
                            @Override
                            public void onSuccess(LoadResult result) {
                                for (E entity : result.<E>getList()) {
                                    store.add(factory.createModel(entity));
                                }
                            }
                        });
        return store;
    }

}
