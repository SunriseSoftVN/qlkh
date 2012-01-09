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

package com.qlvt.client.client.utils;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.Window;
import com.qlvt.core.system.SystemUtil;

/**
 * The Class DiaLogUtils.
 *
 * @author Nguyen Duc Dung
 * @since 8/16/11, 10:09 AM
 */
public final class DiaLogUtils {

    private static final String APP_MESSAGE_TITLE = SystemUtil.getConfiguration().applicationName();

    private DiaLogUtils() {
        //Hide it.
    }

    /**
     * Log and show error for rpc exception.
     *
     * @param caught
     */
    public static void logAndShowRpcErrorMessage(Throwable caught) {
        logAndShowErrorMessage(ApplicationMessageUtils.getConstants().rpcErrorMessage(), caught);
    }

    /**
     * Log and show error for exception.
     *
     * @param message
     * @param caught
     */
    public static void logAndShowErrorMessage(Object message, Throwable caught) {
        Log.error(caught.getMessage(), caught);
        showMessage(message);
    }

    /**
     * Show message box to notify user.
     */
    public static void showMessage(Object message) {
        showMessage(APP_MESSAGE_TITLE, message);
    }

    /**
     * Show message box to notify user.
     *
     * @param title
     * @param message
     */
    public static void showMessage(String title, Object message) {
        MessageBox.alert(title, String.valueOf(message), null);
    }

    /**
     * Show conform dialog.
     *
     * @param message
     * @param listener
     */
    public static void conform(Object message, Listener<MessageBoxEvent> listener) {
        MessageBox.confirm(APP_MESSAGE_TITLE, String.valueOf(message), listener);
    }

    /**
     * Show message alert.
     * <p/>
     * Using like <b>Window.alert(String message);</b>
     *
     * @param message
     */
    public static void alert(Object message) {
        Window.alert(String.valueOf(message));
    }

    /**
     * Show notify dialog to user.
     *
     * @param message
     */
    public static void notify(Object message) {
        Info.display(APP_MESSAGE_TITLE, String.valueOf(message));
    }
}
