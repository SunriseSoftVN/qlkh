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

package com.qlvt.server.servlet;

import com.google.inject.Singleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The Class ReportServlet.
 *
 * @author Nguyen Duc Dung
 * @since 2/23/12, 2:33 PM
 */
@Singleton
public class ReportServlet extends HttpServlet {

    public static final String REPORT_FILENAME = "reportName";
    public static final String REPORT_DIRECTORY = "/report/";

    public static final String PDF_CONTENT_TYPE = "application/pdf";
    public static final String RESPONSE_HEADER = "Content-Disposition";
    public static final String RESPONSE_HEADER_CONTENT = "attachment; filename=";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reportFileName = req.getParameter(REPORT_FILENAME);

        String st = getClass().getResource(REPORT_DIRECTORY).getPath() + reportFileName;
        File file = new File(st);

        byte[] bytes = new byte[(int) file.length()];
        InputStream inputStream = new FileInputStream(file);
        inputStream.read(bytes);

        resp.setContentType(PDF_CONTENT_TYPE);
        resp.setHeader(RESPONSE_HEADER, RESPONSE_HEADER_CONTENT + reportFileName);
        resp.setContentLength(bytes.length);
        resp.getOutputStream().write(bytes, 0, bytes.length);
        resp.getOutputStream().flush();
        resp.getOutputStream().close();
    }
}
