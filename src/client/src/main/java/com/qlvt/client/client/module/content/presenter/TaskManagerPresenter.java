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

package com.qlvt.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlvt.client.client.module.content.place.TaskManagerPlace;
import com.qlvt.client.client.module.content.view.TaskManagerView;
import com.qlvt.client.client.service.TaskService;
import com.qlvt.client.client.service.TaskServiceAsync;
import com.qlvt.client.client.utils.DiaLogUtils;
import com.qlvt.core.client.model.Task;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

import java.util.Date;
import java.util.List;

/**
 * The Class TaskManagerPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 12/31/11, 2:48 PM
 */
@Presenter(view = TaskManagerView.class, place = TaskManagerPlace.class)
public class TaskManagerPresenter extends AbstractPresenter<TaskManagerView> {

    private TaskServiceAsync taskService = TaskService.App.getInstance();

    @Override
    public void onActivate() {
        view.show();
        view.getPagingToolBar().refresh();
    }

    @Override
    protected void doBind() {
        view.createGrid(createTaskListStore());
        view.getPagingToolBar().bind((PagingLoader<?>) view.getTaskGird().getStore().getLoader());
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                Task task = new Task();
                task.setCreateBy(1l);
                task.setUpdateBy(1l);
                task.setCreatedDate(new Date());
                task.setUpdatedDate(new Date());
                BeanModelFactory factory = BeanModelLookup.get().getFactory(Task.class);
                BeanModel model = factory.createModel(task);
                view.getTaskGird().getStore().insert(model, view.getTaskGird().getStore().getCount());
            }
        });
    }

    private ListStore<BeanModel> createTaskListStore() {
        RpcProxy<BasePagingLoadResult<List<Task>>> rpcProxy = new RpcProxy<BasePagingLoadResult<List<Task>>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<BasePagingLoadResult<List<Task>>> callback) {
                taskService.getTasksForGrid((BasePagingLoadConfig) loadConfig, callback);
            }
        };

        PagingLoader<PagingLoadResult<Task>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<Task>>(rpcProxy, new BeanModelReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<BeanModel>(pagingLoader);
    }
}
