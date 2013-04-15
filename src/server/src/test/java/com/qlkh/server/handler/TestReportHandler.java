/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler;

import com.qlkh.core.client.action.report.PriceReportAction;
import com.qlkh.core.client.action.report.TaskReportAction;
import com.qlkh.core.client.constant.ReportFileTypeEnum;
import com.qlkh.core.client.constant.ReportFormEnum;
import com.qlkh.core.client.constant.ReportTypeEnum;
import net.customware.gwt.dispatch.server.Dispatch;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static com.qlkh.server.business.rule.StationCodeEnum.CAUGIAT;

/**
 * The Class TestReportHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/6/12, 10:40 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration
@Transactional
public class TestReportHandler extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private Dispatch dispatch;

    @Test(timeout = 20000)
    public void testTaskReport() throws DispatchException {
        dispatch.execute(new TaskReportAction(ReportTypeEnum.CA_NAM, ReportFormEnum.MAU_2,
                ReportFileTypeEnum.PDF, 31, null, 2012));
        dispatch.execute(new TaskReportAction(ReportTypeEnum.CA_NAM, ReportFormEnum.MAU_2,
                ReportFileTypeEnum.EXCEL, CAUGIAT.getId(), null, 2012));
    }

    @Test(timeout = 20000)
    public void testPriceReport() throws DispatchException {
        dispatch.execute(new PriceReportAction(ReportTypeEnum.CA_NAM, ReportFileTypeEnum.PDF, 27, null, 2013));
    }
}
