package com.qlkh.server.rest;

import com.qlkh.core.client.action.report.TaskReportAction;
import com.qlkh.core.client.constant.ReportTypeEnum;
import com.qlkh.server.handler.report.TaskReportHandler;
import net.customware.gwt.dispatch.shared.ActionException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The Class ReportController.
 *
 * @author Nguyen Duc Dung
 * @since 4/20/14 12:09 PM
 */
@Controller
public class ReportController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public @ResponseBody UserBean getReport() throws ActionException {
        TaskReportAction action = new TaskReportAction();
        action.setStationId(31l);
        action.setReportTypeEnum(ReportTypeEnum.Q2);
        action.setYear(2014);

        getTaskReportHandler().buildReportData(action);
        return new UserBean("dung ne");
    }

    private TaskReportHandler getTaskReportHandler() {
        return applicationContext.getBean(TaskReportHandler.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
