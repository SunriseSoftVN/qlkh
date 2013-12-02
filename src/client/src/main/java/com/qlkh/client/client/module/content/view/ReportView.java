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
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.ReportConstant;
import com.qlkh.client.client.module.content.view.security.ReportSecurity;
import com.qlkh.client.client.utils.BrowserUtils;
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

    SimpleComboBox<Integer> cbbMaterialYear = new SimpleComboBox<Integer>();

    SimpleComboBox<ReportTypeEnum> cbbMaterialReportType = new SimpleComboBox<ReportTypeEnum>();

    NumberField txtMaterialFrom = new NumberField();

    NumberField txtMaterialTo = new NumberField();

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
    @FieldSecurity
    FormPanel planReportPanel = new FormPanel();

    @I18nField
    @FieldSecurity
    FormPanel priceReportPanel = new FormPanel();

    @I18nField
    @FieldSecurity
    FormPanel materialReportPanel = new FormPanel();

    @I18nField
    @FieldSecurity
    FormPanel wareHouseReportPanel = new FormPanel();

    @I18nField
    Button btnMaterialReportPdf = new Button();

    @I18nField
    Button btnMaterialReportXls = new Button();

    @I18nField
    Button btnMaterialInReport = new Button();

    @I18nField
    Button btnMaterialInExcelReport = new Button();

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

        cbbMaterialReportType.add(ReportTypeEnum.Q1);
        cbbMaterialReportType.add(ReportTypeEnum.Q2);
        cbbMaterialReportType.add(ReportTypeEnum.Q3);
        cbbMaterialReportType.add(ReportTypeEnum.Q4);
        cbbMaterialReportType.setSimpleValue(ReportTypeEnum.Q1);
        cbbMaterialReportType.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbMaterialReportType.setEditable(false);
        cbbMaterialReportType.setWidth(100);

        for (int i = 2012; i < 2030; i++) {
            cbbTaskYear.add(i);
            cbbPriceYear.add(i);
            cbbMaterialYear.add(i);
        }
        cbbTaskYear.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbTaskYear.setWidth(60);
        cbbTaskYear.setEditable(false);

        cbbPriceYear.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbPriceYear.setWidth(60);
        cbbPriceYear.setEditable(false);

        cbbMaterialYear.setTriggerAction(ComboBox.TriggerAction.ALL);
        cbbMaterialYear.setWidth(60);
        cbbMaterialYear.setEditable(false);

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
        materialReportPanel.setFrame(true);
        wareHouseReportPanel.setFrame(true);

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

        HorizontalPanel hp3 = new HorizontalPanel();
        hp3.setSpacing(4);
        hp3.add(cbbMaterialReportType);
        hp3.add(cbbMaterialYear);
        hp3.add(btnMaterialReportPdf);
        hp3.add(btnMaterialReportXls);

        materialReportPanel.add(hp3);

        btnMaterialInReport.setEnabled(BrowserUtils.is_Java_Enable());

        HorizontalPanel hp4 = new HorizontalPanel();
        hp4.setSpacing(4);
        hp4.add(new Label(getConstant().lblMaterialRegex()));
        hp4.add(txtMaterialFrom);
        hp4.add(txtMaterialTo);
        hp4.add(btnMaterialInReport);
        hp4.add(btnMaterialInExcelReport);

        wareHouseReportPanel.add(hp4);

        VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(10);
        vp.add(planReportPanel);
        vp.add(priceReportPanel);
        vp.add(materialReportPanel);
        vp.add(wareHouseReportPanel);
        contentPanel.add(vp);

        contentPanel.setHeaderVisible(false);
        contentPanel.setFrame(true);
        contentPanel.setHeight(Window.getClientHeight() - 90);
        contentPanel.setLayout(new RowLayout(Style.Orientation.HORIZONTAL));

//        if (UserRoleEnum.WAREHOUSE_MANAGER.getRole().equals(LoginUtils.getRole()) && !contentPanel.isRendered()) {
//            if(BrowserUtils.is_Java_Enable()) {
//                HTML applet = new HTML("<applet id='qz' archive='qzprint/qz-print.jar' name='QZ Print Plugin' code='qz.PrintApplet.class' width='55' height='55'>\n" +
//                        "\t<param name='jnlp_href' value='qzprint/qz-print_jnlp.jnlp'>\n" +
//                        "\t<param name='cache_option' value='plugin'>\n" +
//                        "\t<param name='disable_logging' value='false'>\n" +
//                        "\t<param name='initial_focus' value='false'>\n" +
//                        "</applet>");
//                RootPanel.get().add(applet);
//            } else {
//                vp.add(new Label("Dể sử dụng tính năng in online bạn cần phải download và cài đặt chương trình dưới đây"));
//                vp.add(new Anchor("Download Java 7", "http://visitec.vn:8080/java/jre-7u45-windows-i586.exe"));
//            }
//        }

        setWidget(contentPanel);
    }

    public com.extjs.gxt.ui.client.widget.Window createReportWindow(String url, Boolean openInNewTab) {
        com.extjs.gxt.ui.client.widget.Window window = new com.extjs.gxt.ui.client.widget.Window();
        window.add(lblDownload);
        if (openInNewTab) {
            window.getButtonBar().add(new Html("<a target='_blank' href='" + url + "'><b>Xem online</b></a>"));
        } else {
            window.getButtonBar().add(new Html("<a href='" + url + "'><b>Download</b></a>"));
        }
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

    public void setEnableMaterialReportButton(boolean enable) {
        cbbMaterialYear.setEnabled(enable);
        cbbMaterialReportType.setEnabled(enable);
        btnMaterialReportPdf.setEnabled(enable);
        btnMaterialReportXls.setEnabled(enable);
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

    public SimpleComboBox<Integer> getCbbMaterialYear() {
        return cbbMaterialYear;
    }

    public SimpleComboBox<ReportTypeEnum> getCbbMaterialReportType() {
        return cbbMaterialReportType;
    }

    public Button getBtnMaterialReportPdf() {
        return btnMaterialReportPdf;
    }

    public Button getBtnMaterialReportXls() {
        return btnMaterialReportXls;
    }

    public Button getBtnMaterialInReport() {
        return btnMaterialInReport;
    }

    public NumberField getTxtMaterialFrom() {
        return txtMaterialFrom;
    }

    public NumberField getTxtMaterialTo() {
        return txtMaterialTo;
    }

    public Button getBtnMaterialInExcelReport() {
        return btnMaterialInExcelReport;
    }
}
