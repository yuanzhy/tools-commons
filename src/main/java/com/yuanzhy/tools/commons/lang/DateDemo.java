package com.yuanzhy.tools.commons.lang;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateDemo {

    @Test
    public void str2Date() throws Exception {
        final String strDate = "2021-07-04 11:11:11";
        final String pattern = "yyyy-MM-dd HH:mm:ss";
        // 原生写法
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date1 = sdf.parse(strDate);
        // 使用commons写法
        Date date2 = DateUtils.parseDate(strDate, pattern);
    }

    @Test
    public void date2Str() {
        final Date date = new Date();
        final String pattern = "yyyy年MM月dd日";
        // 原生写法
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String strDate = sdf.format(date);
        // 使用commons写法
        String strDate2 = DateFormatUtils.format(date, pattern);
    }

    @Test
    public void dateCalc() {
        final Date date = new Date();
        // 原生写法
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 5); // 加5天
        cal.add(Calendar.HOUR_OF_DAY, -5); // 减5小时
        // 使用commons写法
        Date newDate1 = DateUtils.addDays(date, 5); // 加5天
        Date newDate2 = DateUtils.addHours(date, -5); // 减5小时
        Date newDate3 = DateUtils.truncate(date, Calendar.DATE); // 过滤时分秒
        boolean isSameDay = DateUtils.isSameDay(newDate1, newDate2); // 判断是否是同一天
    }
}
