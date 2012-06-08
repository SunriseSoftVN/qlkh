/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
