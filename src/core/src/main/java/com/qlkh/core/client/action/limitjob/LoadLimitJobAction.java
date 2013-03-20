package com.qlkh.core.client.action.limitjob;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import net.customware.gwt.dispatch.shared.Action;

/**
 * The Class LoadLimitJobAction.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 2:25 PM
 */
public class LoadLimitJobAction implements Action<LoadLimitJobResult> {

    private BasePagingLoadConfig loadConfig;

    public LoadLimitJobAction() {
    }

    public LoadLimitJobAction(BasePagingLoadConfig loadConfig) {
        this.loadConfig = loadConfig;
    }

    public BasePagingLoadConfig getLoadConfig() {
        return loadConfig;
    }
}
