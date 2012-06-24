/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.utils;

import com.google.common.base.Preconditions;
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
    public static final String CODE_JOIN = " - ";
    public static final String CODE_SPLIT = ".";

    private TaskCodeUtils() {

    }

    public static int convert(String code) {
        Preconditions.checkArgument(StringUtils.isNotBlank(code));
        Preconditions.checkArgument(code.length() >= 5);
        Preconditions.checkArgument(NumberUtils.isNumber(code));
        return Integer.valueOf(code.replace(".",""));
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

    public static String extractFormCode(String childTasks) {
        if (StringUtils.isNotBlank(childTasks)) {
            int index = childTasks.indexOf(CODE_JOIN);
            if (index > 0) {
                return childTasks.substring(0, index);
            }
        }
        return null;
    }

    public static String extractToCode(String childTasks) {
        if (StringUtils.isNotBlank(childTasks)) {
            int index = childTasks.indexOf(CODE_JOIN);
            if (index > 0) {
                return childTasks.substring(index + CODE_JOIN.length(), childTasks.length());
            }
        }
        return null;
    }

    public static String getFromCode(String code) {
        if (StringUtils.isNotEmpty(code) && code.length() >= 5) {
            int index = code.indexOf(".") + 1;
            if (index > 1) {
                String first = code.substring(0, index);
                String last = code.substring(index, code.length());
                Integer from = Integer.valueOf(last) + 1;
                return first + from;
            }
        }
        return code;
    }

    public static String getToCode(String code) {
        if (StringUtils.isNotEmpty(code) && code.length() >= 5) {
            String first = code.substring(0, code.length() - 2);
            String last = "99";
            return first + last;
        }
        return code;
    }


    /**
     * Get task prefix
     * Ex: 1.100 -> 100
     * @param code
     * @return null if something is wrong.
     */
    public static Integer getTaskPrefix(String code) {
        if(StringUtils.isNotEmpty(code) && code.length() >= 4) {
            int index = code.indexOf(CODE_SPLIT);
            if(index >= 1) {
                String subCode = code.substring(index + 1, code.length());
                return Integer.valueOf(subCode);
            }
        }
        return null;
    }

}
