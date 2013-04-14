package com.qlkh.server.handler.report;

import com.qlkh.core.client.action.report.PriceReportAction;
import com.qlkh.core.client.action.report.PriceReportResult;
import com.qlkh.server.handler.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

/**
 * The Class PriceReportHandler.
 *
 * @author Nguyen Duc Dung
 * @since 4/14/13 11:00 AM
 */
public class PriceReportHandler extends AbstractHandler<PriceReportAction, PriceReportResult> {

    @Override
    public Class<PriceReportAction> getActionType() {
        return PriceReportAction.class;
    }

    @Override
    public PriceReportResult execute(PriceReportAction priceReportAction, ExecutionContext executionContext) throws DispatchException {
        return null;
    }
}
