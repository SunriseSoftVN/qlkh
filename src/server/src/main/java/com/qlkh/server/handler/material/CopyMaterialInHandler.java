package com.qlkh.server.handler.material;

import com.qlkh.core.client.action.material.CopyMaterialInAction;
import com.qlkh.core.client.action.material.CopyMaterialInResult;
import com.qlkh.core.client.action.report.TaskReportAction;
import com.qlkh.core.client.model.*;
import com.qlkh.core.client.model.view.TaskMaterialDataView;
import com.qlkh.core.client.report.StationReportBean;
import com.qlkh.core.client.report.TaskSumReportBean;
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
        List<TaskSumReportBean> tasks = getTaskReportHandler().buildReportData(new TaskReportAction(action));
        List<TaskMaterialDataView> dataViews = sqlQueryDao.getTaskMaterial(action.getYear(), action.getQuarter());
        List<Material> materials = generalDao.getAll(Material.class);
        Station station = generalDao.findById(Station.class, action.getStationId());
        List<MaterialPerson> materialPersons = generalDao.findCriteria(MaterialPerson.class,
                Restrictions.eq("station.id", action.getStationId()));
        List<MaterialGroup> materialGroups = generalDao.getAll(MaterialGroup.class);

        List<MaterialIn> materialIns = generalDao.findCriteria(MaterialIn.class,
                Restrictions.eq("quarter", action.getQuarter()),
                Restrictions.eq("year", action.getYear()),
                Restrictions.eq("station.id", action.getStationId()));

        List<Long> materialIds = extract(materialIns, on(MaterialIn.class).getMaterial().getId());

        Map<String, MaterialIn> copyData = new HashMap<String, MaterialIn>();
        SimpleRegexMatcher matcher = new SimpleRegexMatcher();
        for (TaskMaterialDataView dataView : dataViews) {
            if (!materialIds.contains(dataView.getMaterialId())) {
                for (TaskSumReportBean task : tasks) {
                    if (task.getTask().getId() == dataView.getTaskId()) {
                        StationReportBean stationBean = task.getStations().get(String.valueOf(action.getStationId()));
                        if (stationBean != null) {
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
                                                        materialIn.setMaterialPerson(materialPersons.get(0));
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

        generalDao.saveOrUpdate(new ArrayList<MaterialIn>(copyData.values()));
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
