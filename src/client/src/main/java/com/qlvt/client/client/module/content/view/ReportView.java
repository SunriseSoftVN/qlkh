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
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Window;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.view.i18n.ReportConstant;
import com.qlvt.client.client.module.content.view.security.ReportSecurity;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

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
    Button btnPlanReportPdf = new Button();

    @I18nField
    Button btnPlanReportXls = new Button();

    @I18nField
    FormPanel planReportPanel = new FormPanel();

    private ContentPanel contentPanel = new ContentPanel();

    @Override
    protected void initializeView() {
        planReportPanel.setFrame(true);

        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(4);
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

    public Button getBtnPlanReportPdf() {
        return btnPlanReportPdf;
    }

    public Button getBtnPlanReportXls() {
        return btnPlanReportXls;
    }
}
