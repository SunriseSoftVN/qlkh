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
import com.qlkh.core.client.model.MaterialGroup;
import com.qlkh.core.client.model.Station;
import com.qlkh.core.client.model.view.TaskMaterialDataView;
import com.qlkh.core.client.report.PriceColumnBean;
import com.qlkh.core.client.report.PriceReportBean;
import com.qlkh.core.client.report.StationReportBean;
import com.qlkh.core.client.report.TaskSumReportBean;
import com.qlkh.core.configuration.ConfigurationServerUtil;
import com.qlkh.server.business.rule.AdditionStationColumnRule;
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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.digester.SimpleRegexMatcher;
import org.hibernate.criterion.Order;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.FileNotFoundException;
import java.util.*;

import static ch.lambdaj.Lambda.*;
import static com.qlkh.server.business.rule.StationCodeEnum.*;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * The Class PriceReportHandler.
 *
 * @author Nguyen Duc Dung
 * @since 4/14/13 11:00 AM
 */
public class PriceReportHandler extends AbstractHandler<PriceReportAction, PriceReportResult> implements ApplicationContextAware {

    private static final String REPORT_FILE_NAME = "kehoachcungvattu";
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
        List<PriceReportBean> displayData;
        if (action.getReportTypeEnum() == ReportTypeEnum.CA_NAM) {

            //init structure only no calculator.
            displayData = buildReportData(action);

            for (ReportTypeEnum typeEnum : ReportTypeEnum.values()) {
                if (typeEnum != ReportTypeEnum.CA_NAM) {
                    action.setReportTypeEnum(typeEnum);
                    List<PriceReportBean> data = buildReportData(action);

                    for (PriceReportBean bean1 : displayData) {
                        for (PriceColumnBean column1 : bean1.getColumns().values()) {
                            for (PriceReportBean bean2 : data) {
                                if (bean2.getCode().equals(bean1.getCode())) {
                                    for (PriceColumnBean column2 : bean2.getColumns().values()) {
                                        if (column1.getId() == column2.getId()) {
                                            if (column1.getWeight() == null) {
                                                column1.setWeight(column2.getWeight());
                                            } else if (column1.getWeight() != null && column2.getWeight() != null) {
                                                column1.setWeight(column2.getWeight() + column1.getWeight());
                                            }

                                            if (column1.getPrice() == null) {
                                                column1.setPrice(column2.getPrice());
                                            } else if (column1.getPrice() != null && column2.getPrice() != null) {
                                                column1.setPrice(column2.getPrice() + column1.getPrice());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            action.setReportTypeEnum(ReportTypeEnum.CA_NAM);

            //re caculator the price
            for (PriceReportBean bean1 : displayData) {
                PriceColumnBean companyCol = selectUnique(bean1.getColumns().values(),
                        having(on(PriceColumnBean.class).getId(), equalTo(StationCodeEnum.COMPANY.getId())));

                if (!bean1.isGroup() && companyCol.getPrice() != null && companyCol.getWeight() != null) {
                    double newPrice = companyCol.getPrice() / companyCol.getWeight();
                    newPrice = Math.round(newPrice * 10) / 10;
                    bean1.setPrice(newPrice);
                }
            }
        } else {
            displayData = buildReportData(action);
        }

        return new PriceReportResult(reportForCompany(action, displayData));
    }

    //this method is only for a quarter if you want to do for the whole year, you have to call this method 4 times.
    private List<PriceReportBean> buildReportData(PriceReportAction action) throws ActionException {
        List<TaskMaterialDataView> dataViews;

        if (action.getReportTypeEnum() == ReportTypeEnum.CA_NAM) {
            dataViews = sqlQueryDao.getTaskMaterial(action.getYear());
        } else {
            dataViews = sqlQueryDao.getTaskMaterial(action.getYear(), action.getReportTypeEnum().getValue());
        }

        List<TaskSumReportBean> tasks = getTaskReportHandler().buildReportData(new TaskReportAction(action));
        List<PriceReportBean> prices = new ArrayList<PriceReportBean>();

        //Add material group.
        List<MaterialGroup> materialGroups = generalDao.getAll(MaterialGroup.class, Order.asc("position"));
        for (MaterialGroup materialGroup : materialGroups) {
            String[] regex = materialGroup.getRegexs();
            if (regex != null) {
                PriceReportBean priceReportBean = new PriceReportBean(
                        materialGroup.getCodeDisplay(),
                        materialGroup.getCode(),
                        materialGroup.getName(),
                        regex
                );
                prices.add(priceReportBean);
            }
        }

        buildTree(prices, tasks, dataViews);

        List<Station> stations = new ArrayList<Station>();

        if (action.getStationId() == COMPANY.getId()) {
            stations = generalDao.getAll(Station.class);
        } else {
            Station station = generalDao.findById(Station.class, action.getStationId());
            stations.add(station);
        }

        //Build column.
        for (PriceReportBean price : prices) {
            price.setColumns(buildColumns(stations));
            for (PriceReportBean child : price.getChildren()) {
                if (child.getColumns().isEmpty()) {
                    child.setColumns(buildColumns(stations));
                }
            }
        }

        //Fill Data
        fillData(prices, tasks);
        forEach(prices).calculate();

        List<PriceReportBean> displayData = new ArrayList<PriceReportBean>();
        for (PriceReportBean price : prices) {
            displayData.add(price);
            int index = 0;
            for (PriceReportBean child : price.getChildren()) {
                if (child.getRegex().length == 0) {
                    child.setStt(String.valueOf(index += 1));
                    displayData.add(child);
                }
            }
        }

        if (action.getReportTypeEnum() == ReportTypeEnum.CA_NAM) {
            //Empty data
            for (PriceReportBean bean : displayData) {
                bean.setPrice(null);
                for (PriceColumnBean col : bean.getColumns().values()) {
                    col.setPrice(null);
                    col.setTaskWeight(null);
                    col.setWeight(null);
                }
            }
        }

        //hide some data
        if (action.getStationId() == COMPANY.getId()) {
            for (PriceReportBean price : displayData) {
                if (price.isGroup()) {
                    for (PriceColumnBean column : price.getColumns().values()) {
                        if (column.getId() != COMPANY.getId()
                                && column.getId() != ND_FOR_REPORT.getId()
                                && column.getId() != TN_FOR_REPORT.getId()) {
                            column.setWeight(null);
                            column.setPrice(null);
                        }
                    }
                }
            }
        }
        return displayData;
    }

    private void fillData(List<PriceReportBean> prices, List<TaskSumReportBean> tasks) {
        for (PriceReportBean price : prices) {
            for (PriceReportBean child : price.getChildren()) {
                for (TaskSumReportBean task : tasks) {
                    if (child.getTaskId() == task.getTask().getId()) {
                        for (StationReportBean station : task.getStations().values()) {
                            PriceColumnBean columnBean = child.getColumns().get(String.valueOf(station.getId()));
                            if (columnBean != null) {
                                columnBean.setTaskWeight(station.getValue());
                            }
                        }
                    }
                }
            }
        }
    }

    private Map<String, PriceColumnBean> buildColumns(List<Station> stations) {
        Map<String, PriceColumnBean> columns = new HashMap<String, PriceColumnBean>();
        for (Station station : stations) {
            PriceColumnBean column = new PriceColumnBean();
            column.setId(station.getId());
            columns.put(String.valueOf(column.getId()), column);
        }

        //To more columns
        PriceColumnBean ndColumn = new PriceColumnBean();
        ndColumn.setId(StationCodeEnum.ND_FOR_REPORT.getId());
        columns.put(String.valueOf(ndColumn.getId()), ndColumn);

        PriceColumnBean tnColumn = new PriceColumnBean();
        tnColumn.setId(StationCodeEnum.TN_FOR_REPORT.getId());
        columns.put(String.valueOf(tnColumn.getId()), tnColumn);

        return columns;
    }

    private void buildTree(List<PriceReportBean> prices, List<TaskSumReportBean> tasks, List<TaskMaterialDataView> dataViews) {
        SimpleRegexMatcher matcher = new SimpleRegexMatcher();

        for (PriceReportBean price : prices) {
            for (TaskSumReportBean task : tasks) {
                for (String regex : price.getRegex()) {
                    if (matcher.match(task.getTask().getCode(), regex)) {
                        List<TaskMaterialDataView> materials = select(dataViews,
                                having(on(TaskMaterialDataView.class).getTaskId(), equalTo(task.getTask().getId())));

                        for (TaskMaterialDataView material : materials) {
                            PriceReportBean childPrice = new PriceReportBean();
                            childPrice.setName(material.getName());
                            childPrice.setUnit(material.getUnit());
                            childPrice.setPrice(material.getPrice());
                            childPrice.setQuantity(material.getQuantity());
                            childPrice.setMaterialId(material.getMaterialId());
                            childPrice.setTaskId(material.getTaskId());
                            //code is unique for each bean
                            childPrice.setCode(String.valueOf(material.getMaterialId() + material.getTaskId()));
                            price.getChildren().add(childPrice);
                        }
                    }
                }
            }
        }

        for (PriceReportBean price1 : prices) {
            for (PriceReportBean price2 : prices) {
                for (String regex : price1.getRegex()) {
                    if (price2.getCode() != null
                            && matcher.match(price2.getCode(), regex)
                            && price2 != price1) {
                        price1.getChildren().add(price2);
                    }
                }
            }
        }
    }

    private String reportForCompany(PriceReportAction action, List<PriceReportBean> priceReportBeans) throws ActionException {
        ReportFileTypeEnum fileTypeEnum = action.getFileTypeEnum();
        try {
            DynamicReport dynamicReport = buildReportLayout(action);
            JasperReport jasperReport = DynamicJasperHelper.
                    generateJasperReport(dynamicReport, new ClassicLayoutManager(), null);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put(JRParameter.REPORT_LOCALE, new Locale("vi", "VN"));
            JasperPrint jasperPrint = JasperFillManager.
                    fillReport(jasperReport, data, new JRBeanCollectionDataSource(priceReportBeans));

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
                    .append(ReportServlet.REPORT_SERVLET_URI)
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
            fastReportBuilder.addColumn("TT", "stt", String.class, 15, detailStyle)
                    .addColumn("Tên và quy cách vật tư", "name", String.class, 80, nameStyle)
                    .addColumn("Đơn vị", "unit", String.class, 15, detailStyle)
                    .addColumn("Đơn giá", "price", Double.class, 20, false, "###,###.###", numberStyle);
            List<Station> stations = new ArrayList<Station>();
            if (stationId == COMPANY.getId()) {
                stations = generalDao.getAll(Station.class);

                Station company = selectUnique(stations, having(on(Station.class).isCompany(), equalTo(true)));
                //move company to the last
                stations.remove(company);
                stations.add(company);

                //Add two more columns. Business rule. TODO remove @dungvn3000
                AdditionStationColumnRule.addStation(stations);

                fastReportBuilder.setTitle("KẾ HOẠCH CUNG ỨNG VẬT TƯ SCTX – KCHT TTTH ĐS " + reportTypeEnum.getName()
                        + " NĂM " + action.getYear() + " \\n CÔNG TY TTTH ĐS VINH \\n");
            } else {
                Station station = generalDao.findById(Station.class, stationId);
                stations.add(station);
                if (branchId != null) {
                    Branch branch = generalDao.findById(Branch.class, branchId);
                    fastReportBuilder.setTitle("KẾ HOẠCH CHI PHÍ VẬT TƯ SCTX – KCHT TTTH ĐS "
                            + reportTypeEnum.getName() + " NĂM " + action.getYear() + " \\n"
                            + branch.getName().toUpperCase() + "\\n");
                } else {
                    if (stationId == CAUGIAT.getId()) {
                        //Add two more columns. Business rule. TODO remove @dungvn3000
                        AdditionStationColumnRule.addStation(stations);
                    }
                    fastReportBuilder.setTitle("KẾ HOẠCH CHI PHÍ VẬT TƯ SCTX – KCHT TTTH ĐS "
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
                            "columns." + station.getId() + ".weight", Double.class, 40, true, "###,###.#", numberStyle);
                    //Last 3 columns is different.
                    if (i >= stations.size() - 3) {
                        fastReportBuilder.addColumn("Tiền",
                                "columns." + station.getId() + ".price", Double.class, 30, false, "###,###.#", numberStyle);
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

        if (reportFileTypeEnum == ReportFileTypeEnum.EXCEL) {
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
