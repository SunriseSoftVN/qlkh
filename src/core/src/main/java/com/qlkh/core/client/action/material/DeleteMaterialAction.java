package com.qlkh.core.client.action.material;

import net.customware.gwt.dispatch.shared.Action;

import java.util.List;

/**
 * The Class DeleteMaterialAction.
 *
 * @author Nguyen Duc Dung
 * @since 3/26/13 8:06 PM
 */
public class DeleteMaterialAction implements Action<DeleteMaterialResult> {

    private List<Long> ids;
    private long id;
    private boolean forceDelete;

    public DeleteMaterialAction() {
    }

    public DeleteMaterialAction(List<Long> ids) {
        this.ids = ids;
    }

    public DeleteMaterialAction(long id) {
        this.id = id;
    }

    public DeleteMaterialAction(List<Long> ids, boolean forceDelete) {
        this.ids = ids;
        this.forceDelete = forceDelete;
    }

    public DeleteMaterialAction(boolean forceDelete, long id) {
        this.forceDelete = forceDelete;
        this.id = id;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isForceDelete() {
        return forceDelete;
    }

    public void setForceDelete(boolean forceDelete) {
        this.forceDelete = forceDelete;
    }
}
