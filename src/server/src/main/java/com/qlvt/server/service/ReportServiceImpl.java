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

package com.qlvt.server.service;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import com.google.inject.Singleton;
import com.qlvt.client.client.service.ReportService;
import com.qlvt.core.client.constant.ReportFileTypeEnum;
import com.qlvt.core.system.SystemUtil;
import com.qlvt.server.service.core.AbstractService;
import com.qlvt.server.servlet.ReportServlet;
import com.qlvt.server.util.ReportExporter;
import com.qlvt.server.util.ServletUtils;
import net.sf.jasperreports.engine.*;

import java.io.FileNotFoundException;

/**
 * The Class ReportServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 2/22/12, 12:45 PM
 */
@Singleton
public class ReportServiceImpl extends AbstractService implements ReportService {

    private static final String REPORT_SERVLET_URI = "/report?";
    private static final String REPORT_FILE_NAME = "report1";

    @Override
    public String reportForCompany(ReportFileTypeEnum fileTypeEnum) {
        try {
            FastReportBuilder fastReportBuilder = new FastReportBuilder();
            fastReportBuilder.addColumn("Name", "name", String.class.getName(), 50);
            fastReportBuilder.addColumn("Age", "age", String.class.getName(), 30);
            fastReportBuilder.addColumn("Email", "email", String.class.getName(), 200);
            fastReportBuilder.setUseFullPageWidth(true);
            fastReportBuilder.setPrintBackgroundOnOddRows(true);
            DynamicReport dynamicReport = fastReportBuilder.build();

            JasperReport jasperReport = DynamicJasperHelper.
                    generateJasperReport(dynamicReport, new ClassicLayoutManager(), null);

            JasperPrint jasperPrint = JasperFillManager.
                    fillReport(jasperReport, null, new JREmptyDataSource(10));

            String filePath = ServletUtils.getInstance().
                    getRealPath(ReportServlet.REPORT_DIRECTORY, REPORT_FILE_NAME
                            + fileTypeEnum.getFileExt());

            if (fileTypeEnum == ReportFileTypeEnum.PDF) {
                ReportExporter.exportReport(jasperPrint, filePath);
            } else if (fileTypeEnum == ReportFileTypeEnum.EXCEL) {
                ReportExporter.exportReportXls(jasperPrint, filePath);
            }

            return new StringBuilder().append(ServletUtils.getInstance().
                    getBaseUrl(getThreadLocalRequest())).
                    append(SystemUtil.getConfiguration().serverServletRootPath()).
                    append(REPORT_SERVLET_URI).
                    append(ReportServlet.REPORT_FILENAME).
                    append("=").
                    append(REPORT_FILE_NAME).
                    append(fileTypeEnum.getFileExt()).toString();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
