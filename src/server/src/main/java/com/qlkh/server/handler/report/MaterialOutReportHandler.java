package com.qlkh.server.handler.report;

import com.qlkh.core.client.action.report.MaterialOutReportAction;
import com.qlkh.core.client.action.report.MaterialOutReportResult;
import com.qlkh.core.client.model.MaterialIn;
import com.qlkh.core.client.report.MaterialReportBean;
import com.qlkh.core.configuration.ConfigurationServerUtil;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.servlet.ReportServlet;
import com.qlkh.server.util.DateTimeUtils;
import com.qlkh.server.util.MoneyConverter;
import com.qlkh.server.util.ReportExporter;
import com.qlkh.server.util.ServletUtils;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;

/**
 * The Class MaterialInReportHandler.
 *
 * @author Nguyen Duc Dung
 * @since 5/24/13 11:43 AM
 */
public class MaterialOutReportHandler extends AbstractHandler<MaterialOutReportAction, MaterialOutReportResult> {

    public static final String JASPER_REPORT_FILE_NAME = "phieuxuatkho.jasper";
    public static final String EXCEL_FILE_NAME = "phieuxuatkho.xls";

    private static final String REPORT_SERVLET_URI = "/report?";

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private SqlQueryDao sqlQueryDao;

    @Override
    public Class<MaterialOutReportAction> getActionType() {
        return MaterialOutReportAction.class;
    }

    @Override
    public MaterialOutReportResult execute(MaterialOutReportAction action, ExecutionContext context) throws DispatchException {
        String reportFilePath = ServletUtils.getInstance().getRealPath(ReportServlet.REPORT_DIRECTORY, JASPER_REPORT_FILE_NAME);
        String xlsFilePath = ServletUtils.getInstance().getRealPath(ReportServlet.REPORT_DIRECTORY, EXCEL_FILE_NAME);
        try {
            List<MaterialReportBean> materialReportBeans = sqlQueryDao.getMaterialOut(action.getRegex());
            if (CollectionUtils.isNotEmpty(materialReportBeans)) {
                MaterialReportBean materialReportBean = materialReportBeans.get(0);
                MaterialIn materialIn = generalDao.findById(MaterialIn.class, materialReportBean.getMaterialId());
                Double totalMoney = sum(materialReportBeans, on(MaterialReportBean.class).getMoney());
                if (materialIn != null) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("stationName", materialIn.getStation().getName());
                    data.put("reason", materialIn.getMaterialGroup().getName());
                    data.put("personName", materialIn.getMaterialPerson().getPersonName());
                    data.put("code", materialIn.getCode());
                    data.put("date", DateTimeUtils.dateTimeInVietnamese());
                    data.put("totalMoneyString", MoneyConverter.transformNumber(String.valueOf(totalMoney.intValue())));
                    JasperPrint jasperPrint = JasperFillManager.fillReport(reportFilePath, data,
                            new JRBeanCollectionDataSource(materialReportBeans));
                    ReportExporter.exportReportXls(jasperPrint, xlsFilePath);
                }
            }
        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String downloadUrl = new StringBuilder().append(ConfigurationServerUtil.getServerBaseUrl())
                .append(ConfigurationServerUtil.getConfiguration().serverServletRootPath())
                .append(REPORT_SERVLET_URI)
                .append(ReportServlet.REPORT_FILENAME_PARAMETER)
                .append("=")
                .append(EXCEL_FILE_NAME).toString();


        return new MaterialOutReportResult(downloadUrl);
    }
}
