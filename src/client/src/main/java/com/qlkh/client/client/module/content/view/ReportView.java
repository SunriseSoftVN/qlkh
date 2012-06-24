/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
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
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.ReportConstant;
import com.qlkh.client.client.module.content.view.security.ReportSecurity;
import com.qlkh.core.client.constant.ReportFormEnum;
import com.qlkh.core.client.constant.ReportTypeEnum;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.FieldSecurity;
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

    @FieldSecurity
    ComboBox<BeanModel> cbbReportStation = new ComboBox<BeanModel>();

    @FieldSecurity
    ComboBox<BeanModel> cbbReportBranch = new ComboBox<BeanModel>();

    SimpleComboBox<Integer> cbbYear = new SimpleComboBox<Integer>();

    SimpleComboBox<ReportFormEnum> cbbReportForm = new SimpleComboBox<ReportFormEnum>();

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

        cbbReportBranch.setDisplayField(BranchManagerView.BRANCH_NAME_COLUMN);
        cbbReportBranch.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbReportBranch.setEditable(false);
        //Set empty store for waiting for loading data for the sever.
        cbbReportBranch.setStore(new ListStore<BeanModel>());

        cbbReportType.add(Arrays.asList(ReportTypeEnum.values()));
        cbbReportType.setSimpleValue(ReportTypeEnum.CA_NAM);
        cbbReportType.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbReportType.setEditable(false);
        cbbReportType.setWidth(100);

        for (int i = 2012; i < 2100; i++) {
            cbbYear.add(i);
        }
        cbbYear.setTriggerAction(ComboBox.TriggerAction.ALL);
        //TODO remove @dungvn3000
        cbbYear.setSimpleValue(2012);
        cbbYear.setWidth(60);
        cbbYear.setEditable(false);

        cbbReportForm.add(Arrays.asList(ReportFormEnum.values()));
        cbbReportForm.setSimpleValue(ReportFormEnum.MAU_1);
        cbbReportForm.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbReportForm.setEditable(false);
        cbbReportForm.setWidth(100);

        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(4);
        hp.add(cbbReportStation);
        hp.add(cbbReportBranch);
        hp.add(cbbReportType);
        hp.add(cbbYear);
        hp.add(cbbReportForm);
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
        cbbReportForm.setEnabled(enable);
        cbbReportBranch.setEnabled(enable);
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

    public ComboBox<BeanModel> getCbbReportBranch() {
        return cbbReportBranch;
    }

    public SimpleComboBox<ReportFormEnum> getCbbReportForm() {
        return cbbReportForm;
    }
}
