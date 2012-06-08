/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.utils;

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
