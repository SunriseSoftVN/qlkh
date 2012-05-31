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
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Window;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.view.i18n.ReportConstant;
import com.qlvt.client.client.module.content.view.security.ReportSecurity;
import com.qlvt.core.client.constant.ReportTypeEnum;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.Arrays;

/**
 * The Class ReportView.
 *
 * @author Nguyen Duc Dung
 * @since 2/19/12, 3:46 PM
 */
@ViewSecurity(configuratorClass = ReportSecurity.class)
@View(constantsClass = ReportConstant.class, parentDomId = DomIdConstant.CONTENT_PANEL)
public class ReportView extends AbstractView<ReportConstant> {

    @I18nField
    Label lblDownload = new Label();

    @I18nField
    SimpleComboBox<ReportTypeEnum> cbbReportType = new SimpleComboBox<ReportTypeEnum>();

    @I18nField
    ComboBox<BeanModel> cbbReportStation = new ComboBox<BeanModel>();

    SimpleComboBox<Integer> cbbYear = new SimpleComboBox<Integer>();

    @I18nField
    Button btnPlanReportPdf = new Button();

    @I18nField
    Button btnPlanReportXls = new Button();

    @I18nField
    Button btnReportCancel = new Button();

    @I18nField
    FormPanel planReportPanel = new FormPanel();

    private ContentPanel contentPanel = new ContentPanel();

    @Override
    protected void initializeView() {
        planReportPanel.setFrame(true);

        cbbReportStation.setDisplayField(StationManagerView.STATION_NAME_COLUMN);
        cbbReportStation.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbReportStation.setEditable(false);

        cbbReportType.add(Arrays.asList(ReportTypeEnum.values()));
        cbbReportType.setSimpleValue(ReportTypeEnum.CA_NAM);
        cbbReportType.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbReportType.setEditable(false);
        cbbReportType.setWidth(100);

        for (int i = 2012; i < 2100; i++) {
            cbbYear.add(i);
        }
        cbbYear.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbYear.setSimpleValue(2012);
        cbbYear.setWidth(60);

        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(4);
        hp.add(cbbReportStation);
        hp.add(cbbReportType);
        hp.add(cbbYear);
        hp.add(btnPlanReportPdf);
        hp.add(btnPlanReportXls);
        planReportPanel.add(hp);

        contentPanel.add(planReportPanel);
        contentPanel.setHeaderVisible(false);
        contentPanel.setFrame(true);
        contentPanel.setHeight(Window.getClientHeight() - 90);
        contentPanel.setLayout(new RowLayout(Style.Orientation.HORIZONTAL));
        setWidget(contentPanel);
    }

    public com.extjs.gxt.ui.client.widget.Window createReportWindow(String url) {
        com.extjs.gxt.ui.client.widget.Window window = new com.extjs.gxt.ui.client.widget.Window();
        window.add(lblDownload);
        window.getButtonBar().add(new Html("<a href='" + url + "'><b>Download</b></a>"));
        window.addButton(btnReportCancel);
        window.setSize(380, 50);
        window.setResizable(false);
        window.setModal(true);
        window.setHeading(getConstant().reportWindow());
        return window;
    }

    public void setEnableReportButton(boolean enable) {
        cbbReportStation.setEnabled(enable);
        cbbYear.setEnabled(enable);
        cbbReportType.setEnabled(enable);
        btnPlanReportPdf.setEnabled(enable);
        btnPlanReportXls.setEnabled(enable);
    }

    public SimpleComboBox<ReportTypeEnum> getCbbReportType() {
        return cbbReportType;
    }

    public Button getBtnPlanReportPdf() {
        return btnPlanReportPdf;
    }

    public Button getBtnPlanReportXls() {
        return btnPlanReportXls;
    }

    public Button getBtnReportCancel() {
        return btnReportCancel;
    }

    public ComboBox<BeanModel> getCbbReportStation() {
        return cbbReportStation;
    }

    public SimpleComboBox<Integer> getCbbYear() {
        return cbbYear;
    }
}
