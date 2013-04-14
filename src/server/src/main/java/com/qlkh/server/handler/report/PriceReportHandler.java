package com.qlkh.server.handler.report;

import com.qlkh.core.client.action.report.PriceReportAction;
import com.qlkh.core.client.action.report.PriceReportResult;
import com.qlkh.core.client.action.report.TaskReportAction;
import com.qlkh.core.client.report.SumReportBean;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * The Class PriceReportHandler.
 *
 * @author Nguyen Duc Dung
 * @since 4/14/13 11:00 AM
 */
public class PriceReportHandler extends AbstractHandler<PriceReportAction, PriceReportResult> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Class<PriceReportAction> getActionType() {
        return PriceReportAction.class;
    }

    @Override
    public PriceReportResult execute(PriceReportAction priceReportAction, ExecutionContext executionContext) throws DispatchException {
        TaskReportHandler taskReportHandler = applicationContext.getBean(TaskReportHandler.class);
        List<SumReportBean> beans = taskReportHandler.buildReportData(new TaskReportAction(priceReportAction));
        return new PriceReportResult();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
