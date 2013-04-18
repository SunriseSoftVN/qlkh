/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.*;
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

    SimpleComboBox<ReportTypeEnum> cbbTaskReportType = new SimpleComboBox<ReportTypeEnum>();

    @FieldSecurity
    ComboBox<BeanModel> cbbTaskReportStation = new ComboBox<BeanModel>();

    @FieldSecurity
    ComboBox<BeanModel> cbbTaskReportBranch = new ComboBox<BeanModel>();

    SimpleComboBox<Integer> cbbTaskYear = new SimpleComboBox<Integer>();

    SimpleComboBox<ReportFormEnum> cbbTaskReportForm = new SimpleComboBox<ReportFormEnum>();

    @FieldSecurity
    ComboBox<BeanModel> cbbPriceReportStation = new ComboBox<BeanModel>();

    @FieldSecurity
    ComboBox<BeanModel> cbbPriceReportBranch = new ComboBox<BeanModel>();

    SimpleComboBox<Integer> cbbPriceYear = new SimpleComboBox<Integer>();

    SimpleComboBox<ReportTypeEnum> cbbPriceReportType = new SimpleComboBox<ReportTypeEnum>();

    @I18nField
    Button btnPlanReportPdf = new Button();

    @I18nField
    Button btnPlanReportXls = new Button();

    @I18nField
    Button btnPriceReportPdf = new Button();

    @I18nField
    Button btnPriceReportXls = new Button();

    @I18nField
    Button btnReportCancel = new Button();

    @I18nField
    FormPanel planReportPanel = new FormPanel();

    @I18nField
    FormPanel priceReportPanel = new FormPanel();

    private ContentPanel contentPanel = new ContentPanel();

    @Override
    protected void initializeView() {
        planReportPanel.setFrame(true);

        cbbTaskReportStation.setDisplayField(StationManagerView.STATION_NAME_COLUMN);
        cbbTaskReportStation.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbTaskReportStation.setEditable(false);

        cbbTaskReportBranch.setDisplayField(BranchManagerView.BRANCH_NAME_COLUMN);
        cbbTaskReportBranch.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbTaskReportBranch.setEditable(false);
        //Set empty store for waiting for loading data for the sever.
        cbbTaskReportBranch.setStore(new ListStore<BeanModel>());

        cbbPriceReportBranch.setDisplayField(BranchManagerView.BRANCH_NAME_COLUMN);
        cbbPriceReportBranch.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbPriceReportBranch.setEditable(false);
        //Set empty store for waiting for loading data for the sever.
        cbbPriceReportBranch.setStore(new ListStore<BeanModel>());

        cbbTaskReportType.add(Arrays.asList(ReportTypeEnum.values()));
        cbbTaskReportType.setSimpleValue(ReportTypeEnum.CA_NAM);
        cbbTaskReportType.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbTaskReportType.setEditable(false);
        cbbTaskReportType.setWidth(100);

        cbbPriceReportType.add(ReportTypeEnum.Q1);
        cbbPriceReportType.add(ReportTypeEnum.Q2);
        cbbPriceReportType.add(ReportTypeEnum.Q3);
        cbbPriceReportType.add(ReportTypeEnum.Q4);
        cbbPriceReportType.setSimpleValue(ReportTypeEnum.Q1);
        cbbPriceReportType.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbPriceReportType.setEditable(false);
        cbbPriceReportType.setWidth(100);

        for (int i = 2012; i < 2100; i++) {
            cbbTaskYear.add(i);
            cbbPriceYear.add(i);
        }
        cbbTaskYear.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbTaskYear.setWidth(60);
        cbbTaskYear.setEditable(false);

        cbbPriceYear.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbPriceYear.setWidth(60);
        cbbPriceYear.setEditable(false);

        cbbTaskReportForm.add(Arrays.asList(ReportFormEnum.values()));
        cbbTaskReportForm.setSimpleValue(ReportFormEnum.MAU_1);
        cbbTaskReportForm.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbTaskReportForm.setEditable(false);
        cbbTaskReportForm.setWidth(100);

        HorizontalPanel hp1 = new HorizontalPanel();
        hp1.setSpacing(4);
        hp1.add(cbbTaskReportStation);
        hp1.add(cbbTaskReportBranch);
        hp1.add(cbbTaskReportType);
        hp1.add(cbbTaskYear);
        hp1.add(cbbTaskReportForm);
        hp1.add(btnPlanReportPdf);
        hp1.add(btnPlanReportXls);
        planReportPanel.add(hp1);

        priceReportPanel.setFrame(true);

        cbbPriceReportStation.setDisplayField(StationManagerView.STATION_NAME_COLUMN);
        cbbPriceReportStation.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbPriceReportStation.setEditable(false);

        HorizontalPanel hp2 = new HorizontalPanel();
        hp2.setSpacing(4);
        hp2.add(cbbPriceReportStation);
        hp2.add(cbbPriceReportBranch);
        hp2.add(cbbPriceReportType);
        hp2.add(cbbPriceYear);
        hp2.add(btnPriceReportPdf);
        hp2.add(btnPriceReportXls);

        priceReportPanel.add(hp2);

        VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(10);
        vp.add(planReportPanel);
        vp.add(priceReportPanel);
        contentPanel.add(vp);

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

    public void setEnableTaskReportButton(boolean enable) {
        cbbTaskReportStation.setEnabled(enable);
        cbbTaskReportForm.setEnabled(enable);
        cbbTaskReportBranch.setEnabled(enable);
        cbbTaskYear.setEnabled(enable);
        cbbTaskReportType.setEnabled(enable);
        btnPlanReportPdf.setEnabled(enable);
        btnPlanReportXls.setEnabled(enable);
    }

    public void setEnablePriceReportButton(boolean enable) {
        cbbPriceReportStation.setEnabled(enable);
        cbbPriceReportBranch.setEnabled(enable);
        cbbPriceYear.setEnabled(enable);
        cbbPriceReportType.setEnabled(enable);
        btnPriceReportPdf.setEnabled(enable);
        btnPriceReportXls.setEnabled(enable);
    }

    public SimpleComboBox<ReportTypeEnum> getCbbTaskReportType() {
        return cbbTaskReportType;
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

    public ComboBox<BeanModel> getCbbTaskReportStation() {
        return cbbTaskReportStation;
    }

    public SimpleComboBox<Integer> getCbbTaskYear() {
        return cbbTaskYear;
    }

    public ComboBox<BeanModel> getCbbTaskReportBranch() {
        return cbbTaskReportBranch;
    }

    public SimpleComboBox<ReportFormEnum> getCbbTaskReportForm() {
        return cbbTaskReportForm;
    }

    public ComboBox<BeanModel> getCbbPriceReportStation() {
        return cbbPriceReportStation;
    }

    public SimpleComboBox<ReportTypeEnum> getCbbPriceReportType() {
        return cbbPriceReportType;
    }

    public SimpleComboBox<Integer> getCbbPriceYear() {
        return cbbPriceYear;
    }

    public ComboBox<BeanModel> getCbbPriceReportBranch() {
        return cbbPriceReportBranch;
    }

    public Label getLblDownload() {
        return lblDownload;
    }

    public Button getBtnPriceReportPdf() {
        return btnPriceReportPdf;
    }

    public Button getBtnPriceReportXls() {
        return btnPriceReportXls;
    }

    public FormPanel getPlanReportPanel() {
        return planReportPanel;
    }

    public FormPanel getPriceReportPanel() {
        return priceReportPanel;
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }
}
