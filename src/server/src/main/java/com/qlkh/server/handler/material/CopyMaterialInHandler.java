package com.qlkh.server.handler.material;

import com.qlkh.core.client.action.material.CopyMaterialInAction;
import com.qlkh.core.client.action.material.CopyMaterialInResult;
import com.qlkh.core.client.action.report.TaskReportAction;
import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.model.*;
import com.qlkh.core.client.model.view.TaskMaterialDataView;
import com.qlkh.core.client.report.StationReportBean;
import com.qlkh.core.client.report.TaskSumReportBean;
import com.qlkh.server.business.rule.StationCodeEnum;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.dao.core.GeneralDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.handler.report.TaskReportHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.apache.commons.digester.SimpleRegexMatcher;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.*;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * The Class CopyMaterialInHandler.
 *
 * @author Nguyen Duc Dung
 * @since 4/30/13 1:19 AM
 */
public class CopyMaterialInHandler extends AbstractHandler<CopyMaterialInAction, CopyMaterialInResult> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private SqlQueryDao sqlQueryDao;

    @Autowired
    private GeneralDao generalDao;

    @Override
    public Class<CopyMaterialInAction> getActionType() {
        return CopyMaterialInAction.class;
    }

    @Override
    public CopyMaterialInResult execute(CopyMaterialInAction action, ExecutionContext context) throws DispatchException {
        List<Material> materials = generalDao.getAll(Material.class);
        List<Station> stations = generalDao.findCriteria(Station.class, Restrictions.ne("id", StationCodeEnum.COMPANY.getId()));
        List<MaterialPerson> materialPersons = generalDao.getAll(MaterialPerson.class);
        List<MaterialGroup> materialGroups = generalDao.getAll(MaterialGroup.class);
        List<MaterialIn> updateData = new ArrayList<MaterialIn>();
        List<MaterialIn> removeData = new ArrayList<MaterialIn>();

        //copy for whole year
        for (QuarterEnum quarter : QuarterEnum.values()) {
            action.setQuarter(quarter.getCode());
            action.setStationId(StationCodeEnum.COMPANY.getId());

            List<TaskSumReportBean> tasks = getTaskReportHandler().buildReportData(new TaskReportAction(action));
            List<TaskMaterialDataView> dataViews = sqlQueryDao.getTaskMaterial(action.getYear(), action.getQuarter());
            List<MaterialIn> materialIns = generalDao.findCriteria(MaterialIn.class,
                    Restrictions.eq("quarter", action.getQuarter()),
                    Restrictions.eq("year", action.getYear()));

            List<Long> materialIds = extract(materialIns, on(MaterialIn.class).getMaterial().getId());

            Map<String, MaterialIn> copyData = new HashMap<String, MaterialIn>();
            SimpleRegexMatcher matcher = new SimpleRegexMatcher();
            for (TaskMaterialDataView dataView : dataViews) {
                if (!materialIds.contains(dataView.getMaterialId())) {
                    for (TaskSumReportBean task : tasks) {
                        if (task.getTask().getId() == dataView.getTaskId()) {
                            for (Station station : stations) {
                                StationReportBean stationBean = task.getStations().get(String.valueOf(station.getId()));
                                if (stationBean != null && stationBean.getValue() != null) {
                                    for (MaterialGroup materialGroup : materialGroups) {
                                        if (materialGroup.getRegexs() != null) {
                                            for (String regex : materialGroup.getRegexs()) {
                                                if (matcher.match(task.getTask().getCode(), regex)) {
                                                    String groupCode = materialGroup.getCode().split("\\.")[0];
                                                    MaterialGroup rootGroup = selectUnique(materialGroups,
                                                            having(on(MaterialGroup.class).getCode(), equalTo(groupCode)));
                                                    if (rootGroup != null) {
                                                        String key = dataView.getMaterialId() + "-" + rootGroup.getId();
                                                        double weight = stationBean.getValue() * dataView.getQuantity();

                                                        if (copyData.get(key) != null) {
                                                            MaterialIn materialIn = copyData.get(key);
                                                            weight = materialIn.getTotal() + weight;
                                                            materialIn.setTotal(weight);
                                                        } else {
                                                            Material material = selectUnique(materials,
                                                                    having(on(Material.class).getId(), equalTo(dataView.getMaterialId())));

                                                            MaterialIn materialIn = new MaterialIn();
                                                            materialIn.setCreateBy(1l);
                                                            materialIn.setUpdateBy(1l);
                                                            materialIn.setMaterial(material);
                                                            materialIn.setStation(station);
                                                            materialIn.setYear(action.getYear());
                                                            materialIn.setQuarter(action.getQuarter());
                                                            materialIn.setExportDate(new Date());

                                                            materialIn.setTotal(weight);

                                                            if (!materialPersons.isEmpty()) {
                                                                MaterialPerson materialPerson = selectFirst(materialPersons,
                                                                        having(on(MaterialPerson.class).getStation().getId(), equalTo(station.getId())));
                                                                if (materialPerson != null) {
                                                                    materialIn.setMaterialPerson(materialPerson);
                                                                }
                                                            }

                                                            materialIn.setMaterialGroup(rootGroup);
                                                            copyData.put(key, materialIn);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            List<Long> materialIds2 = extract(dataViews, on(TaskMaterialDataView.class).getMaterialId());
            for (MaterialIn materialIn : materialIns) {
                if (!materialIds2.contains(materialIn.getMaterial().getId())) {
                    if (materialIn.getWeight() == null && materialIn.getCode() == null) {
                        removeData.add(materialIn);
                    }
                }
            }

            updateData.addAll(copyData.values());
        }

        generalDao.saveOrUpdate(updateData);
        generalDao.deleteByIds(MaterialIn.class, extract(removeData, on(MaterialIn.class).getId()));
        return new CopyMaterialInResult();
    }

    private TaskReportHandler getTaskReportHandler() {
        return applicationContext.getBean(TaskReportHandler.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
