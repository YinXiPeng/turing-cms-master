package com.bonc.turing.cms.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yecy
 * @date 2017-03-22 18:16
 */
public class DateUtil {

    /**
     * 获取现在时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateString() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(currentTime);
    }

    /**
     * 时间戳转日期
     *
     * @param time 时间戳
     * @return
     */
    public static String getDateStringByTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(time);
    }

    /**
     * 时间戳转日期
     *
     * @param time 时间戳
     * @return
     */
    public static String getDateStringByTime(long time, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(time);
    }

    /**
     * 日期转时间戳
     *
     * @param date 日期
     * @return
     */
    public static long getTimeByDateString(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.parse(date).getTime();
    }

    /**
     * 获取昨天时间
     *
     * @return
     */
    public static String getYesterday() {
        long time = System.currentTimeMillis() - (1000 * 60 * 60 * 24);
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * 获取当天4点时间
     *
     * @return
     */
    public static long get4AMTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 4);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * 获取当天4点时间
     *
     * @return
     */
    public static long get16PMTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * 获取当天9点时间
     *
     * @return
     */
    public static long get9AMTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 9);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * 获取当天22点时间
     *
     * @return
     */
    public static long get22PMTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 22);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }


    /**
     * 获取当天21点时间
     *
     * @return
     */
    public static long get21PM5MinuteTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 21);
        cal.set(Calendar.SECOND, 5);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * 获取当天24点时间
     *
     * @return
     */
    public static long getTodayLastTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * 获取当天0点时间
     *
     * @return
     */
    public static long getTodayFirstTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * 获取昨天0点时间
     * @return
     */
    public static long getYesterdayFirstTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)-1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * 获取昨天天24点时间
     * @return
     */
    public static  long getYesterdayLastTime(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)-1);
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }



    /**
     * 获取这周日临界时间
     *
     * @param date
     * @return
     */
    public static long getThisWeekLastTime(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        //设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);

        //星期一
        //String begin = sdf.format(cal.getTime());

        //星期日
        cal.add(Calendar.DATE, 6);
        String end = sdf.format(cal.getTime()) + " 23:59:59";
        return getTimeByDateString(end);
    }


    /**
     * 获取7天前凌晨时间戳
     *
     * @return
     */
    public static long get7DaysAgoTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }


    /**
     * 获取15天前凌晨时间戳
     *
     * @return
     */
    public static long get15DaysAgoTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -15);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }


    /**
     * 获取当前时间的整点小时时间戳
     *
     * @return
     */
    public static long getCurrHourTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取下一个小时整点小时时间戳
     *
     * @return
     */
    public static long getNextHourTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取第二天9点整点小时时间戳
     *
     * @return
     */
    public static long getNextDay9AMTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }


    public static int differentDaysByMillisecond(long time1, long time2) {
        int days = (int) ((time1 - time2) / (1000 * 3600 * 24));
        return days;
    }


    /**时间戳
     * 获取当天0点
     * @return
     */
    public static long getTodayBeginTimeStamp()  {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return  c.getTime().getTime();
    }
    /**时间戳
     * 获取当前时间
     * @return
     */
    public static long getTodayNowTimeStamp()  {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return  c.getTime().getTime();
    }

    /**
     * 获取10天前凌晨时间戳
     * @return
     */
    public static long getNDaysAgoTimeStamp(long timeStamp,int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.add(Calendar.DATE, - n);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return  calendar.getTime().getTime();
    }

    /**
     * 获取指定时间戳N天后24点的时间戳
     * @return
     */
    public static long getNDaysAfterTimeStamp(long timeStamp,int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.add(Calendar.DATE,n);
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return  calendar.getTime().getTime();
    }


    /**时间戳
     * 获取当前年开始时间
     * @return
     */
    public static long getThisYearBeginTimeStamp()  {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_YEAR, 0);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return  c.getTime().getTime();
    }
    /**时间戳
     * 获取当前年结束时间
     * @return
     */
    public static long getThisYearLastTimeStamp()  {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_YEAR, 365);
        c.set(Calendar.HOUR_OF_DAY, 24);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return  c.getTime().getTime();
    }

}
