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

package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.StationLockConstants;
import com.qlkh.client.client.module.content.view.security.StationLockViewSecurity;
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
    private List<CheckBox> q1Cbs = new ArrayList<CheckBox>();
    private List<CheckBox> q2Cbs = new ArrayList<CheckBox>();
    private List<CheckBox> q3Cbs = new ArrayList<CheckBox>();
    private List<CheckBox> q4Cbs = new ArrayList<CheckBox>();
    public CheckBox annualCompanyCb;
    public CheckBox q1CompanyCb;
    public CheckBox q2CompanyCb;
    public CheckBox q3CompanyCb;
    public CheckBox q4CompanyCb;

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

    public void addNormalButton(CheckBox cbQ1, CheckBox cbQ2, CheckBox cbQ3, CheckBox cbQ4,
                                boolean isCompany) {
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
        if(!isCompany) {
            q1Cbs.add(cbQ1);
            q2Cbs.add(cbQ2);
            q3Cbs.add(cbQ3);
            q4Cbs.add(cbQ4);
        } else {
            q1CompanyCb =cbQ1;
            q2CompanyCb =cbQ2;
            q3CompanyCb =cbQ3;
            q4CompanyCb =cbQ4;
        }
    }

    public void checkAllLock(List<CheckBox> checkBoxes, boolean value) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.disableEvents(true);
            checkBox.setValue(value);
            checkBox.disableEvents(false);
        }
    }

    public void enableAllLock(List<CheckBox> checkBoxes, boolean enabled) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setEnabled(enabled);
            checkBox.enableEvents(enabled);
        }
    }

    public void checkCompanyCb(CheckBox cbCompany, List<CheckBox> checkBoxes) {
        boolean check = true;
        for (CheckBox checkBox : checkBoxes) {
            if (!checkBox.getValue()) {
                check = false;
                break;
            }
        }
        cbCompany.enableEvents(false);
        cbCompany.setValue(check);
        cbCompany.enableEvents(true);
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

    public List<CheckBox> getAnnualCbs() {
        return annualCbs;
    }

    public List<CheckBox> getQ1Cbs() {
        return q1Cbs;
    }

    public List<CheckBox> getQ2Cbs() {
        return q2Cbs;
    }

    public List<CheckBox> getQ3Cbs() {
        return q3Cbs;
    }

    public List<CheckBox> getQ4Cbs() {
        return q4Cbs;
    }

    public CheckBox getQ1CompanyCb() {
        return q1CompanyCb;
    }

    public CheckBox getQ2CompanyCb() {
        return q2CompanyCb;
    }

    public CheckBox getQ3CompanyCb() {
        return q3CompanyCb;
    }

    public CheckBox getQ4CompanyCb() {
        return q4CompanyCb;
    }
}
