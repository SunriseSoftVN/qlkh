/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.servlet;

import com.qlkh.core.client.constant.ReportFileTypeEnum;
import com.qlkh.server.util.ServletUtils;

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
public class ReportServlet extends HttpServlet {

    public static final String REPORT_FILENAME_PARAMETER = "reportName";
    public static final String REPORT_DIRECTORY = "report";

    public static final String RESPONSE_HEADER = "Content-Disposition";
    public static final String RESPONSE_HEADER_CONTENT = "attachment; filename=";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reportFileName = req.getParameter(REPORT_FILENAME_PARAMETER);
        ReportFileTypeEnum fileTypeEnum = ReportFileTypeEnum.getFileType(reportFileName);
        if (fileTypeEnum != null) {
            String st = ServletUtils.getInstance().getRealPath(REPORT_DIRECTORY, reportFileName);
            File file = new File(st);

            byte[] bytes = new byte[(int) file.length()];
            InputStream inputStream = new FileInputStream(file);
            inputStream.read(bytes);

            resp.setContentType(fileTypeEnum.getFileContent());
            resp.setHeader(RESPONSE_HEADER, RESPONSE_HEADER_CONTENT + reportFileName);
            resp.setContentLength(bytes.length);
            resp.getOutputStream().write(bytes, 0, bytes.length);
            resp.getOutputStream().flush();
            resp.getOutputStream().close();
        }
    }
}
