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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<MaterialPerson> materialPersons = generalDao.findCriteria(MaterialPerson.class, Restrictions.eq("station.id", action.getStationId()));
        List<MaterialGroup> materialGroups = generalDao.getAll(MaterialGroup.class);

        List<MaterialIn> materialIns = generalDao.findCriteria(MaterialIn.class,
                Restrictions.eq("quarter", action.getQuarter()),
                Restrictions.eq("year", action.getYear()),
                Restrictions.eq("station.id", action.getStationId()));

        List<Long> materialIds = extract(materialIns, on(MaterialIn.class).getMaterial().getId());

        Map<Long, Double> data = new HashMap<Long, Double>();

        for (TaskMaterialDataView dataView : dataViews) {
            if(!materialIds.contains(dataView.getMaterialId())) {
                for (TaskSumReportBean task : tasks) {
                    if (task.getTask().getId() == dataView.getTaskId()) {
                        StationReportBean stationBean = task.getStations().get(String.valueOf(action.getStationId()));
                        if (stationBean != null) {
                            double weight = stationBean.getValue() * dataView.getQuantity();
                            if (data.get(dataView.getMaterialId()) == null) {
                                data.put(dataView.getMaterialId(), weight);
                            } else {
                                weight += data.get(dataView.getMaterialId());
                                data.put(dataView.getMaterialId(), weight);
                            }
                        }
                    }
                }
            }
        }

        SimpleRegexMatcher matcher = new SimpleRegexMatcher();
        List<MaterialIn> copyData = new ArrayList<MaterialIn>();
        for (Long materialId : data.keySet()) {
            Material material = selectUnique(materials, having(on(Material.class).getId(), equalTo(materialId)));
            MaterialIn materialIn = new MaterialIn();
            materialIn.setCreateBy(1l);
            materialIn.setUpdateBy(1l);

            if (!materialPersons.isEmpty()) {
                materialIn.setMaterialPerson(materialPersons.get(0));
            }

            for (TaskMaterialDataView dataView : dataViews) {
                if (dataView.getMaterialId() == materialId) {
                    TaskSumReportBean task = selectUnique(tasks, having(on(TaskSumReportBean.class).getTask().getId(), equalTo(dataView.getTaskId())));
                    if (task != null) {
                        for (MaterialGroup materialGroup : materialGroups) {
                            for (String regex : materialGroup.getRegexs()) {
                                if (matcher.match(task.getTask().getCode(), regex)) {
                                    String groupCode = materialGroup.getCode().split("\\.")[0];
                                    MaterialGroup rootGroup = selectUnique(materialGroups, having(on(MaterialGroup.class).getCode(), equalTo(groupCode)));
                                    if (rootGroup != null) {
                                        materialIn.setMaterialGroup(rootGroup);
                                    }
                                }
                            }
                        }
                    }
                }
            }


            materialIn.setTotal(data.get(materialId));
            materialIn.setMaterial(material);
            materialIn.setStation(station);
            materialIn.setYear(action.getYear());
            materialIn.setQuarter(action.getQuarter());
            copyData.add(materialIn);
        }

        generalDao.saveOrUpdate(copyData);

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
