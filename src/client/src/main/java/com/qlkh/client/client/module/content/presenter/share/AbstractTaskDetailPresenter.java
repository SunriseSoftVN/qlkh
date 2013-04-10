/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.presenter.share;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.event.dom.client.KeyCodes;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.view.TaskDetailDKView;
import com.qlkh.client.client.module.content.view.i18n.TaskDetailDKConstant;
import com.qlkh.client.client.module.content.view.share.AbstractTaskDetailView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.action.station.LoadStationAction;
import com.qlkh.core.client.action.station.LoadStationResult;
import com.qlkh.core.client.criterion.ClientRestrictions;
import com.qlkh.core.client.model.Station;
import com.qlkh.core.client.model.Task;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.utils.LoginUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class AbstractTaskDetailPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 6/11/12, 6:20 PM
 */
public class AbstractTaskDetailPresenter<V extends
        AbstractTaskDetailView<? extends TaskDetailDKConstant>> extends AbstractPresenter<V> {

    protected DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;
    protected Station currentStation;
    protected Task currentTask;

    @Override
    public void onActivate() {
        view.show();
        if (currentStation != null) {
            //Reload view
            resetView();
        }
        if (view.getTaskGird() != null) {
            view.getTaskGird().focus();
        }
    }

    @Override
    protected void doBind() {
        createTaskGrid();

        view.getBtnRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                resetFilter();
                resetView();
            }
        });

        view.getTxtSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                String st = view.getTxtSearch().getValue();
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    if (StringUtils.isNotBlank(st)) {
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getTaskGird().
                                getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put(TaskDetailDKView.TASK_NAME_COLUMN,
                                view.getTxtSearch().getValue());
                        filters.put(TaskDetailDKView.TASK_CODE_COLUMN,
                                view.getTxtSearch().getValue());
                        loadConfig.set("filters", filters);
                    } else {
                        resetFilter();
                    }
                    resetView();
                } else if (event.getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    resetFilter();
                    com.google.gwt.user.client.Timer timer = new com.google.gwt.user.client.Timer() {
                        @Override
                        public void run() {
                            resetView();
                        }
                    };
                    timer.schedule(100);
                }
            }
        });
        view.getBtnSubTaskRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (currentTask != null) {
                    view.getSubTaskPagingToolBar().refresh();
                }
            }
        });
        view.getBtnSubTaskSave().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                List entities = new ArrayList();
                for (BeanModel model : view.getSubTaskDetailGird().getStore().getModels()) {
                    entities.add(model.getBean());
                }
                dispatch.execute(new SaveAction(entities), new AbstractAsyncCallback<SaveResult>() {
                    @Override
                    public void onSuccess(SaveResult result) {
                        DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                        view.getSubTaskPagingToolBar().refresh();
                    }
                });
            }
        });
    }

    protected void resetFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getTaskGird().
                getStore().getLoadConfig();
        loadConfig.set("hasFilter", false);
        loadConfig.set("filters", null);
        view.getTxtSearch().clear();
    }


    protected void resetView() {
        view.getTaskPagingToolBar().refresh();
        emptySubGird();
    }

    protected void emptySubGird() {
        if (view.getSubTaskDetailGird() != null) {
            view.getSubTaskDetailGird().getStore().removeAll();
            currentTask = null;
        }
    }

    protected ListStore<BeanModel> createSubTaskListStore() {
        return null;
    }

    protected void createTaskGrid() {
        dispatch.execute(new LoadStationAction(LoginUtils.getUserName()), new AbstractAsyncCallback<LoadStationResult>() {
            @Override
            public void onSuccess(LoadStationResult result) {
                currentStation = result.getStation();

                view.createTaskGrid(GridUtils.createListStore(Task.class, ClientRestrictions.in("taskTypeCode", getTaskTypeCode())));
                view.getTaskPagingToolBar().bind((PagingLoader<?>) view.getTaskGird().getStore().getLoader());
                resetView();
                view.getTaskGird().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
                    @Override
                    public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
                        int index = se.getSelection().size() - 1;
                        if (index >= 0) {
                            Task task = se.getSelection().get(index).getBean();
                            if (task.getId() != null) {
                                currentTask = task;
                                view.getSubTaskPagingToolBar().refresh();
                            } else {
                                emptySubGird();
                            }
                        }
                    }
                });
                view.getTaskGird().focus();

                createSubTaskGrid();
            }
        });
    }

    protected void createSubTaskGrid() {

    }

    protected Integer[] getTaskTypeCode() {
        return null;
    }
}
