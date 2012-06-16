/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.handler;

import com.qlkh.core.client.action.report.ReportAction;
import com.qlkh.core.client.action.report.ReportResult;
import com.qlkh.core.client.constant.ReportFileTypeEnum;
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

import static org.junit.Assert.assertEquals;

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
    public void testReport() throws DispatchException {
        ReportResult result = dispatch.
                execute(new ReportAction(ReportTypeEnum.CA_NAM, ReportFileTypeEnum.PDF, 27, null, 2012));
        assertEquals(result.getReportUrl(), "http://127.0.0.1:8080/service/report?reportName=kehoachtacnghiep.pdf");
    }
}
