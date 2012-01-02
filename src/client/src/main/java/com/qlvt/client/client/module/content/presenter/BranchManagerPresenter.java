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

package com.qlvt.client.client.module.content.presenter;

import com.qlvt.client.client.module.content.place.BranchManagerPlace;
import com.qlvt.client.client.module.content.view.BranchManagerView;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

/**
 * The Class BranchManagerPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 1/2/12, 1:07 PM
 */
@Presenter(view = BranchManagerView.class, place = BranchManagerPlace.class)
public class BranchManagerPresenter extends AbstractPresenter<BranchManagerView> {
    @Override
    public void onActivate() {
        view.show();
    }
}
