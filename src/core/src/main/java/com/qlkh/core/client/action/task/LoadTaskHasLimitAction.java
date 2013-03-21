package com.qlkh.core.client.action.task;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadTaskHasLimitAction.
 *
 * @author Nguyen Duc Dung
 * @since 3/21/13 10:59 AM
 */
public class LoadTaskHasLimitAction implements Action<LoadTaskHasLimitResult> {

    private boolean hasLimit = true;
    private boolean hasNoLimit = true;
    private BasePagingLoadConfig config;

    public LoadTaskHasLimitAction() {
    }

    public LoadTaskHasLimitAction(boolean hasLimit, boolean hasNoLimit, BasePagingLoadConfig config) {
        this.hasLimit = hasLimit;
        this.hasNoLimit = hasNoLimit;
        this.config = config;
    }

    public BasePagingLoadConfig getConfig() {
        return config;
    }

    public boolean isHasNoLimit() {
        return hasNoLimit;
    }

    public boolean isHasLimit() {
        return hasLimit;
    }
}
