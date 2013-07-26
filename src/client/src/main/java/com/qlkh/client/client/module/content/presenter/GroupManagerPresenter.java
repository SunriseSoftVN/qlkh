package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.GroupManagerPlace;
import com.qlkh.client.client.module.content.view.GroupManagerView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.core.DeleteAction;
import com.qlkh.core.client.action.core.DeleteResult;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.model.Group;
import com.qlkh.core.client.model.MaterialIn;
import com.qlkh.core.client.model.Station;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class GroupManagerPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 7/26/13 9:27 AM
 */
@Presenter(view = GroupManagerView.class, place = GroupManagerPlace.class)
public class GroupManagerPresenter extends AbstractPresenter<GroupManagerView> {
    private static final String[] RELATE_ENTITY_NAMES = {MaterialIn.class.getName()};

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;

    private ListStore<BeanModel> stationListStore;
    private Group currentGroup;
    private Window groupEditWindow;

    @Override
    public void onActivate() {
        view.show();
        if (stationListStore != null) {
            //reload stations list.
            stationListStore = GridUtils.createListStoreForCb(Station.class);
            view.getCbbStation().setStore(stationListStore);
        }
        view.getPagingToolBar().refresh();
        view.getGroupGird().focus();
    }

    @Override
    protected void doBind() {
        stationListStore = GridUtils.createListStoreForCb(Station.class);
        view.getCbbStation().setStore(stationListStore);
        view.createGrid(GridUtils.createListStore(Group.class));
        view.getPagingToolBar().bind((PagingLoader<?>) view.getGroupGird().getStore().getLoader());
        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
        view.getBtnRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                view.getPagingToolBar().refresh();
            }
        });
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                groupEditWindow = view.createGroupEditWindow();
                currentGroup = new Group();
                groupEditWindow.show();
            }
        });
        view.getBtnEdit().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (view.getGroupGird().getSelectionModel().getSelectedItem() != null) {
                    Group selectGroup = view.getGroupGird().getSelectionModel().getSelectedItem().getBean();
                    currentGroup = selectGroup;
                    if (selectGroup != null) {
                        view.getTxtGroupName().setValue(currentGroup.getName());
                        BeanModel station = null;
                        for (BeanModel model : view.getCbbStation().getStore().getModels()) {
                            if (currentGroup.getStation().getId().
                                    equals(model.<Station>getBean().getId())) {
                                station = model;
                            }
                        }
                        view.getCbbStation().setValue(station);

                        groupEditWindow = view.createGroupEditWindow();
                        groupEditWindow.show();
                    }
                }
            }
        });
        view.getBtnGroupEditOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (view.getGroupEditPanel().isValid() && currentGroup != null) {
                    currentGroup.setName(view.getTxtGroupName().getValue());
                    currentGroup.setStation(view.getCbbStation().getValue().<Station>getBean());
                    currentGroup.setUpdateBy(1l);
                    currentGroup.setCreateBy(1l);
                    dispatch.execute(new SaveAction(currentGroup), new AbstractAsyncCallback<SaveResult>() {
                        @Override
                        public void onSuccess(SaveResult result) {
                            DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                            groupEditWindow.hide();
                            updateGrid(result.<Group>getEntity());
                        }
                    });
                }
            }
        });
        view.getBtnGroupEditCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                groupEditWindow.hide();
            }
        });
    }

    private void updateGrid(Group group) {
        boolean isNotFound = true;
        BeanModelFactory factory = BeanModelLookup.get().getFactory(Group.class);
        BeanModel updateModel = factory.createModel(group);
        for (BeanModel model : view.getGroupGird().getStore().getModels()) {
            if (group.getId().equals(model.<Group>getBean().getId())) {
                int index = view.getGroupGird().getStore().indexOf(model);
                view.getGroupGird().getStore().remove(model);
                view.getGroupGird().getStore().insert(updateModel, index);
                isNotFound = false;
            }
        }
        if (isNotFound) {
            view.getGroupGird().getStore().add(updateModel);
            view.getGroupGird().getView().ensureVisible(view.getGroupGird()
                    .getStore().getCount() - 1, 1, false);
        }
        view.getGroupGird().getSelectionModel().select(updateModel, false);
    }

    private class DeleteButtonEventListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            final List<BeanModel> models = view.getGroupGird().getSelectionModel().getSelectedItems();
            if (CollectionsUtils.isNotEmpty(models)) {
                if (models.size() == 1) {
                    final Group group = (Group) models.get(0).getBean();
                    showDeleteTagConform(group.getId(), group.getName());
                } else {
                    List<Long> groupIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        final Group group = model.getBean();
                        groupIds.add(group.getId());
                    }
                    showDeleteTagConform(groupIds, null);
                }
            }
        }
    }

    private void showDeleteTagConform(long groupId, String tagName) {
        List<Long> groupIds = new ArrayList<Long>(1);
        groupIds.add(groupId);
        showDeleteTagConform(groupIds, tagName);
    }

    private void showDeleteTagConform(final List<Long> groupIds, String tagName) {
        assert groupIds != null;
        String deleteMessage;
        final AsyncCallback<DeleteResult> callback = new AbstractAsyncCallback<DeleteResult>() {
            @Override
            public void onSuccess(DeleteResult result) {
                if (result.isDelete()) {
                    //Reload grid.
                    view.getPagingToolBar().refresh();
                    DiaLogUtils.notify(view.getConstant().deleteGroupMessageSuccess());
                } else {
                    DiaLogUtils.showMessage(view.getConstant().deleteErrorMessage());
                }
            }
        };
        final boolean hasManyTag = groupIds.size() > 1;
        if (hasManyTag) {
            deleteMessage = view.getConstant().deleteAllGroupMessage();
        } else {
            deleteMessage = StringUtils.substitute(view.getConstant().deleteGroupMessage(), tagName);
        }

        DiaLogUtils.conform(deleteMessage, new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked().getText().equals("Yes")) {
                    if (hasManyTag) {
                        dispatch.execute(new DeleteAction(Group.class.getName(), groupIds, RELATE_ENTITY_NAMES), callback);
                    } else {
                        dispatch.execute(new DeleteAction(Group.class.getName(), groupIds.get(0), RELATE_ENTITY_NAMES), callback);
                    }
                }
            }
        });
    }

    @Override
    public String mayStop() {
        if (groupEditWindow != null && groupEditWindow.isVisible()) {
            return view.getConstant().conformExitMessage();
        }
        return null;
    }

    @Override
    public void onCancel() {
        if (groupEditWindow != null && groupEditWindow.isVisible()) {
            groupEditWindow.hide();
        }
    }

}
