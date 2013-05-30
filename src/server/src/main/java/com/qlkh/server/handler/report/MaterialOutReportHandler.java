package com.qlkh.server.handler.report;

import com.qlkh.core.client.action.report.MaterialOutReportAction;
import com.qlkh.core.client.action.report.MaterialOutReportResult;
import com.qlkh.core.client.report.MaterialReportBean;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.servlet.ReportServlet;
import com.qlkh.server.util.*;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jxls.util.Util;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class MaterialInReportHandler.
 *
 * @author Nguyen Duc Dung
 * @since 5/24/13 11:43 AM
 */
public class MaterialOutReportHandler extends AbstractHandler<MaterialOutReportAction, MaterialOutReportResult> {

    private static final String JASPER_REPORT_FILE_NAME = "phieuxuatkho.jasper";
    private static final String OUTPUT_FILE_NAME = "phieuxuatkho";
    private static final String OUTPUT_FILE_EXT = ".xls";
    private static final String REPORT_SERVLET_URI = "/report/";

    @Autowired
    private SqlQueryDao sqlQueryDao;

    @Override
    public Class<MaterialOutReportAction> getActionType() {
        return MaterialOutReportAction.class;
    }

    @Override
    public MaterialOutReportResult execute(MaterialOutReportAction action, ExecutionContext context) throws DispatchException {
        String reportFilePath = ServletUtils.getInstance().getRealPath(ReportServlet.REPORT_DIRECTORY, JASPER_REPORT_FILE_NAME);
        String htmlFilePath = ServletUtils.getInstance().getRealPath(ReportServlet.REPORT_DIRECTORY, OUTPUT_FILE_NAME);
        List<String> ouputs = new ArrayList<String>();
        try {
            List<MaterialReportBean> materialReportBeans = sqlQueryDao.getMaterialOut(action.getRegex());
            if (CollectionUtils.isNotEmpty(materialReportBeans)) {
                for (MaterialReportBean materialReportBean : materialReportBeans) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("stationName", materialReportBean.getStationName());
                    data.put("reason", materialReportBean.getReason());
                    data.put("personName", materialReportBean.getPersonName());
                    data.put("code", materialReportBean.getCode());
                    data.put("date", DateTimeUtils.dateTimeInVietnamese());
                    data.put("totalMoneyString", MoneyConverter
                            .transformNumber(String.valueOf(materialReportBean.getMoney().intValue())));

                    List<MaterialReportBean> beans = new ArrayList<MaterialReportBean>();
                    beans.add(materialReportBean);
                    for (int i = 1; i <= 3; i++) {
                        data.put("reportName", "Liên " + i);
                        String fileOutputPath = htmlFilePath + materialReportBean.getCode() + i + OUTPUT_FILE_EXT;
                        JasperPrint jasperPrint = JasperFillManager.fillReport(reportFilePath, data,
                                new JRBeanCollectionDataSource(beans));
                        ReportExporter.exportReportXls(jasperPrint, fileOutputPath);
                        ouputs.add(fileOutputPath);
                    }
                }
            }
        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            HSSFWorkbook reportWorkbook = new HSSFWorkbook();
            for (String output : ouputs) {
                File file = new File(output);
                FileInputStream input = new FileInputStream(file);
                HSSFWorkbook tempWorkbook = new HSSFWorkbook(input);
                if (tempWorkbook.getNumberOfSheets() > 0) {
                    HSSFSheet copySheet = tempWorkbook.getSheetAt(0);
                    HSSFSheet newSheet = reportWorkbook.createSheet();
                    ExcelUtil.copySheets(newSheet, copySheet);
                }
                input.close();
                FileSystemUtils.deleteRecursively(file);
            }
            Util.writeToFile(htmlFilePath + OUTPUT_FILE_EXT, reportWorkbook);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MaterialOutReportResult(ouputs);
    }
}
