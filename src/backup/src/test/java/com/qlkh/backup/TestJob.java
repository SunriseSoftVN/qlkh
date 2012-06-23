/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.backup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The Class TestJob.
 *
 * @author Nguyen Duc Dung
 * @since 6/23/12, 3:54 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/backup.xml")
public class TestJob {

    @Test
    public void test() throws InterruptedException {
        Thread.sleep(1500);
    }

}
