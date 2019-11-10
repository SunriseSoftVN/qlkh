/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.util;

import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.constant.SettingEnum;
import com.qlkh.core.client.model.Settings;
import com.qlkh.server.dao.SettingDao;
import org.jfree.data.time.Quarter;

import java.util.Calendar;
import java.util.Date;

/**
 * The Class DateTimeUtils.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/12, 10:31 PM
 */
public final class DateTimeUtils {

    private static Calendar cal = Calendar.getInstance();

    private DateTimeUtils() {

    }

    public static int getCurrentYear(SettingDao dao) {
        Settings settings = dao.findByName(SettingEnum.YEAR.getName());

        if (settings != null) {
            int year = Integer.parseInt(settings.getValue());
            return year;
        } else {
            return cal.get(Calendar.YEAR);
        }
    }

    public static int getLastYear(SettingDao dao) {
        return getCurrentYear(dao) - 1;
    }

    public static QuarterEnum getCurrentQuarter(SettingDao dao) {
        int currentYear = getCurrentYear(dao);

        if (currentYear == cal.get(Calendar.YEAR)) {
            int month = cal.get(Calendar.MONTH);
            int quarter = month / 3 + 1;
            return QuarterEnum.valueOf(quarter);
        } else {
            return QuarterEnum.Q1;
        }
    }

    public static int getDateForQuarter(int quarter, int year) {
        Quarter quarter1 = new Quarter(quarter, year);
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(quarter1.getStart());

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(quarter1.getEnd());

        return (int) daysBetween(startDate, endDate);
    }

    /**
     * author: http://tripoverit.blogspot.sg/2007/07/java-calculate-difference-between-two.html
     */
    //assert: startDate must be before endDate
    public static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public static String dateTimeInVietnamese() {
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        return "Ngày " + day + " tháng " + month + " năm " + year;
    }

    public static String dateTimeInVietnamese(Date date) {
        int day = date.getDate();
        int month = date.getMonth() + 1;
        int year = date.getYear() + 1900;
        return "Ngày " + day + " tháng " + month + " năm " + year;
    }

}
