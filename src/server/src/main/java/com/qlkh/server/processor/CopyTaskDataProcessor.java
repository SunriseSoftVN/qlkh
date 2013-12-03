/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.processor;

import com.qlkh.server.worker.Worker;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * The Class CopyTaskDataProcessor.
 *
 * @author Nguyen Duc Dung
 * @since 12/3/13 9:09 AM
 */
public class CopyTaskDataProcessor implements Processor {

    private Worker copyTaskDataWorker;

    //one time a day.
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    @Override
    public void process() {
        copyTaskDataWorker.workForMe();
    }

    public void setCopyTaskDataWorker(Worker copyTaskDataWorker) {
        this.copyTaskDataWorker = copyTaskDataWorker;
    }
}
