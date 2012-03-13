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

package com.qlvt.client.client.utils;

import com.smvp4g.mvp.client.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TaskCodeUtils.
 *
 * @author Nguyen Duc Dung
 * @since 3/13/12, 4:57 PM
 */
public final class TaskCodeUtils {

    public static final String CODE_SEPARATOR = " ; ";

    private TaskCodeUtils() {

    }

    public static List<String> getChildTaskCodes(String childTasks) {
        List<String> childTaskCodes = new ArrayList<String>();
        if (StringUtils.isNotBlank(childTasks)) {
            String[] st = childTasks.split(CODE_SEPARATOR);
            for (String code : st) {
                if (StringUtils.isNotBlank(code)) {
                    childTaskCodes.add(code.trim());
                }
            }
        }
        return childTaskCodes;
    }

}
