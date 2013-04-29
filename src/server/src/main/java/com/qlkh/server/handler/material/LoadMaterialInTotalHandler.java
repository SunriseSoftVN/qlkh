package com.qlkh.server.handler.material;

import com.qlkh.core.client.action.material.LoadMaterialInTotalAction;
import com.qlkh.core.client.action.material.LoadMaterialInTotalResult;
import com.qlkh.core.client.action.report.TaskReportAction;
import com.qlkh.core.client.model.view.TaskMaterialDataView;
import com.qlkh.core.client.report.StationReportBean;
import com.qlkh.core.client.report.TaskSumReportBean;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.handler.core.AbstractHandler;
import com.qlkh.server.handler.report.TaskReportHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * The Class LoadMaterialInTotalHandler.
 *
 * @author Nguyen Duc Dung
 * @since 4/29/13 2:54 PM
 */
public class LoadMaterialInTotalHandler extends AbstractHandler<LoadMaterialInTotalAction, LoadMaterialInTotalResult> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private SqlQueryDao sqlQueryDao;


    @Override
    public Class<LoadMaterialInTotalAction> getActionType() {
        return LoadMaterialInTotalAction.class;
    }

    @Override
    public LoadMaterialInTotalResult execute(LoadMaterialInTotalAction action, ExecutionContext context) throws DispatchException {
        List<TaskSumReportBean> tasks = getTaskReportHandler().buildReportData(new TaskReportAction(action));
        List<TaskMaterialDataView> dataViews = sqlQueryDao.getTaskMaterialByMaterialId(action.getMaterialId(), action.getYear(), action.getReportTypeEnum().getValue());

        double total = 0d;
        for(TaskSumReportBean task : tasks) {
            for(TaskMaterialDataView taskMaterial: dataViews) {
                if(taskMaterial.getTaskId() == task.getTask().getId()) {
                    StationReportBean station = task.getStations().get(String.valueOf(action.getStationId()));
                    if (station != null) {
                        double weight = station.getValue() * taskMaterial.getQuantity();
                        total += weight;
                    }
                }
            }
        }

        return new LoadMaterialInTotalResult(total);
    }

    private TaskReportHandler getTaskReportHandler() {
        return applicationContext.getBean(TaskReportHandler.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
