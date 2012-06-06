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

package com.qlvt.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Window;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.view.i18n.StationLockConstants;
import com.qlvt.client.client.module.content.view.security.StationLockViewSecurity;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class StationLockView.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/12, 9:40 PM
 */
@ViewSecurity(configuratorClass = StationLockViewSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = StationLockConstants.class)
public class StationLockView extends AbstractView<StationLockConstants> {

    @I18nField
    FormPanel annualLockPanel = new FormPanel();

    @I18nField
    FormPanel normalLockPanel = new FormPanel();

    private HorizontalPanel horizontalPanel = new HorizontalPanel();
    private VerticalPanel annualStationNamePanel = new VerticalPanel();
    private VerticalPanel annualButtonPanel = new VerticalPanel();
    private VerticalPanel normalStationNamePanel = new VerticalPanel();
    private VerticalPanel normalButtonPanel = new VerticalPanel();
    private ContentPanel contentPanel = new ContentPanel();

    private List<CheckBox> annualCbs = new ArrayList<CheckBox>();
    public CheckBox annualCompanyCb;

    @Override
    protected void initializeView() {
        annualLockPanel.setFrame(true);
        annualLockPanel.setAutoHeight(true);
        annualLockPanel.setAutoWidth(true);
        HorizontalPanel hp1 = new HorizontalPanel();
        hp1.setSpacing(7);
        hp1.add(annualStationNamePanel);
        hp1.add(annualButtonPanel);
        annualLockPanel.add(hp1);

        normalLockPanel.setFrame(true);
        normalLockPanel.setAutoWidth(true);
        normalLockPanel.setAutoHeight(true);
        HorizontalPanel hp2 = new HorizontalPanel();
        hp2.setSpacing(7);
        hp2.add(normalStationNamePanel);
        hp2.add(normalButtonPanel);
        normalLockPanel.add(hp2);

        horizontalPanel.setAutoWidth(true);
        horizontalPanel.setSpacing(5);
        horizontalPanel.add(annualLockPanel);
        horizontalPanel.add(normalLockPanel);
        contentPanel.add(horizontalPanel);

        contentPanel.setHeaderVisible(false);
        contentPanel.setFrame(true);
        contentPanel.setHeight(Window.getClientHeight() - 90);
        contentPanel.setLayout(new RowLayout(Style.Orientation.HORIZONTAL));
        setWidget(contentPanel);
    }

    public void addStationName(String stationName) {
        annualStationNamePanel.add(new Label(stationName));
        normalStationNamePanel.add(new Label(stationName));
    }

    public void addAnnualButton(CheckBox checkBox, boolean isCompanyCheckBox) {
        annualButtonPanel.add(checkBox);
        if (!isCompanyCheckBox) {
            annualCbs.add(checkBox);
        } else {
            annualCompanyCb = checkBox;
        }
    }

    public void addNormalButton(CheckBox cbQ1, CheckBox cbQ2, CheckBox cbQ3, CheckBox cbQ4) {
        HorizontalPanel hp = new HorizontalPanel();
        cbQ1.setBoxLabel(getConstant().btnQ1());
        cbQ2.setBoxLabel(getConstant().btnQ2());
        cbQ3.setBoxLabel(getConstant().btnQ3());
        cbQ4.setBoxLabel(getConstant().btnQ4());
        hp.add(cbQ1);
        hp.add(cbQ2);
        hp.add(cbQ3);
        hp.add(cbQ4);
        normalButtonPanel.add(hp);
    }

    public void checkAllAnnualLock(boolean value) {
        for (CheckBox checkBox : annualCbs) {
            checkBox.disableEvents(true);
            checkBox.setValue(value);
            checkBox.disableEvents(false);
        }
    }

    public void enableAllAnnualLock(boolean enabled) {
        for (CheckBox checkBox : annualCbs) {
            checkBox.setEnabled(enabled);
            checkBox.enableEvents(enabled);
        }
    }

    public void checkAnnualCompanyCb() {
        boolean check = true;
        for (CheckBox checkBox : annualCbs) {
            if (!checkBox.getValue()) {
                check = false;
                break;
            }
        }
        annualCompanyCb.enableEvents(false);
        annualCompanyCb.setValue(check);
        annualCompanyCb.enableEvents(true);
    }

    public void layout() {
        annualStationNamePanel.layout();
        normalStationNamePanel.layout();
        annualButtonPanel.layout();
        normalButtonPanel.layout();
        horizontalPanel.layout(true);
    }

    public CheckBox getAnnualCompanyCb() {
        return annualCompanyCb;
    }
}
