package com.qlkh.server.handler.report;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import com.qlkh.core.client.action.report.TaskDefaultAction;
import com.qlkh.core.client.action.report.TaskDefaultResult;
import com.qlkh.core.client.constant.ReportFileTypeEnum;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.configuration.ConfigurationServerUtil;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.servlet.ReportServlet;
import com.qlkh.server.util.DateTimeUtils;
import com.qlkh.server.util.ReportExporter;
import com.qlkh.server.util.ServletUtils;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The Class TaskDefaultHandler.
 *
 * @author Nguyen Duc Dung
 * @since 12/8/13 10:00 AM
 */
public class TaskDefaultHandler extends AbstractHandler<TaskDefaultAction, TaskDefaultResult> {

    private static final String REPORT_FILE_NAME = "dinhmuccongviec";
    private static final Font DEFAULT_FONT = new Font(8, "Arial", "/fonts/Arial.ttf",
            Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
    private static final Font TITLE_FONT = new Font(14, "Arial", "/fonts/Arial.ttf",
            Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<TaskDefaultAction> getActionType() {
        return TaskDefaultAction.class;
    }

    @Override
    public TaskDefaultResult execute(TaskDefaultAction action, ExecutionContext context)
            throws DispatchException {
        DynamicReport reportLayout = buildReportLayout(action);
        ReportFileTypeEnum fileTypeEnum = action.getFileTypeEnum();

        try {
            JasperReport jasperReport = DynamicJasperHelper.
                    generateJasperReport(reportLayout, new ClassicLayoutManager(), null);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put(JRParameter.REPORT_LOCALE, new Locale("vi", "VN"));

            JasperPrint jasperPrint = JasperFillManager.
                    fillReport(jasperReport, data, new JRBeanCollectionDataSource(buildReportData()));

            String fileName = REPORT_FILE_NAME + fileTypeEnum.getFileExt();

            String filePath = ServletUtils.getInstance().getRealPath(ReportServlet.REPORT_DIRECTORY, fileName);

            if (fileTypeEnum == ReportFileTypeEnum.PDF) {
                ReportExporter.exportReport(jasperPrint, filePath);
            } else if (fileTypeEnum == ReportFileTypeEnum.EXCEL) {
                ReportExporter.exportReportXls(jasperPrint, filePath);
            }

            String reportUrl = new StringBuilder().append(ConfigurationServerUtil.getServerBaseUrl())
                    .append(ConfigurationServerUtil.getConfiguration().serverServletRootPath())
                    .append(ReportServlet.REPORT_SERVLET_URI)
                    .append(ReportServlet.REPORT_FILENAME_PARAMETER)
                    .append("=")
                    .append(fileName).toString();

            return new TaskDefaultResult(reportUrl);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new TaskDefaultResult();
    }

    private DynamicReport buildReportLayout(TaskDefaultAction action) {
        ReportFileTypeEnum reportFileTypeEnum = action.getFileTypeEnum();

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
        numberStyle.setPaddingRight(5);

        Style nameStyle = new Style();
        nameStyle.setFont(DEFAULT_FONT);
        nameStyle.setBorder(Border.THIN());
        nameStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        nameStyle.setBorderLeft(Border.THIN());
        nameStyle.setBorderRight(Border.THIN());
        nameStyle.setBorderBottom(Border.THIN());

        try {
            fastReportBuilder.addColumn("Mã CV", "code", String.class, 18, detailStyle)
                    .addColumn("Nội dung công việc", "name", String.class, 78, nameStyle)
                    .addColumn("Đơn vị", "unit", String.class, 15, detailStyle)
                    .addColumn("Định mức", "defaultValue", Double.class, 15, false, "###,###.###", detailStyle)
                    .addColumn("Số lần", "quota", Integer.class, 14, detailStyle);
            fastReportBuilder.setTitle("ĐỊNH MỨC CÔNG VIỆC CÔNG TY THÔNG TIN TÍN HIỆU ĐS VINH NĂM " + DateTimeUtils.getCurrentYear());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        fastReportBuilder.setHeaderHeight(50);

        fastReportBuilder.setDefaultStyles(titleStyle, null, headerStyle, null);
        fastReportBuilder.setUseFullPageWidth(true);
        fastReportBuilder.setLeftMargin(10);

        if (reportFileTypeEnum == ReportFileTypeEnum.EXCEL) {
            fastReportBuilder.setIgnorePagination(true);
        }

        return fastReportBuilder.build();
    }

    public List<Task> buildReportData() {
        return generalDao.getAll(Task.class, Order.asc("code"));
    }

}
