/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
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
    FormPanel dkLockPanel = new FormPanel();

    @I18nField
    FormPanel kdkLockPanel = new FormPanel();

    @I18nField
    FormPanel namLockPanel = new FormPanel();

    private HorizontalPanel horizontalPanel = new HorizontalPanel();
    private VerticalPanel dkStationNamePanel = new VerticalPanel();
    private VerticalPanel dkButtonPanel = new VerticalPanel();
    private VerticalPanel kdkStationNamePanel = new VerticalPanel();
    private VerticalPanel kdkButtonPanel = new VerticalPanel();
    private VerticalPanel namStationNamePanel = new VerticalPanel();
    private VerticalPanel namButtonPanel = new VerticalPanel();
    private ContentPanel contentPanel = new ContentPanel();

    private List<CheckBox> dkCbs = new ArrayList<CheckBox>();
    private List<CheckBox> kdkQ1Cbs = new ArrayList<CheckBox>();
    private List<CheckBox> kdkQ2Cbs = new ArrayList<CheckBox>();
    private List<CheckBox> kdkQ3Cbs = new ArrayList<CheckBox>();
    private List<CheckBox> kdkQ4Cbs = new ArrayList<CheckBox>();
    private List<CheckBox> namQ1Cbs = new ArrayList<CheckBox>();
    private List<CheckBox> namQ2Cbs = new ArrayList<CheckBox>();
    private List<CheckBox> namQ3Cbs = new ArrayList<CheckBox>();
    private List<CheckBox> namQ4Cbs = new ArrayList<CheckBox>();

    public CheckBox dkCompanyCb;
    public CheckBox kdkQ1CompanyCb;
    public CheckBox kdkQ2CompanyCb;
    public CheckBox kdkQ3CompanyCb;
    public CheckBox kdkQ4CompanyCb;
    public CheckBox namQ1CompanyCb;
    public CheckBox namQ2CompanyCb;
    public CheckBox namQ3CompanyCb;
    public CheckBox namQ4CompanyCb;

    @Override
    protected void initializeView() {
        dkLockPanel.setFrame(true);
        dkLockPanel.setAutoHeight(true);
        dkLockPanel.setAutoWidth(true);
        HorizontalPanel hp1 = new HorizontalPanel();
        hp1.setSpacing(7);
        hp1.add(dkStationNamePanel);
        hp1.add(dkButtonPanel);
        dkLockPanel.add(hp1);

        namLockPanel.setFrame(true);
        namLockPanel.setAutoWidth(true);
        namLockPanel.setAutoHeight(true);
        HorizontalPanel hp3 = new HorizontalPanel();
        hp3.setSpacing(7);
        hp3.add(namStationNamePanel);
        hp3.add(namButtonPanel);
        namLockPanel.add(hp3);

        kdkLockPanel.setFrame(true);
        kdkLockPanel.setAutoWidth(true);
        kdkLockPanel.setAutoHeight(true);
        HorizontalPanel hp2 = new HorizontalPanel();
        hp2.setSpacing(7);
        hp2.add(kdkStationNamePanel);
        hp2.add(kdkButtonPanel);
        kdkLockPanel.add(hp2);

        horizontalPanel.setAutoWidth(true);
        horizontalPanel.setSpacing(5);
        horizontalPanel.add(dkLockPanel);
        horizontalPanel.add(namLockPanel);
        horizontalPanel.add(kdkLockPanel);
        contentPanel.add(horizontalPanel);

        contentPanel.setHeaderVisible(false);
        contentPanel.setFrame(true);
        contentPanel.setHeight(Window.getClientHeight() - 90);
        contentPanel.setLayout(new RowLayout(Style.Orientation.HORIZONTAL));
        setWidget(contentPanel);
    }

    public void addStationName(String stationName) {
        dkStationNamePanel.add(new Label(stationName));
        kdkStationNamePanel.add(new Label(stationName));
        namStationNamePanel.add(new Label(stationName));
    }

    public void addDKButton(CheckBox checkBox, boolean isCompanyCheckBox) {
        dkButtonPanel.add(checkBox);
        if (!isCompanyCheckBox) {
            dkCbs.add(checkBox);
        } else {
            dkCompanyCb = checkBox;
        }
    }

    public void addNamButton(CheckBox cbQ1, CheckBox cbQ2, CheckBox cbQ3, CheckBox cbQ4,
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
        namButtonPanel.add(hp);
        if (!isCompany) {
            namQ1Cbs.add(cbQ1);
            namQ2Cbs.add(cbQ2);
            namQ3Cbs.add(cbQ3);
            namQ4Cbs.add(cbQ4);
        } else {
            namQ1CompanyCb = cbQ1;
            namQ2CompanyCb = cbQ2;
            namQ3CompanyCb = cbQ3;
            namQ4CompanyCb = cbQ4;
        }
    }

    public void addKDKButton(CheckBox cbQ1, CheckBox cbQ2, CheckBox cbQ3, CheckBox cbQ4,
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
        kdkButtonPanel.add(hp);
        if (!isCompany) {
            kdkQ1Cbs.add(cbQ1);
            kdkQ2Cbs.add(cbQ2);
            kdkQ3Cbs.add(cbQ3);
            kdkQ4Cbs.add(cbQ4);
        } else {
            kdkQ1CompanyCb = cbQ1;
            kdkQ2CompanyCb = cbQ2;
            kdkQ3CompanyCb = cbQ3;
            kdkQ4CompanyCb = cbQ4;
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
        dkStationNamePanel.layout();
        kdkStationNamePanel.layout();
        dkButtonPanel.layout();
        kdkButtonPanel.layout();
        horizontalPanel.layout(true);
    }

    public CheckBox getDkCompanyCb() {
        return dkCompanyCb;
    }

    public List<CheckBox> getDkCbs() {
        return dkCbs;
    }

    public List<CheckBox> getKdkQ1Cbs() {
        return kdkQ1Cbs;
    }

    public List<CheckBox> getKdkQ2Cbs() {
        return kdkQ2Cbs;
    }

    public List<CheckBox> getKdkQ3Cbs() {
        return kdkQ3Cbs;
    }

    public List<CheckBox> getKdkQ4Cbs() {
        return kdkQ4Cbs;
    }

    public CheckBox getKdkQ1CompanyCb() {
        return kdkQ1CompanyCb;
    }

    public CheckBox getKdkQ2CompanyCb() {
        return kdkQ2CompanyCb;
    }

    public CheckBox getKdkQ3CompanyCb() {
        return kdkQ3CompanyCb;
    }

    public CheckBox getKdkQ4CompanyCb() {
        return kdkQ4CompanyCb;
    }

    public List<CheckBox> getNamQ1Cbs() {
        return namQ1Cbs;
    }

    public List<CheckBox> getNamQ2Cbs() {
        return namQ2Cbs;
    }

    public List<CheckBox> getNamQ3Cbs() {
        return namQ3Cbs;
    }

    public List<CheckBox> getNamQ4Cbs() {
        return namQ4Cbs;
    }

    public CheckBox getNamQ1CompanyCb() {
        return namQ1CompanyCb;
    }

    public CheckBox getNamQ2CompanyCb() {
        return namQ2CompanyCb;
    }

    public CheckBox getNamQ3CompanyCb() {
        return namQ3CompanyCb;
    }

    public CheckBox getNamQ4CompanyCb() {
        return namQ4CompanyCb;
    }
}
