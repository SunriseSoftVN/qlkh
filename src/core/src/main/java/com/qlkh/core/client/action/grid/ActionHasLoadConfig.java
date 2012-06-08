/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.action.grid;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class HasLoadConfig.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/12, 4:20 PM
 */
public interface ActionHasLoadConfig<R extends Result> extends Action<R> {
    public BasePagingLoadConfig getConfig();

    void setConfig(BasePagingLoadConfig config);
}
