package com.qlkh.server.rest;

import com.qlkh.core.client.action.report.TaskReportAction;
import com.qlkh.core.client.constant.ReportTypeEnum;
import com.qlkh.core.client.report.TaskSumReportBean;
import com.qlkh.server.handler.report.TaskReportHandler;
import net.customware.gwt.dispatch.shared.ActionException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * The Class ReportController.
 *
 * @author Nguyen Duc Dung
 * @since 4/20/14 12:09 PM
 */
@Controller
public class ReportController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @RequestMapping(value = "/reportStation", method = RequestMethod.GET)
    public @ResponseBody List<TaskSumReportBean> getReportStationData(
            @RequestParam(value="stationId", required=true) long stationId,
            @RequestParam(value="quarter", required=true) int quarter,
            @RequestParam(value="year", required=true) int year
    ) throws ActionException {
        TaskReportAction action = new TaskReportAction();
        action.setStationId(stationId);
        action.setReportTypeEnum(ReportTypeEnum.valueOf(quarter));
        action.setYear(year);
        return getTaskReportHandler().buildReportData(action);
    }

    private TaskReportHandler getTaskReportHandler() {
        return applicationContext.getBean(TaskReportHandler.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
