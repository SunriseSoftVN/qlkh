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

package com.qlvt.client.client.module.main.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.widget.MyCenterLayoutContainer;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

@View(parentDomId = DomIdConstant.BOTTOM_PANEL)
public class CopyRightView extends AbstractView {

    MyCenterLayoutContainer layoutContainer = new MyCenterLayoutContainer();

    @Override
    protected void initializeView() {
        HTML html = new HTML("<p style='text-align:center'>" +
                "Copyright 2012 <a href='http://smvp4g.com'>smvp4g.com</a></p>");
        layoutContainer.add(html);
        layoutContainer.setPixelSize(Window.getClientWidth(), 100);
        layoutContainer.setFixedHeight(100);
        setWidget(layoutContainer);
    }
}
