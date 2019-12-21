package com.cpto.dapp.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author sunli
 * @date 2018/12/07
 */
public class DateUtil {

    /**
     * 将时间转换为时间戳
     *
     * @return 时间戳
     */
    public static Timestamp timeToStamp(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(str);
        return new Timestamp(date.getTime());
    }

    /**
     * 将时间戳转换为时间
     *
     * @return 时间
     */
    public static String stampToTime(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(new Long(str));
        return simpleDateFormat.format(date);
    }

    /**
     * 返回当前日期
     *
     * @return 当前日期
     */
    public static String today() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    /**
     * 返回当前日期的时间戳
     *
     * @return 当前日期的时间戳
     */
    public static Timestamp todayTimeStamp() throws ParseException {
        return timeToStamp(fullFromTime(today()));
    }

    /**
     * 返回当前时间的时间戳
     *
     * @return 当前时间的时间戳
     */
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 返回过期时间的时间戳
     *
     * @param from    起始时间
     * @param timeout 超时时间
     * @return 过期时间的时间戳
     */
    public static Timestamp expiresTime(Timestamp from, Long timeout) {
        return new Timestamp(from.getTime() + timeout);
    }

    /**
     * 判断时间是否过期
     *
     * @param expiresTime 过期时间
     * @return 是否过期
     */
    public static boolean isExpires(Timestamp expiresTime) {

        if (ObjectUtil.isEmpty(expiresTime)) {
            return false;
        }

        return DateUtil.now().compareTo(expiresTime) > 0;
    }

    /**
     * 判断时间是否未过期
     *
     * @param expiresTime 过期时间
     * @return 是否未过期
     */
    public static boolean isNotExpires(Timestamp expiresTime) {
        return !isExpires(expiresTime);
    }

    /**
     * 分钟转换为毫秒数
     *
     * @param minute 分钟
     * @return 毫秒数
     * @throws IllegalArgumentException 参数异常
     */
    public static Long minuteToMillisecond(Integer minute) {

        if (ObjectUtil.isEmpty(minute)) {
            throw new IllegalArgumentException();
        }

        return minute.longValue() * 60 * 1000L;
    }

    /**
     * 小时转换为毫秒数
     *
     * @param hour 小时
     * @return 毫秒数
     * @throws IllegalArgumentException 参数异常
     */
    public static Long hourToMillisecond(Integer hour) {

        if (ObjectUtil.isEmpty(hour)) {
            throw new IllegalArgumentException();
        }

        return hour.longValue() * 60 * 60 * 1000L;
    }

    /**
     * 天数转换为毫秒数
     *
     * @param day 天数
     * @return 毫秒数
     * @throws IllegalArgumentException 参数异常
     */
    public static Long dayToMillisecond(Integer day) {

        if (ObjectUtil.isEmpty(day)) {
            throw new IllegalArgumentException();
        }

        return day.longValue() * 24 * 60 * 60 * 1000L;
    }

    /**
     * timestamp类型时间加算天数
     *
     * @param timestamp 时间
     * @param day       加算天数
     * @return 加算天数后的timestamp时间
     * @throws IllegalArgumentException 参数异常
     */
    public static Timestamp timestampAddDay(Timestamp timestamp, Integer day) {

        if (ObjectUtil.isEmpty(timestamp) || ObjectUtil.isEmpty(day)) {
            throw new IllegalArgumentException();
        }

        return new Timestamp(timestamp.getTime() + dayToMillisecond(day));
    }

    /**
     * timestamp类型时间减算天数
     *
     * @param timestamp 时间
     * @param day       减算天数
     * @return 减算天数后的timestamp时间
     */
    public static Timestamp timestampSubDay(Timestamp timestamp, Integer day) {
        return timestampAddDay(timestamp, day * -1);
    }

    /**
     * 返回查询用开始时间（yyyy-mm-dd 00:00:00）
     *
     * @param time 时间
     * @return 开始时间
     */
    public static String fullFromTime(String time) {

        String result = null;

        if (ObjectUtil.isNotEmpty(time)) {
            if (ObjectUtil.equals(time.length(), 4)) {
                result = time + "-01-01 00:00:00";

            } else if (ObjectUtil.equals(time.length(), 7)) {
                result = time + "-01 00:00:00";

            } else if (ObjectUtil.equals(time.length(), 10)) {
                result = time + " 00:00:00";

            } else {
                result = time;
            }
        }

        return result;
    }

    /**
     * 返回查询用结束时间（yyyy-mm-dd 23:59:59）
     *
     * @param time 时间
     * @return 开始时间
     */
    public static String fullToTime(String time) {

        String result = null;

        if (ObjectUtil.isNotEmpty(time)) {
            if (ObjectUtil.equals(time.length(), 4)) {
                result = time + "-12-31 23:59:59";

            } else if (ObjectUtil.equals(time.length(), 7)) {
                // 处理每月最后一天及闰年日期
                Calendar calendar = Calendar.getInstance();
                // 设置年份
                calendar.set(Calendar.YEAR, Integer.valueOf(time.substring(0, 4)));
                // 设置月份
                calendar.set(Calendar.MONTH, Integer.valueOf(time.substring(5, 7)) - 1);
                // 设置日期
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                result = dateFormat.format(calendar.getTime()) + " 23:59:59";

            } else if (ObjectUtil.equals(time.length(), 10)) {
                result = time + " 23:59:59";

            } else {
                result = time;
            }
        }

        return result;
    }
}