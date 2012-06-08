/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.core.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.utils.DiaLogUtils;

/**
 * The Class AbstractAsyncCallback. Using to show dialog message and log RPC error message.
 *
 * @author dungvn3000
 * @since 6/27/11, 7:50 PM
 */
public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T> {

    @Override
    public void onFailure(Throwable caught) {
        DiaLogUtils.logAndShowRpcErrorMessage(caught);
    }

}
