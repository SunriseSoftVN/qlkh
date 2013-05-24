package com.qlkh.server.handler.report;

import com.qlkh.core.client.action.report.MaterialInReportAction;
import com.qlkh.core.client.action.report.MaterialInReportResult;
import com.qlkh.core.client.model.Material;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.servlet.ReportServlet;
import com.qlkh.server.util.ReportExporter;
import com.qlkh.server.util.ServletUtils;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * The Class MaterialInReportHandler.
 *
 * @author Nguyen Duc Dung
 * @since 5/24/13 11:43 AM
 */
public class MaterialInReportHandler extends AbstractHandler<MaterialInReportAction, MaterialInReportResult> {

    public static final String JASPER_REPORT_FILE_NAME = "phieuxuatkho.jasper";
    public static final String EXCEL_FILE_NAME = "phieuxuatkho.xls";

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<MaterialInReportAction> getActionType() {
        return MaterialInReportAction.class;
    }

    @Override
    public MaterialInReportResult execute(MaterialInReportAction action, ExecutionContext context) throws DispatchException {
        try {
            String reportFilePath = ServletUtils.getInstance().getRealPath(ReportServlet.REPORT_DIRECTORY, JASPER_REPORT_FILE_NAME);
            String xlsFilePath = ServletUtils.getInstance().getRealPath(ReportServlet.REPORT_DIRECTORY, EXCEL_FILE_NAME);
            List<Material> materials = generalDao.getAll(Material.class);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportFilePath, null, new JRBeanCollectionDataSource(materials));
            ReportExporter.exportReportXls(jasperPrint, xlsFilePath);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
