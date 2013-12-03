/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.processor;

import com.qlkh.core.configuration.ConfigurationServerUtil;
import com.qlkh.server.worker.Worker;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * The Class BackProcessor.
 *
 * @author Nguyen Duc Dung
 * @since 6/23/12, 4:06 PM
 */
public class BackupProcessor implements Processor {

    private Worker backupWorker;

    //one time a day.
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    @Override
    public void process() {
        if (ConfigurationServerUtil.isProductionMode()) {
            backupWorker.workForMe();
        }
    }

    public void setBackupWorker(Worker backupWorker) {
        this.backupWorker = backupWorker;
    }
}
