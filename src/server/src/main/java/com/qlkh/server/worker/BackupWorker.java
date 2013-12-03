/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class BackupWorker.
 *
 * @author Nguyen Duc Dung
 * @since 6/23/12, 4:01 PM
 */
public class BackupWorker implements Worker {

    private Logger logger = LoggerFactory.getLogger(BackupWorker.class);
    private String userName;
    private String passWord;
    private String dir;

    @Override
    public void workForMe() {
        String command = "mysqldump -u" + userName;

        if (passWord != null && passWord.length() > 0) {
            command += " -p" + passWord;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String fileName = "qlkh-" + dateFormat.format(new Date()) + ".sql";

        command += " qlkh -r " + dir + fileName;

        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            logger.error("Can't not execute mysqldump", e);
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
