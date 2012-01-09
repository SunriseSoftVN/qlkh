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

package com.qlvt.client.client.widget;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;

/**
 * The Class MyCenterLayoutContainer.
 *
 * @author Nguyen Duc Dung
 * @since 1/9/12, 5:01 PM
 */
public class MyCenterLayoutContainer extends CenterLayoutContainer {

    private Integer fixedHeight;
    private int flexibleHeight;
    private Integer fixedWidth;
    private int flexibleWidth;

    public MyCenterLayoutContainer() {
        setMonitorWindowResize(false);
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                int width = event.getWidth();
                int height = event.getHeight();
                if (fixedWidth != null) {
                    width = fixedWidth;
                }
                if (fixedHeight != null) {
                    height = fixedHeight;
                }
                width += flexibleWidth;
                height += flexibleHeight;
                setPixelSize(width, height);
            }
        });
    }


    public void setFixedHeight(Integer fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    public void setFixedWidth(Integer fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    public void setFlexibleHeight(int flexibleHeight) {
        this.flexibleHeight = flexibleHeight;
    }

    public void setFlexibleWidth(int flexibleWidth) {
        this.flexibleWidth = flexibleWidth;
    }
}
