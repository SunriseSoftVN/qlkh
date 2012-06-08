/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.utils;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.qlkh.core.configuration.ConfigurationClientUtil;

/**
 * The Class DiaLogUtils.
 *
 * @author Nguyen Duc Dung
 * @since 8/16/11, 10:09 AM
 */
public final class DiaLogUtils {

    private static final String APP_MESSAGE_TITLE = ConfigurationClientUtil.getConfiguration().applicationName();

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
        final com.extjs.gxt.ui.client.widget.Window window = new com.extjs.gxt.ui.client.widget.Window();
        Button btnReload = new Button("Reload");
        Button btnOk = new Button("Ok");
        btnOk.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                window.hide();
            }
        });
        btnReload.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                Window.Location.reload();
            }
        });
        window.addButton(btnReload);
        window.addButton(btnOk);
        window.add(new HTML(String.valueOf(message)));
        window.setResizable(false);
        window.setModal(true);
        window.setHeading(APP_MESSAGE_TITLE);
        window.setFocusWidget(btnOk);
        window.show();
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
