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

import com.google.inject.Singleton;
import com.qlvt.client.client.service.ReportService;
import com.qlvt.core.system.SystemUtil;
import com.qlvt.server.service.core.AbstractService;
import com.qlvt.server.servlet.ReportServlet;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

import javax.servlet.http.HttpServletRequest;

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
    private static final String PDF_EXT = ".pdf";
    private static final String REPORT_EXT = ".jasper";

    @Override
    public String reportForCompany() {
        try {
            JasperRunManager.runReportToPdfFile(getClass().getResource(ReportServlet.REPORT_DIRECTORY
                    + REPORT_FILE_NAME + REPORT_EXT).getPath(), null, new JREmptyDataSource(50));
            return getBaseUrl(getThreadLocalRequest()) + SystemUtil.getConfiguration().serverServletRootPath()
                    + REPORT_SERVLET_URI + ReportServlet.REPORT_FILENAME + "=" + REPORT_FILE_NAME + PDF_EXT;
        } catch (JRException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getBaseUrl(HttpServletRequest request) {
        if ((request.getServerPort() == 80) ||
                (request.getServerPort() == 443))
            return request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        else
            return request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
