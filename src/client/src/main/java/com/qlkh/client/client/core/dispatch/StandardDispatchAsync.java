/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.core.dispatch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.qlkh.client.client.utils.LoadingUtils;
import com.qlkh.client.client.utils.ServiceUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.AbstractDispatchAsync;
import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.ExceptionHandler;
import net.customware.gwt.dispatch.client.standard.StandardDispatchService;
import net.customware.gwt.dispatch.client.standard.StandardDispatchServiceAsync;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class StandardDispatchAsync.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 1:42 AM
 */
public class StandardDispatchAsync extends AbstractDispatchAsync {

    private static final StandardDispatchServiceAsync realService = GWT.create(StandardDispatchService.class);
    private static final String DISPATCH_SERVICE_RELATIVE_PATH = "dispatch";
    private static final int TIME_DELAY = 300;

    public static final StandardDispatchAsync INSTANCE = new StandardDispatchAsync(new DefaultExceptionHandler());

    private StandardDispatchAsync(ExceptionHandler exceptionHandler) {
        super(exceptionHandler);
        setServiceEntryPoint(realService);
    }

    public <A extends Action<R>, R extends Result> void execute(final A action, final AsyncCallback<R> callback) {
        LoadingUtils.showLoading();
        realService.execute(action, new AsyncCallback<Result>() {
            public void onFailure(Throwable caught) {
                hideLoadingPanel();
                StandardDispatchAsync.this.onFailure(action, caught, callback);
            }

            @SuppressWarnings("unchecked")
            public void onSuccess(Result result) {
                hideLoadingPanel();
                StandardDispatchAsync.this.onSuccess(action, (R) result, callback);
            }
        });
    }

    private void setServiceEntryPoint(StandardDispatchServiceAsync serviceAsync) {
        if (StringUtils.isNotEmpty(ServiceUtils.getServiceEntryPoint())) {
            ServiceDefTarget endpoint = (ServiceDefTarget) serviceAsync;
            endpoint.setServiceEntryPoint(StringUtils.concatUrlPath(ServiceUtils.getServiceEntryPoint(),
                    DISPATCH_SERVICE_RELATIVE_PATH));
        }
    }

    private void hideLoadingPanel() {
        Timer timer = new Timer() {
            @Override
            public void run() {
                LoadingUtils.hideLoading();
            }
        };
        timer.schedule(TIME_DELAY);
    }
}
