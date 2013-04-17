package com.qlkh.server.handler.report;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.*;
import com.qlkh.core.client.action.report.PriceReportAction;
import com.qlkh.core.client.action.report.PriceReportResult;
import com.qlkh.core.client.action.report.TaskReportAction;
import com.qlkh.core.client.constant.ReportFileTypeEnum;
import com.qlkh.core.client.constant.ReportTypeEnum;
import com.qlkh.core.client.model.Branch;
import com.qlkh.core.client.model.Station;
import com.qlkh.core.client.model.view.TaskMaterialDataView;
import com.qlkh.core.client.report.MaterialReportBean;
import com.qlkh.core.client.report.PriceSumReportBean;
import com.qlkh.core.client.report.StationReportBean;
import com.qlkh.core.client.report.TaskSumReportBean;
import com.qlkh.core.configuration.ConfigurationServerUtil;
import com.qlkh.server.business.rule.AdditionStationColumnRule;
import com.qlkh.server.business.rule.PriceReportRule;
import com.qlkh.server.business.rule.StationCodeEnum;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.servlet.ReportServlet;
import com.qlkh.server.util.ReportExporter;
import com.qlkh.server.util.ServletUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.*;
import static com.qlkh.server.business.rule.StationCodeEnum.CAUGIAT;
import static com.qlkh.server.business.rule.StationCodeEnum.COMPANY;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * The Class PriceReportHandler.
 *
 * @author Nguyen Duc Dung
 * @since 4/14/13 11:00 AM
 */
public class PriceReportHandler extends AbstractHandler<PriceReportAction, PriceReportResult> implements ApplicationContextAware {

    private static final String REPORT_SERVLET_URI = "/report?";
    private static final String REPORT_FILE_NAME = "kehoachcungvatu";
    private static final Font DEFAULT_FONT = new Font(8, "Arial", "/fonts/Arial.ttf",
            Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
    private static final Font TITLE_FONT = new Font(14, "Arial", "/fonts/Arial.ttf",
            Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);

    private ApplicationContext applicationContext;

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private SqlQueryDao sqlQueryDao;

    @Override
    public Class<PriceReportAction> getActionType() {
        return PriceReportAction.class;
    }

    @Override
    public PriceReportResult execute(PriceReportAction action, ExecutionContext executionContext) throws DispatchException {
        List<TaskMaterialDataView> dataViews = sqlQueryDao.getTaskMaterial(action.getYear(), action.getReportTypeEnum().getValue());
        List<TaskSumReportBean> taskSumReportBeans = getTaskReportHandler().buildReportData(new TaskReportAction(action));
        List<PriceSumReportBean> priceSumReportBeans = new ArrayList<PriceSumReportBean>();

        //Add default material
        PriceReportRule.addDefault(priceSumReportBeans);

        for (PriceSumReportBean priceSumReportBean : priceSumReportBeans) {
            for (TaskSumReportBean taskSumReportBean : taskSumReportBeans) {
                for (String regex : priceSumReportBean.getMaterial().getRange()) {
                    if (taskSumReportBean.getTask().getCode().matches(regex)) {
                        List<TaskMaterialDataView> taskMaterialDataViews = select(dataViews,
                                having(on(TaskMaterialDataView.class).getTaskId(), equalTo(taskSumReportBean.getTask().getId())));
                        for (TaskMaterialDataView taskMaterialDataView : taskMaterialDataViews) {
                            MaterialReportBean materialReportBean = new MaterialReportBean(taskMaterialDataView);
                            PriceSumReportBean child = new PriceSumReportBean();
                            child.setMaterial(materialReportBean);
                            priceSumReportBean.getChilds().add(child);
                        }
                        if (priceSumReportBean.getStations().isEmpty()) {
                            priceSumReportBean.setStations(taskSumReportBean.getStations());
                        }
                    }
                }
            }
        }

        for (PriceSumReportBean bean1 : priceSumReportBeans) {
            for (PriceSumReportBean bean2 : priceSumReportBeans) {
                for (String regex : bean1.getMaterial().getRange()) {
                    if (bean2.getMaterial().getCode() != null
                            && bean2.getMaterial().getCode().matches(regex)) {
                        bean1.getChilds().add(bean2);
                    }
                }
            }
        }

        forEach(priceSumReportBeans).calculate();

        //only for display
        List<PriceSumReportBean> displayBeans = new ArrayList<PriceSumReportBean>();
        for (PriceSumReportBean priceSumReportBean : priceSumReportBeans) {
            displayBeans.add(priceSumReportBean);
            int stt = 0;
            for (PriceSumReportBean child : priceSumReportBean.getChilds()) {
                if (child.getMaterial().getRange().length == 0) {
                    child.getMaterial().setStt(String.valueOf(stt += 1));
                    displayBeans.add(child);
                }
            }
        }

        //Hide some value
        for (PriceSumReportBean priceSumReportBean : displayBeans) {
            if (!priceSumReportBean.getChilds().isEmpty()) {
                for (StationReportBean station : priceSumReportBean.getStations().values()) {
                    if (station.getId() != StationCodeEnum.COMPANY.getId()
                            && station.getId() != StationCodeEnum.ND_FOR_REPORT.getId()
                            && station.getId() != StationCodeEnum.TN_FOR_REPORT.getId()) {
                        station.setMaterialWeight(null);
                        station.setMaterialPrice(null);
                    }
                }
            }
        }

        return new PriceReportResult(reportForCompany(action, displayBeans));
    }

    private String reportForCompany(PriceReportAction action, List<PriceSumReportBean> priceSumReportBeans) throws ActionException {
        ReportFileTypeEnum fileTypeEnum = action.getFileTypeEnum();
        try {
            DynamicReport dynamicReport = buildReportLayout(action);
            JasperReport jasperReport = DynamicJasperHelper.
                    generateJasperReport(dynamicReport, new ClassicLayoutManager(), null);

            JasperPrint jasperPrint = JasperFillManager.
                    fillReport(jasperReport, null, new JRBeanCollectionDataSource(priceSumReportBeans));

            String fileName = REPORT_FILE_NAME;
            if (action.getBranchId() != null) {
                Branch branch = generalDao.findById(Branch.class, action.getBranchId());
                fileName += "_" + StringUtils.convertNonAscii(branch.getName()).
                        replaceAll(" ", "_").toLowerCase();
            } else {
                Station station = generalDao.findById(Station.class, action.getStationId());
                fileName += "_" + StringUtils.convertNonAscii(station.getName()).
                        replaceAll(" ", "_").toLowerCase();
            }

            fileName += "_" + action.getReportTypeEnum() + "_"
                    + action.getYear() + fileTypeEnum.getFileExt();

            String filePath = ServletUtils.getInstance().getRealPath(ReportServlet.REPORT_DIRECTORY, fileName);

            if (fileTypeEnum == ReportFileTypeEnum.PDF) {
                ReportExporter.exportReport(jasperPrint, filePath);
            } else if (fileTypeEnum == ReportFileTypeEnum.EXCEL) {
                ReportExporter.exportReportXls(jasperPrint, filePath);
            }

            return new StringBuilder().append(ConfigurationServerUtil.getServerBaseUrl())
                    .append(ConfigurationServerUtil.getConfiguration().serverServletRootPath())
                    .append(REPORT_SERVLET_URI)
                    .append(ReportServlet.REPORT_FILENAME_PARAMETER)
                    .append("=")
                    .append(fileName).toString();

        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DynamicReport buildReportLayout(PriceReportAction action) {

        ReportTypeEnum reportTypeEnum = action.getReportTypeEnum();
        ReportFileTypeEnum reportFileTypeEnum = action.getFileTypeEnum();
        long stationId = action.getStationId();
        Long branchId = action.getBranchId();

        FastReportBuilder fastReportBuilder = new FastReportBuilder();

        Style titleStyle = new Style();
        TITLE_FONT.setBold(true);
        titleStyle.setFont(TITLE_FONT);
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        titleStyle.setVerticalAlign(VerticalAlign.MIDDLE);

        Style headerStyle = new Style();
        headerStyle.setFont(DEFAULT_FONT);
        headerStyle.getFont().setBold(true);
        headerStyle.setBorder(Border.THIN);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);

        Style detailStyle = new Style();
        detailStyle.setFont(DEFAULT_FONT);
        detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        detailStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        detailStyle.setBorderLeft(Border.THIN);
        detailStyle.setBorderRight(Border.THIN);
        detailStyle.setBorderBottom(Border.THIN);

        Style numberStyle = new Style();
        numberStyle.setFont(DEFAULT_FONT);
        numberStyle.setBorder(Border.THIN);
        numberStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
        numberStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        numberStyle.setBorderLeft(Border.THIN);
        numberStyle.setBorderRight(Border.THIN);
        numberStyle.setBorderBottom(Border.THIN);

        Style nameStyle = new Style();
        nameStyle.setFont(DEFAULT_FONT);
        nameStyle.setBorder(Border.THIN);
        nameStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        nameStyle.setBorderLeft(Border.THIN);
        nameStyle.setBorderRight(Border.THIN);
        nameStyle.setBorderBottom(Border.THIN);

        try {
            fastReportBuilder.addColumn("TT", "material.stt", String.class, 15, detailStyle)
                    .addColumn("Tên và quy cách vật tư", "material.name", String.class, 80, nameStyle)
                    .addColumn("Đơn vị", "material.unit", String.class, 15, detailStyle)
                    .addColumn("Đơn giá", "material.price", Double.class, 30, false, "###,###.###", detailStyle);
            List<Station> stations = new ArrayList<Station>();
            if (stationId == COMPANY.getId()) {
                stations = generalDao.getAll(Station.class);

                Station company = selectUnique(stations, having(on(Station.class).isCompany(), equalTo(true)));
                //move company to the last
                stations.remove(company);
                stations.add(company);

                //Add two more columns. Business rule. TODO remove @dungvn3000
                AdditionStationColumnRule.addStation(stations);

                fastReportBuilder.setTitle("KẾ HOẠCH CUNG VẬT TƯ SCTX – KCHT TTTH ĐS " + reportTypeEnum.getName()
                        + " NĂM " + action.getYear() + " \\n CÔNG TY TTTH ĐS VINH \\n");
            } else {
                Station station = generalDao.findById(Station.class, stationId);
                stations.add(station);
                if (branchId != null) {
                    Branch branch = generalDao.findById(Branch.class, branchId);
                    fastReportBuilder.setTitle("KẾ HOẠCH SCTX – KCHT THÔNG TIN TÍN HIỆU ĐS "
                            + reportTypeEnum.getName() + " NĂM " + action.getYear() + " \\n"
                            + branch.getName().toUpperCase() + "\\n");
                } else {
                    if (stationId == CAUGIAT.getId()) {
                        //Add two more columns. Business rule. TODO remove @dungvn3000
                        AdditionStationColumnRule.addStation(stations);
                    }
                    fastReportBuilder.setTitle("KẾ HOẠCH SCTX – KCHT THÔNG TIN TÍN HIỆU ĐS "
                            + reportTypeEnum.getName() + " NĂM " + action.getYear() + " \\n"
                            + station.getName().toUpperCase() + "\\n");
                }
            }

            if (CollectionUtils.isNotEmpty(stations)) {
                Map<Integer, String> colSpans = new HashMap<Integer, String>();

                for (int i = 0; i < stations.size(); i++) {
                    Station station = stations.get(i);
                    int index = fastReportBuilder.getColumns().size();
                    //Format number style, remove omit unnecessary '.0'
                    fastReportBuilder.addColumn("KL",
                            "stations." + station.getId() + ".materialWeight", Double.class, 30, true, "###,###.#", numberStyle);
                    //Last 3 columns is different.
                    if (i >= stations.size() - 3) {
                        fastReportBuilder.addColumn("Tiền",
                                "stations." + station.getId() + ".materialPrice", Double.class, 30, false, "###,###.#", numberStyle);
                    }
                    colSpans.put(index, station.getShortName());
                }

                if (stationId == COMPANY.getId()) {
                    for (Integer colIndex : colSpans.keySet()) {
                        if (colIndex < fastReportBuilder.getColumns().size() - 6) {
                            fastReportBuilder.setColspan(colIndex, 1, colSpans.get(colIndex));
                        } else {
                            fastReportBuilder.setColspan(colIndex, 2, colSpans.get(colIndex));
                        }
                    }
                }

                //Add two more column for CAU GIAT station report.
                if (stationId == CAUGIAT.getId() && branchId == null) {
                    for (Integer colIndex : colSpans.keySet()) {
                        fastReportBuilder.setColspan(colIndex, 2, colSpans.get(colIndex));
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        fastReportBuilder.setHeaderHeight(50);
        if (stationId == COMPANY.getId()) {
            fastReportBuilder.setPageSizeAndOrientation(Page.Page_A4_Landscape());
        } else {
            fastReportBuilder.setPageSizeAndOrientation(Page.Page_A4_Portrait());
        }

        fastReportBuilder.setDefaultStyles(titleStyle, null, headerStyle, null);
        fastReportBuilder.setUseFullPageWidth(true);
        fastReportBuilder.setLeftMargin(10);

        if (reportFileTypeEnum == ReportFileTypeEnum.PDF) {
            fastReportBuilder.setPrintBackgroundOnOddRows(true);
        } else {
            fastReportBuilder.setIgnorePagination(true);
        }

        return fastReportBuilder.build();
    }


    private TaskReportHandler getTaskReportHandler() {
        return applicationContext.getBean(TaskReportHandler.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
