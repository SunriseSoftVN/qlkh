/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.backup.processor;

import com.qlkh.backup.worker.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * The Class BackProcessor.
 *
 * @author Nguyen Duc Dung
 * @since 6/23/12, 4:06 PM
 */
public class BackupProcessor implements Processor {

    @Autowired
    private Worker backWorker;

    @Scheduled(cron = "0 0 20 * * *")
    @Override
    public void process() {
        backWorker.workForMe();
    }
}
