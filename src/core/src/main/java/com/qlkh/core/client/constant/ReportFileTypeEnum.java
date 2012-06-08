/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.constant;

import com.smvp4g.mvp.client.core.utils.StringUtils;

/**
 * The Class ReportFileTypeEnum.
 *
 * @author Nguyen Duc Dung
 * @since 3/6/12, 6:30 PM
 */
public enum ReportFileTypeEnum {

    PDF(".pdf", "application/pdf"),
    EXCEL(".xls", "application/xls");

    private String fileExt;

    private String fileContent;

    ReportFileTypeEnum(String fileExt, String fileContent) {
        this.fileExt = fileExt;
        this.fileContent = fileContent;
    }

    public String getFileExt() {
        return fileExt;
    }

    public String getFileContent() {
        return fileContent;
    }

    public static ReportFileTypeEnum getFileType(String fileName) {
        int index = StringUtils.indexOf(fileName, ".");
        if (index > 0 && index < fileName.length() - 1) {
            String fileExt = fileName.substring(index, fileName.length());
            for (ReportFileTypeEnum typeEnum : values()) {
                if (typeEnum.getFileExt().equals(fileExt)) {
                    return typeEnum;
                }
            }
        }
        return null;
    }
}
