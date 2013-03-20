package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.LimitJobPlace;
import com.qlkh.client.client.module.content.presenter.share.AbstractTaskDetailPresenter;
import com.qlkh.client.client.module.content.view.LimitJobView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailDKAction;
import com.qlkh.core.client.action.taskdetail.LoadTaskDetailDKResult;
import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.core.client.constant.StationLockTypeEnum;
import com.qlkh.core.client.model.StationLock;
import com.qlkh.core.client.model.TaskDetailDK;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

import static com.qlkh.core.client.constant.TaskTypeEnum.DK;

/**
 * The Class LimitJobPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 10:10 AM
 */
@Presenter(view = LimitJobView.class, place = LimitJobPlace.class)
public class LimitJobPresenter extends AbstractTaskDetailPresenter<LimitJobView> {

    @Override
    protected ListStore<BeanModel> createSubTaskListStore() {
        RpcProxy<LoadTaskDetailDKResult> rpcProxy = new RpcProxy<LoadTaskDetailDKResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadTaskDetailDKResult> callback) {
                long currentTaskId = -1;
                if (currentTask != null) {
                    currentTaskId = currentTask.getId();
                }
                dispatch.execute(new LoadTaskDetailDKAction((BasePagingLoadConfig) loadConfig,
                        currentTaskId, currentStation.getId()), callback);
            }
        };

        PagingLoader<PagingLoadResult<TaskDetailDK>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<TaskDetailDK>>(rpcProxy, new LoadGridDataReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<BeanModel>(pagingLoader);
    }

    @Override
    protected void createSubTaskGrid() {
        view.createSubTaskGrid(createSubTaskListStore());
        view.getSubTaskPagingToolBar().bind((PagingLoader<?>) view.getSubTaskDetailGird().getStore().getLoader());
    }

    @Override
    protected Integer[] getTaskTypeCode() {
        return new Integer[]{DK.getCode()};
    }

}
