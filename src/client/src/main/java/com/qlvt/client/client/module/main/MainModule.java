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

package com.qlvt.client.client.module.main;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.smvp4g.mvp.client.core.module.AbstractModule;
import com.smvp4g.mvp.client.core.module.annotation.Module;

/**
 * The Class MainModule.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 8:49 AM
 */
@Module
public class MainModule extends AbstractModule {
    @Override
    public void configure() {
        //Disable Window scroll's.
        Window.enableScrolling(false);
    }

    @Override
    public void start() {
        RootPanel.get("loading_panel").setVisible(false);
    }
}
