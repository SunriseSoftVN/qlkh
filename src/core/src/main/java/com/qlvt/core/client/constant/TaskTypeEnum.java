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

package com.qlvt.core.client.constant;

/**
 * The Class TaskTypeEnum.
 *
 * @author Nguyen Duc Dung
 * @since 2/13/12, 5:03 PM
 */
public enum TaskTypeEnum {

    NORMAL(0),
    SUM(1),
    SUBSUM(2);

    private int taskTypeCode;

    TaskTypeEnum(int taskTypeCode) {
        this.taskTypeCode = taskTypeCode;
    }

    public int getTaskTypeCode() {
        return taskTypeCode;
    }

    public static TaskTypeEnum valueOf(int taskType) {
        for (TaskTypeEnum type : values()) {
            if (type.getTaskTypeCode() == taskType) {
                return type;
            }
        }
        return null;
    }
}
