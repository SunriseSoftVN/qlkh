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

package com.qlvt.client.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.qlvt.core.client.model.User;
import com.smvp4g.mvp.client.AbstractEntryPoint;

import java.util.Date;

/**
 * The Class QlvtEntryPoint.
 *
 * @author Nguyen Duc Dung
 * @since 12/27/11, 10:31 AM
 */
public class QlvtEntryPoint extends AbstractEntryPoint {


    @Override
    public void onModuleLoad() {
        final User user = new User();
        user.setUserName("dung");
        user.setPassWord("ducdung");
        user.setCreatedDate(new Date());
        user.setUpdatedDate(new Date());
        user.setCreateBy(1l);
        user.setUpdateBy(1l);
        Button button = new Button("test");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Test.App.getInstance().test(user, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(String result) {
                        Window.alert(result);
                    }
                });
            }
        });

        Button button1 = new Button("test 2");
        button1.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            }
        });

        RootPanel.get().add(button);
    }
}
