package com.example.asc.asc.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author zhanglize
 * @create 2019/10/11
 */
public class DateUtils {

    /**
     * 默认时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认时间格式：HH:mm
     */
    public static final String DEFAULT_TIME_PATTERN = "HH:mm";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");//日期格式
    public static final SimpleDateFormat DATE_FORMAT_ = new SimpleDateFormat("yyyy-MM-dd");//日期格式
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");//时间时间
    public static final SimpleDateFormat DATE_AND_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//完整日期加时间格式
    public static final Date NOW_DATE = new Date();//当前时间

    /**
     * 判断是否对应的日期格式
     *
     * @param time
     * @return
     */
    public static boolean judgeDateFormat(String time) {
        boolean flag = true;
        try {
            DATE_FORMAT.parse(time);
        } catch (ParseException e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 判断是否对应的时间格式
     *
     * @param time
     * @return
     */
    public static boolean judgeTimeFormat(String time) {
        boolean flag = true;
        try {
            TIME_FORMAT.parse(time);
        } catch (ParseException e) {
            flag = false;
        }
        return flag;
    }


    /**
     * 字符串转换为日期类型
     * string-->yyyy-MM-dd HH:mm:ss
     *
     * @param dateString
     * @return
     */
    public static Date stringToDate(String dateString) {
        try {
            return DATE_AND_TIME_FORMAT.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * 将日期转换为指定格式的日期的形式
     * string-->yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static Date toDate(Date date) {
        try {
            date = DATE_FORMAT_.parse(DATE_FORMAT_.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将日期转换为指定格式的日期的形式
     * string-->yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String toStringDate(Date date) {
        return DATE_FORMAT_.format(date);
    }


    /**
     * 日期类型转化为字符串
     * yyyy-MM-dd HH:mm:ss -->string
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        try {
            return DATE_AND_TIME_FORMAT.format(date);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return date.toString();
    }

    /**
     * yyyy-MM-dd HH:mm:ss --->yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String timeToDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * yyyy-MM-dd HH:mm:ss --->HH:mm
     *
     * @param time
     * @return
     */
    public static String dateToOnlyTime(String time) {
        Date date = DateUtils.stringToDate(time);
        return new SimpleDateFormat(DEFAULT_TIME_PATTERN).format(date);
    }

    /**
     * yyyy-MM-dd+HH:mm:ss = yyyy-MM-dd HH:mm:ss
     *
     * @param date yyyy-MM-dd
     * @param time HH:mm:ss
     * @return
     */
    public static Date groupDateAndTime(Date date, Date time) {
        Calendar c1 = Calendar.getInstance();//yyyy-MM-dd
        c1.setTime(date);
        Calendar c2 = Calendar.getInstance();//HH:mm:ss
        c2.setTime(time);
        Calendar group = Calendar.getInstance();//yyyy-MM-dd HH:mm:ss
        group.set(Calendar.YEAR, c1.get(Calendar.YEAR));
        group.set(Calendar.MONTH, c1.get(Calendar.MONTH));
        group.set(Calendar.DATE, c1.get(Calendar.DATE));
        group.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR));
        group.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));
        group.set(Calendar.SECOND, c2.get(Calendar.SECOND));
        String s = DATE_AND_TIME_FORMAT.format(group.getTime()); //经过一次转化为标准格式的处理
        try {
            return DATE_AND_TIME_FORMAT.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定日期yyyy-MM-dd HH:mm:ss 的工具信息
     *
     * @param
     * @return
     */
    public static String stringToDate() {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
        return df.format(new Date());
    }

    /**
     * 获取指定日期HH:mm 的工具信息
     *
     * @param
     * @return
     */
    public static String nowTime() {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_TIME_PATTERN);
        return df.format(new Date());
    }

    /**
     * 获取到昨天的日期信息 yyyyMMdd
     *
     * @param
     * @return
     */
    public static String yesterDayTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return DATE_FORMAT.format(cal.getTime());//获取昨天日期
    }


    /**
     * 获取到当天Timestamp yyyy-MM-dd HH:mm:ss
     *
     * @param
     * @return
     */
    public static Timestamp toTimestamp() {
        return Timestamp.valueOf(DATE_AND_TIME_FORMAT.format(new Date()));
    }

}
