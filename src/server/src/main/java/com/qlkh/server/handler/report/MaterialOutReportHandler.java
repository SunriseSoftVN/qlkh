package com.qlkh.server.handler.report;

import com.qlkh.core.client.action.report.MaterialOutReportAction;
import com.qlkh.core.client.action.report.MaterialOutReportResult;
import com.qlkh.core.client.report.MaterialReportBean;
import com.qlkh.core.configuration.ConfigurationServerUtil;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.servlet.ReportServlet;
import com.qlkh.server.util.DateTimeUtils;
import com.qlkh.server.util.ExcelUtil;
import com.qlkh.server.util.ReportExporter;
import com.qlkh.server.util.ServletUtils;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jxls.util.Util;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * The Class MaterialInReportHandler.
 *
 * @author Nguyen Duc Dung
 * @since 5/24/13 11:43 AM
 */
public class MaterialOutReportHandler extends AbstractHandler<MaterialOutReportAction, MaterialOutReportResult> {

    private static final String REPORT_TEMPLATE_DIRECTORY = "report/template";
    private static final String JASPER_REPORT_FILE_NAME = "phieuxuatkho.jasper";
    private static final String OUTPUT_FILE_NAME = "phieuxuatkho";
    private static final String PRINT_All_FILE_NAME = "printall.xls";
    private static final String PDF_OUTPUT_FILE_EXT = ".pdf";
    private static final String EXCEL_OUTPUT_FILE_EXT = ".xls";

    @Autowired
    private SqlQueryDao sqlQueryDao;

    @Override
    public Class<MaterialOutReportAction> getActionType() {
        return MaterialOutReportAction.class;
    }

    @Override
    public MaterialOutReportResult execute(MaterialOutReportAction action, ExecutionContext context) throws DispatchException {
        String reportFilePath = ServletUtils.getInstance().getRealPath(REPORT_TEMPLATE_DIRECTORY, JASPER_REPORT_FILE_NAME);
        String outputFilePath = ServletUtils.getInstance().getRealPath(ReportServlet.REPORT_DIRECTORY, OUTPUT_FILE_NAME);
        String printAllFilePath = ServletUtils.getInstance().getRealPath(REPORT_TEMPLATE_DIRECTORY, PRINT_All_FILE_NAME);

        List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
        try {
            List<MaterialReportBean> materialReportBeans = sqlQueryDao.getMaterialOut(action.getForm(), action.getTo());
            if (CollectionUtils.isNotEmpty(materialReportBeans)) {
                for (MaterialReportBean materialReportBean : materialReportBeans) {
                    Map<String, Object> data = new HashMap<String, Object>();

                    if (StringUtils.isNotBlank(materialReportBean.getGroupName())) {
                        data.put("stationName", materialReportBean.getGroupName());
                    } else {
                        data.put("stationName", materialReportBean.getStationName());
                    }

                    data.put("reason", materialReportBean.getReason());
                    data.put("personName", materialReportBean.getPersonName());
                    data.put("code", "A" + materialReportBean.getReportCode());
                    data.put("date", DateTimeUtils.dateTimeInVietnamese(materialReportBean.getExportDate()));
//                    data.put("totalMoneyString", MoneyConverter
//                            .transformNumber(String.valueOf(materialReportBean.getMoney().intValue())));
                    data.put(JRParameter.REPORT_LOCALE, new Locale("vi", "VN"));

                    List<MaterialReportBean> beans = new ArrayList<MaterialReportBean>();
                    beans.add(materialReportBean);
                    for (int i = 1; i <= 3; i++) {
                        data.put("reportName", "Liên " + i);
                        JasperPrint jasperPrint = JasperFillManager.fillReport(reportFilePath, data,
                                new JRBeanCollectionDataSource(beans));
                        //fix for utf8
                        jasperPrint.getDefaultStyle().setPdfFontName("/fonts/Arial.ttf");
                        jasperPrints.add(jasperPrint);
                    }
                }

                if (action.isPdf()) {
                    String fileOutputPath = outputFilePath + PDF_OUTPUT_FILE_EXT;
                    ReportExporter.exportReport(jasperPrints, fileOutputPath);
                } else {
                    List<String> temps = new ArrayList<String>();
                    for (JasperPrint jasperPrint : jasperPrints) {
                        UUID id = UUID.randomUUID();
                        String fileOutputPath = outputFilePath + id.toString() + EXCEL_OUTPUT_FILE_EXT;
                        ReportExporter.exportReportXls(jasperPrint, fileOutputPath);
                        temps.add(fileOutputPath);
                    }

                    if (CollectionUtils.isNotEmpty(temps)) {
                        File printAllFile = new File(printAllFilePath);
                        FileInputStream printAllFileInputStream = new FileInputStream(printAllFile);
                        HSSFWorkbook reportWorkbook = new HSSFWorkbook(printAllFileInputStream);
                        for (String output : temps) {
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
                        Util.writeToFile(outputFilePath + EXCEL_OUTPUT_FILE_EXT, reportWorkbook);
                        printAllFileInputStream.close();
                    }
                }
            }
        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String reportUrl = new StringBuilder().append(ConfigurationServerUtil.getServerBaseUrl())
                .append(ConfigurationServerUtil.getConfiguration().serverServletRootPath())
                .append(ReportServlet.REPORT_SERVLET_URI)
                .append(ReportServlet.REPORT_FILENAME_PARAMETER)
                .append("=").toString();


        if (action.isPdf()) {
            reportUrl += OUTPUT_FILE_NAME + PDF_OUTPUT_FILE_EXT;
        } else {
            reportUrl += OUTPUT_FILE_NAME + EXCEL_OUTPUT_FILE_EXT;
        }

        return new MaterialOutReportResult(reportUrl);
    }
}
