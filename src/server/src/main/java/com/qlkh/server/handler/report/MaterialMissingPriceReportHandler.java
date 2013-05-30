package com.qlkh.server.handler.report;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.customexpression.DJSimpleExpression;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.qlkh.core.client.action.report.MaterialMissingPriceReportAction;
import com.qlkh.core.client.action.report.MaterialMissingPriceReportResult;
import com.qlkh.core.client.constant.ReportFileTypeEnum;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.configuration.ConfigurationServerUtil;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.servlet.ReportServlet;
import com.qlkh.server.util.ReportExporter;
import com.qlkh.server.util.ServletUtils;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * The Class MaterialMissingPriceReportHandler.
 *
 * @author Nguyen Duc Dung
 * @since 5/23/13 1:35 PM
 */
public class MaterialMissingPriceReportHandler extends AbstractHandler<MaterialMissingPriceReportAction, MaterialMissingPriceReportResult> {

    private static final String REPORT_FILE_NAME = "baocaovattu";
    private static final Font DEFAULT_FONT = new Font(8, "Arial", "/fonts/Arial.ttf",
            Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
    private static final Font TITLE_FONT = new Font(14, "Arial", "/fonts/Arial.ttf",
            Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);

    @Autowired
    private SqlQueryDao sqlQueryDao;

    @Override
    public Class<MaterialMissingPriceReportAction> getActionType() {
        return MaterialMissingPriceReportAction.class;
    }

    @Override
    public MaterialMissingPriceReportResult execute(MaterialMissingPriceReportAction action, ExecutionContext context) throws DispatchException {
        ReportFileTypeEnum fileTypeEnum = action.getFileTypeEnum();
        try {
            DynamicReport dynamicReport = buildReportLayout(action);
            JasperReport jasperReport = DynamicJasperHelper.
                    generateJasperReport(dynamicReport, new ClassicLayoutManager(), null);

            List<Material> materials = sqlQueryDao.getMaterialsMissingPrice(action.getYear(), action.getQuarter());

            JasperPrint jasperPrint = JasperFillManager.
                    fillReport(jasperReport, null, new JRBeanCollectionDataSource(materials));

            String fileName = REPORT_FILE_NAME + fileTypeEnum.getFileExt();

            String filePath = ServletUtils.getInstance().getRealPath(ReportServlet.REPORT_DIRECTORY, fileName);

            if (fileTypeEnum == ReportFileTypeEnum.PDF) {
                ReportExporter.exportReport(jasperPrint, filePath);
            } else if (fileTypeEnum == ReportFileTypeEnum.EXCEL) {
                ReportExporter.exportReportXls(jasperPrint, filePath);
            }

            String fileUrl = new StringBuilder().append(ConfigurationServerUtil.getServerBaseUrl())
                    .append(ConfigurationServerUtil.getConfiguration().serverServletRootPath())
                    .append(ReportServlet.REPORT_SERVLET_URI)
                    .append(ReportServlet.REPORT_FILENAME_PARAMETER)
                    .append("=")
                    .append(fileName).toString();

            return new MaterialMissingPriceReportResult(fileUrl);

        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new MaterialMissingPriceReportResult();
    }

    private DynamicReport buildReportLayout(MaterialMissingPriceReportAction action) {
        FastReportBuilder fastReportBuilder = new FastReportBuilder();

        Style titleStyle = new Style();
        TITLE_FONT.setBold(true);
        titleStyle.setFont(TITLE_FONT);
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        titleStyle.setVerticalAlign(VerticalAlign.MIDDLE);

        Style headerStyle = new Style();
        headerStyle.setFont(DEFAULT_FONT);
        headerStyle.getFont().setBold(true);
        headerStyle.setBorder(Border.THIN());
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);

        Style detailStyle = new Style();
        detailStyle.setFont(DEFAULT_FONT);
        detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        detailStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        detailStyle.setBorderLeft(Border.THIN());
        detailStyle.setBorderRight(Border.THIN());
        detailStyle.setBorderBottom(Border.THIN());

        Style numberStyle = new Style();
        numberStyle.setFont(DEFAULT_FONT);
        numberStyle.setBorder(Border.THIN());
        numberStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
        numberStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        numberStyle.setBorderLeft(Border.THIN());
        numberStyle.setBorderRight(Border.THIN());
        numberStyle.setBorderBottom(Border.THIN());

        Style nameStyle = new Style();
        nameStyle.setFont(DEFAULT_FONT);
        nameStyle.setBorder(Border.THIN());
        nameStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        nameStyle.setBorderLeft(Border.THIN());
        nameStyle.setBorderRight(Border.THIN());
        nameStyle.setBorderBottom(Border.THIN());

        try {
            AbstractColumn sttColumn = ColumnBuilder.getNew()
                    .setTitle("STT")
                    .setCustomExpression(new DJSimpleExpression(DJSimpleExpression.TYPE_VARIABLE, "COLUMN_COUNT", Integer.class.getName()))
                    .setStyle(detailStyle)
                    .setWidth(30)
                    .setFixedWidth(true)
                    .build();

            fastReportBuilder.addColumn(sttColumn);
            fastReportBuilder.addColumn("Tên và quy cách vật tư", "name", String.class, 80, nameStyle)
                    .addColumn("Mã", "code", String.class, 15, nameStyle);
            fastReportBuilder.setTitle("Báo cáo vật tư chưa khai báo giá \\n"
                    + "quý " + action.getQuarter() + " năm " + action.getYear() + " \\n");
            fastReportBuilder.setDefaultStyles(titleStyle, null, headerStyle, null);
            fastReportBuilder.setUseFullPageWidth(true);
            fastReportBuilder.setLeftMargin(10);

            if (action.getFileTypeEnum() == ReportFileTypeEnum.PDF) {
                fastReportBuilder.setPrintBackgroundOnOddRows(true);
            } else {
                fastReportBuilder.setIgnorePagination(true);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return fastReportBuilder.build();
    }
}
