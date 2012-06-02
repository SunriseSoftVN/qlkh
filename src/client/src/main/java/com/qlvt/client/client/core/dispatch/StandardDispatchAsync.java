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

package com.qlvt.client.client.core.dispatch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.qlvt.client.client.utils.ServiceUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.AbstractDispatchAsync;
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

    private static final String DISPATCH_SERVICE_RELATIVE_PATH = "dispatch";

    private static final StandardDispatchServiceAsync realService = GWT.create(StandardDispatchService.class);

    public StandardDispatchAsync(ExceptionHandler exceptionHandler) {
        super(exceptionHandler);
        setServiceEntryPoint(realService);
    }

    public <A extends Action<R>, R extends Result> void execute(final A action, final AsyncCallback<R> callback) {
        realService.execute(action, new AsyncCallback<Result>() {
            public void onFailure(Throwable caught) {
                StandardDispatchAsync.this.onFailure(action, caught, callback);
            }

            @SuppressWarnings("unchecked")
            public void onSuccess(Result result) {
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
}
